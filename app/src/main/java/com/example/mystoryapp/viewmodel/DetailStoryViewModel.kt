package com.example.mystoryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapp.api.ApiConfig
import com.example.mystoryapp.response.DetailStoryResponse
import com.example.mystoryapp.response.Story
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailStoryViewModel : ViewModel() {

    private val _setStory = MutableLiveData<Story>()
    val setStory: LiveData<Story> = _setStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _showToast = MutableLiveData<String>()
    val showToast: LiveData<String> = _showToast


    fun getStory(id: String, token: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().detailStory("Bearer $token", id)
        client.enqueue(object : Callback<DetailStoryResponse>{
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error){
                        _setStory.value = responseBody.story
                    }
                }else{
                    _showToast.value = response.message()
                }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                _isLoading.value = false
                _showToast.value = t.message
            }

        })
    }
}