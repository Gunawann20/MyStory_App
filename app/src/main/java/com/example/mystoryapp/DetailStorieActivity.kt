package com.example.mystoryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mystoryapp.databinding.ActivityDetailStorieBinding
import com.example.mystoryapp.preferences.UserPreference
import com.example.mystoryapp.response.Story
import com.example.mystoryapp.viewmodel.DetailStoryViewModel
import java.text.SimpleDateFormat
import java.util.*

class DetailStorieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStorieBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStorieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val token = UserPreference(this).getUSer().token.toString()
        val id = intent.getStringExtra("ID").toString()

        val detailStoryViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DetailStoryViewModel::class.java]
        detailStoryViewModel.getStory(id, token)
        detailStoryViewModel.setStory.observe(this){ setStory->
            setDetailStory(setStory)
        }
        detailStoryViewModel.showToast.observe(this){ message->
            showToast(message)
        }

    }
    private fun setDetailStory(detailStory: Story){
        Glide.with(this).load(detailStory.photoUrl).into(binding.ivDetailStory)
        binding.tvName.text = detailStory.name
        binding.tvDate.text = convertDate(detailStory.createdAt)
        binding.tvDascription.text = detailStory.description
    }
    private fun showToast(message: String){
        Toast.makeText(this@DetailStorieActivity, message, Toast.LENGTH_SHORT).show()
    }
}