package com.example.mystoryapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystoryapp.data.StoryRepository
import com.example.mystoryapp.di.Injection
import com.example.mystoryapp.response.ListStoryItem

class MainViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun getStory(): LiveData<PagingData<ListStoryItem>> = storyRepository.getStory().cachedIn(viewModelScope)
}
class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}