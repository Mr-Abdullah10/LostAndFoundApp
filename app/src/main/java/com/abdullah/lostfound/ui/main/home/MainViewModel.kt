package com.abdullah.lostfound.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdullah.lostfound.Repositories.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    private val _currentUser = MutableStateFlow(authRepository.getCurrentUser())
    val currentUser: StateFlow<FirebaseUser?> = _currentUser



    fun logout() {
        viewModelScope.launch {
            val result = authRepository.logout()
            if (result.isSuccess) {
                _currentUser.value = null
            }
        }
    }
}
