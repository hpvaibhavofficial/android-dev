package com.example.helpinghands.di

import com.example.helpinghands.repository.data.repo.ServiceRepository
import com.example.helpinghands.repository.data.repo.AuthRepository
import com.example.helpinghands.repository.data.repo.AuthRepositoryImpl
import com.example.helpinghands.repository.data.repo.ECommerceRepository
import com.example.helpinghands.repository.data.repo.ECommerceRepositoryImpl
import com.example.helpinghands.repository.data.repo.ServiceRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module providing Singleton instances of Firebase services and all Repositories.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule { // Changed from 'class' to 'object' for static provider functions

    // --- 1. Firebase Core Providers ---
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }

    // --- 2. Repository Bindings ---

    @Provides
    @Singleton
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository {
        return impl
    }

    @Provides
    @Singleton
    fun bindECommerceRepository(impl: ECommerceRepositoryImpl): ECommerceRepository {
        return impl
    }

    @Provides
    @Singleton
    fun bindServiceRepository(impl: ServiceRepositoryImpl): ServiceRepository {
        return impl
    }
}