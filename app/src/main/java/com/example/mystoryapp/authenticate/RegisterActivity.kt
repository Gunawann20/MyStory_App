package com.example.mystoryapp.authenticate

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivityRegisterBinding
import com.example.mystoryapp.viewmodel.RegisterViewModel


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        playAnimation()

        val name: EditText = binding.edtName
        val email: EditText = binding.edtEmail
        val password: EditText = binding.edtPassword
        val button: Button = binding.btnRegister

        password.transformationMethod = PasswordTransformationMethod()

        val registerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[RegisterViewModel::class.java]
        registerViewModel.isLoading.observe(this){
            showLoading(it)
        }
        registerViewModel.showToast.observe(this){ showToast->
            showToast(showToast)
        }
        button.setOnClickListener {
            if (name.text.isEmpty()){
                name.error = getString(R.string.required)
            }
            if (email.text.isEmpty()){
                email.error = getString(R.string.required)
            }
            if (password.text.isEmpty()){
                password.error = getString(R.string.required)
            }

            if (name.error == null && email.error == null && password.error == null){
                registerViewModel.register(name.text.toString(), email.text.toString(), password.text.toString())
                registerViewModel.register.observe(this){ register->
                    if (!register.error){
                        showToast("Berhasil membuat akun")
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }else{
                        showToast("Email sudah digunakan")
                    }
                }
            }
        }
    }
    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.imageView2, View.ALPHA, 1f).apply {
            duration = 5000
        }.start()

        val tvRegister = ObjectAnimator.ofFloat(binding.textView2, View.ALPHA, 1f).setDuration(500)
        val edtName = ObjectAnimator.ofFloat(binding.edtName, View.ALPHA, 1f).setDuration(500)
        val edtEmail = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(500)
        val edtPassword = ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(500)
        val btnRegister = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(tvRegister, edtName,edtEmail,edtPassword, btnRegister)
            start()
        }
    }
    private fun showToast(message: String){
        Toast.makeText(this@RegisterActivity, message,Toast.LENGTH_SHORT).show()
    }
    private fun showLoading(isLoading: Boolean){
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

}