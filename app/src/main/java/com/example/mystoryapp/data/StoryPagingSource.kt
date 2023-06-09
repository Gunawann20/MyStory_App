package com.example.mystoryapp.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mystoryapp.api.ApiService
import com.example.mystoryapp.preferences.UserPreference
import com.example.mystoryapp.response.ListStoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StoryPagingSource(private val apiService: ApiService, private val userPreference: UserPreference) : PagingSource<Int, ListStoryItem>() {
    companion object{
        const val INITIAL_PAGE_INDEX = 1
    }
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val token = userPreference.getUSer().token.toString()
            val responseData = withContext(Dispatchers.IO){
                apiService.stories("Bearer $token", position, params.loadSize).execute()
            }

            if (responseData.isSuccessful){
                LoadResult.Page(
                    data = responseData.body()?.listStory ?: emptyList(),
                    prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                    nextKey = if (responseData.body()?.listStory.isNullOrEmpty()) null else position + 1
                )
            } else {
                LoadResult.Error(Exception("Failed to load data"))
            }
        } catch (e: Exception){
            LoadResult.Error(e)
        }
    }
}