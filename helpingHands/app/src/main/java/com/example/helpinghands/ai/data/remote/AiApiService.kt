package com.example.helpinghands.ai.data.remote

import com.example.helpinghands.ai.data.model.AiRequest
import com.example.helpinghands.ai.data.model.AiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AiApiService {

    @POST("v1/chat")
    suspend fun getAiReply(@Body request: AiRequest): AiResponse
}
