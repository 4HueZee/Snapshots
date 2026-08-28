package com.example.battlebarge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * UI State for the Profile Screen.
 */
sealed class ProfileUiState {
    object Idle : ProfileUiState()
    object Loading : ProfileUiState()
    data class Success(val message: String) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

/**
 * ProfileViewModel manages the state of the profile customization UI.
 * It ensures a clean separation between the UI and the data layer (UserRepository).
 */
class ProfileViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Idle)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    // Observational source of truth from repository
    val userProfile: StateFlow<UserProfile?> = UserRepository.userProfileFlow

    // Local form state
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _bio = MutableStateFlow("")
    val bio: StateFlow<String> = _bio.asStateFlow()

    private val _region = MutableStateFlow("NA")
    val region: StateFlow<String> = _region.asStateFlow()

    private val _isPublic = MutableStateFlow(true)
    val isPublic: StateFlow<Boolean> = _isPublic.asStateFlow()

    init {
        // Automatically populate form when profile is first loaded
        viewModelScope.launch {
            userProfile.filterNotNull().first().let { profile ->
                _username.value = profile.username
                _bio.value = profile.bio
                _region.value = profile.region
                _isPublic.value = profile.isPublic
            }
        }
    }

    fun onUsernameChange(name: String) {
        if (name.length <= 15) {
            _username.value = name.replace("\n", "")
            resetStateIfIdle()
        }
    }

    fun onBioChange(newBio: String) {
        if (newBio.length <= 60) {
            _bio.value = newBio.replace("\n", "")
            resetStateIfIdle()
        }
    }

    fun onRegionChange(newRegion: String) {
        _region.value = newRegion
        resetStateIfIdle()
    }

    fun onVisibilityChange(public: Boolean) {
        _isPublic.value = public
        resetStateIfIdle()
    }

    private fun resetStateIfIdle() {
        if (_uiState.value !is ProfileUiState.Idle) {
            _uiState.value = ProfileUiState.Idle
        }
    }

    /**
     * Commits changes to the repository.
     */
    fun saveChanges() {
        viewModelScope.launch {
            val current = userProfile.value
            val updates = mutableMapOf<String, Any>()
            
            // Delta detection: only send what changed
            if (_username.value.trim() != current?.username) updates["username"] = _username.value.trim()
            if (_bio.value.trim() != current?.bio) updates["bio"] = _bio.value.trim()
            if (_region.value != current?.region) updates["region"] = _region.value
            if (_isPublic.value != current?.isPublic) updates["isPublic"] = _isPublic.value

            if (updates.isEmpty()) {
                _uiState.value = ProfileUiState.Success("Profile is already up to date")
                return@launch
            }

            _uiState.value = ProfileUiState.Loading
            val result = UserRepository.updateProfile(updates)
            
            _uiState.value = result.fold(
                onSuccess = { ProfileUiState.Success("Profile updated successfully") },
                onFailure = { ProfileUiState.Error(it.message ?: "Sync failed. Please try again.") }
            )
        }
    }
}
