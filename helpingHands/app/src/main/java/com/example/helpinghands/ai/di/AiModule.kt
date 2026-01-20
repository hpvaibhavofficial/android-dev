package com.example.helpinghands.ai.di

import com.example.helpinghands.ai.data.remote.AiApiService
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AiModule {

    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://your-ai-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    fun provideAiService(retrofit: Retrofit): AiApiService =
        retrofit.create(AiApiService::class.java)
}
