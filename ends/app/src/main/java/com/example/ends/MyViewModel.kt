package com.example.ends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

class MyViewModel : ViewModel() {

    private val _result = MutableLiveData<String>()
    val result: LiveData<String> get() = _result

    // Normal suspend work
    fun startWork() {
        viewModelScope.launch {
            try {
                _result.value = "Starting..."
                val data = fetchData()
                _result.value = "Fetched: $data"
            } catch (e: Exception) {
                _result.value = "Error: ${e.message}"
            }
        }
    }

    private suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        delay(1000)
        return@withContext "Hello from suspend"
    }

    // Work with timeout
    fun startWithTimeout() {
        viewModelScope.launch {
            try {
                val result = withTimeout(1500) {
                    delay(1000)
                    "Completed before timeout"
                }
                _result.value = result
            } catch (e: TimeoutCancellationException) {
                _result.value = "Timeout"
            }
        }
    }

    // Work with Flow
    fun collectFlow() {
        viewModelScope.launch {
            createFlow().onEach {
                _result.value = it
            }.collect()
        }
    }

    private fun createFlow(): Flow<String> = flow {
        emit("Flow started")
        delay(500)
        emit("Flow emitted value")
    }

    override fun onCleared() {
        super.onCleared()
    }
}
