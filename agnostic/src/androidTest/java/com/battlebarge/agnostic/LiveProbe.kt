package com.battlebarge.agnostic

import androidx.test.platform.app.InstrumentationRegistry
import androidx.room.Room
import com.battlebarge.agnostic.data.local.EdenDatabase
import com.battlebarge.agnostic.data.parser.SingularityParser
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * The LiveProbe validation test.
 * This test runs on a device/emulator to verify that the Agnostic Engine 
 * correctly parses XML and commits it to the Eden database.
 */
class LiveProbe {

    @Test
    fun verify_engine_parsing_and_persistence() = runBlocking {
        // 1. Setup - Create an in-memory Eden database
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val db = Room.inMemoryDatabaseBuilder(context, EdenDatabase::class.java).build()
        val parser = SingularityParser()
        val dao = db.singularityDao()

        // 2. Load the Mock File from Assets
        val inputStream = context.assets.open("mock_rules.cat")

        // 3. Action - Parse XML into Singularities
        val result = parser.parse(inputStream)

        // 4. Action - Commit to Eden
        dao.replaceData(result.entities, result.tags)

        // 5. Verification - Query the database to confirm it worked
        val unit = dao.getSingularityById("unit-01")
        
        println("PROBE LOG: Found Singularity in Eden: ${unit?.name}")
        
        // Assert that the data in the database matches our mock XML
        assertEquals("Battle Barge Knight", unit?.name)
        assertEquals("unit-01", unit?.id)
        
        db.close()
    }
}
