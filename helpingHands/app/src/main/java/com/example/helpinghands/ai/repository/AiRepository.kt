package com.example.helpinghands.ai.repository

import com.example.helpinghands.ai.data.remote.AiApiService
import com.example.helpinghands.ai.data.model.AiRequest

class AiRepository(private val api: AiApiService) {

    suspend fun askBot(message: String) =
        api.getAiReply(AiRequest(message))
}
