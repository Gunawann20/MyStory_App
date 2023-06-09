package com.example.mystoryapp.di

import android.content.Context
import com.example.mystoryapp.api.ApiConfig
import com.example.mystoryapp.data.StoryRepository
import com.example.mystoryapp.preferences.UserPreference

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val userPreference = UserPreference(context)
        return StoryRepository(apiService, userPreference)
    }
}