package com.example.mystoryapp.viewmodel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.StoryMapsActivity
import com.example.mystoryapp.api.ApiConfig
import com.example.mystoryapp.response.ListStoryItem
import com.example.mystoryapp.response.StoriesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryMapsViewModel: ViewModel() {

    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> = _stories

    fun getStoryMaps(token: String){
        viewModelScope.launch(Dispatchers.Default) {
            val client = ApiConfig.getApiService().stories("Bearer $token", 1)
            client.enqueue(object : Callback<StoriesResponse>{
                override fun onResponse(
                    call: Call<StoriesResponse>,
                    response: Response<StoriesResponse>
                ) {
                    viewModelScope.launch(Dispatchers.Main) {
                        if (response.isSuccessful){
                            if (response.body() != null){
                                _stories.value = response.body()!!.listStory
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {

                }

            })
        }
    }
}