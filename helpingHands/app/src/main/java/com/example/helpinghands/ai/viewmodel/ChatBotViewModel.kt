package com.example.helpinghands.ai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helpinghands.ai.repository.AiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatBotViewModel(private val repo: AiRepository) : ViewModel() {

    private val _botReply = MutableStateFlow("")
    val botReply = _botReply.asStateFlow()

    fun sendMessage(msg: String) {
        viewModelScope.launch {
            val response = repo.askBot(msg)
            _botReply.value = response.reply
        }
    }
}
