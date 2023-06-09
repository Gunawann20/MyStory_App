package com.example.mystoryapp.authenticate

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.MainActivity
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivityLoginBinding
import com.example.mystoryapp.preferences.UserPreference
import com.example.mystoryapp.response.LoginResponse
import com.example.mystoryapp.response.UserModel
import com.example.mystoryapp.viewmodel.LoginViewModel
import android.content.Context

class LoginActivity : AppCompatActivity() {
    private lateinit var userModel: UserModel
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val button: Button = binding.btnLogin
        val email: EditText = binding.edtEmail
        val password: EditText = binding.edtPassword
        password.transformationMethod = PasswordTransformationMethod()


        val loginViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[LoginViewModel::class.java]
        loginViewModel.isLoading.observe(this){
            showLoading(it)
        }
        loginViewModel.showToast.observe(this){ showToast ->
            showToast(showToast)
        }

        button.setOnClickListener {
            if (email.text.isEmpty()){
                email.error = getString(R.string.required)
            }
            if (password.text.isEmpty()){
                password.error = getString(R.string.required)
            }
            if (email.error == null && password.error == null){
                loginViewModel.login(email.text.toString(), password.text.toString())
                loginViewModel.login.observe(this){ login->
                    getUserData(login)
                }
            }
        }
        playAnimation()
    }
    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.ivLogin, "alpha", 0f, 1f).apply {
            duration = 5000
        }.start()

        val tvLogin = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(500)
        val edtEmail = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(500)
        val edtPassword = ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(500)
        val btnSubmit = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(tvLogin, edtEmail, edtPassword, btnSubmit)
            start()
        }
    }
    private fun showLoading(isLoading: Boolean){
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }
    private fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun getUserData(loginResponse: LoginResponse){
        val name = loginResponse.loginResult.name
        val userId = loginResponse.loginResult.userId
        val token = loginResponse.loginResult.token
        saveSesionLoginUser(name, userId, token, true)
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
    }
    private fun saveSesionLoginUser(name: String, userId: String, token: String, isLogin: Boolean){
        val userPereference  = UserPreference(this)

        userModel = UserModel()
        userModel.name = name
        userModel.userId = userId
        userModel.token = token
        userModel.isLogin = isLogin

        userPereference.setUser(userModel)
    }
}