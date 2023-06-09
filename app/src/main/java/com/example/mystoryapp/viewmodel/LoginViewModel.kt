package com.example.mystoryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.api.ApiConfig
import com.example.mystoryapp.response.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel: ViewModel() {
    private val _login = MutableLiveData<LoginResponse>()
    val login: LiveData<LoginResponse> = _login

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _showToast = MutableLiveData<String>()
    val showToast: LiveData<String> = _showToast

    fun login(email: String, password: String){
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.Default){
            val client = ApiConfig.getApiService().login(email, password)
            client.enqueue(object :Callback<LoginResponse>{
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    viewModelScope.launch(Dispatchers.Main){
                        _isLoading.value = false
                        if (response.isSuccessful){
                            if (response.body() != null){
                                _login.value = response.body()
                            }
                        }else{
                            _showToast.value = "Email atau password tidak valid!"
                        }
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    viewModelScope.launch(Dispatchers.Main){
                        _isLoading.value = false
                        _showToast.value = t.message
                    }
                }

            })
        }
    }
}