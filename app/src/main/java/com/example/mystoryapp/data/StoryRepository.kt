package com.example.mystoryapp.data

import android.graphics.pdf.PdfDocument.Page
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.mystoryapp.api.ApiService
import com.example.mystoryapp.preferences.UserPreference
import com.example.mystoryapp.response.ListStoryItem

class StoryRepository(private val apiService: ApiService, private val userPreference: UserPreference) {
    fun getStory(): LiveData<PagingData<ListStoryItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, userPreference)
            }
        ).liveData
    }
}