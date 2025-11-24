package com.example.helpinghands.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helpinghands.data.model.User
import com.example.helpinghands.repository.data.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    // 🔹 Authentication results
    private val _authState = MutableLiveData<Result<User>>()
    val authState: LiveData<Result<User>> = _authState

    // 🔹 UI Loading state (separate & clean)
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    // 🔹 Sign-out state
    private val _isSignedOut = MutableLiveData<Boolean>()
    val isSignedOut: LiveData<Boolean> = _isSignedOut

    // ---------------------------------------------------------
    // 🔹 Splash Screen → Check if user exists and load role
    // ---------------------------------------------------------
    fun checkCurrentUserRole() {
        val uid = repository.currentUserId
        if (uid != null) {
            _loading.value = true
            viewModelScope.launch {
                val result = repository.getUserRole(uid)
                _loading.postValue(false)
                _authState.postValue(result)
            }
        } else {
            _authState.postValue(Result.failure(Exception("Not Authenticated")))
        }
    }

    // ---------------------------------------------------------
    // 🔹 Sign Up User
    // ---------------------------------------------------------
    fun signUp(email: String, password: String, name: String) {
        _loading.value = true
        viewModelScope.launch {
            val result = repository.signUp(email, password, name)
            _loading.postValue(false)
            _authState.postValue(result)
        }
    }

    // ---------------------------------------------------------
    // 🔹 Login User
    // ---------------------------------------------------------
    fun signIn(email: String, password: String) {
        _loading.value = true
        viewModelScope.launch {
            val result = repository.signIn(email, password)
            _loading.postValue(false)
            _authState.postValue(result)
        }
    }

    // ---------------------------------------------------------
    // 🔹 Logout
    // ---------------------------------------------------------
    fun signOut() {
        repository.signOut()
        _isSignedOut.value = true
    }
}
