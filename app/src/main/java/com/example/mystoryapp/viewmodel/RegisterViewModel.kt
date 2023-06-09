package com.example.mystoryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.api.ApiConfig
import com.example.mystoryapp.response.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel: ViewModel() {
    private val _register = MutableLiveData<RegisterResponse>()
    val register: LiveData<RegisterResponse> = _register

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _showToast = MutableLiveData<String>()
    val showToast: LiveData<String> = _showToast

    fun register(name: String, email: String, password: String){
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.Default){
            val client = ApiConfig.getApiService().register(name, email, password)
            client.enqueue(object : Callback<RegisterResponse>{
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    viewModelScope.launch(Dispatchers.Main){
                        _isLoading.value = false
                        if (response.isSuccessful){
                            if (response.body() != null){
                                _register.value = response.body()
                            }
                        }else{
                            _showToast.value = "Email sudah digunakan!"
                        }
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    viewModelScope.launch(Dispatchers.Main){
                        _isLoading.value = false
                        _showToast.value = t.message
                    }
                }

            })
        }
    }
}