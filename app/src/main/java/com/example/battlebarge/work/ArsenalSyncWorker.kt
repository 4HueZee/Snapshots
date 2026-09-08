package com.example.battlebarge.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.battlebarge.engine.EngineCore
import com.battlebarge.agnostic.domain.model.SingularityState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.coroutineScope

/**
 * Background worker that orchestrates the high-performance Arsenal Sync.
 * Ensures that rule downloads continue even if the user leaves the app.
 */
class ArsenalSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = coroutineScope {
        val owner = inputData.getString("owner") ?: return@coroutineScope Result.failure()
        val repo = inputData.getString("repo") ?: return@coroutineScope Result.failure()
        val branch = inputData.getString("branch") ?: "main"

        val repository = try {
            EngineCore.provideRepository()
        } catch (e: Exception) {
            // Ensure EngineCore is initialized if the process was killed
            EngineCore.initialize(applicationContext)
            EngineCore.provideRepository()
        }

        // 1. Observe repository state to pipe progress back to WorkManager
        val progressJob = launch {
            repository.engineState.collectLatest { state ->
                if (state is SingularityState.BulkLoading) {
                    setProgress(
                        workDataOf(
                            "progress" to state.progress,
                            "message" to state.message,
                            "current" to state.current,
                            "total" to state.total
                        )
                    )
                }
            }
        }

        // 2. Execute the Sync
        try {
            val result = repository.syncArsenal(owner, repo, branch)
            progressJob.cancel()
            
            if (result.isSuccess) {
                Result.success()
            } else {
                // If it's a network error, we can retry
                Result.retry()
            }
        } catch (e: Exception) {
            progressJob.cancel()
            Result.failure()
        }
    }
}
