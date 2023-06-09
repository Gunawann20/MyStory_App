package com.example.mystoryapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.authenticate.BeginActivity
import com.example.mystoryapp.databinding.ActivityMainBinding
import com.example.mystoryapp.preferences.UserPreference
import com.example.mystoryapp.response.ListStoryItem
import androidx.activity.viewModels
import com.example.mystoryapp.adapter.LoadingStateAdapter
import com.example.mystoryapp.adapter.StoryListAdapter
import com.example.mystoryapp.viewmodel.MainViewModel
import com.example.mystoryapp.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory(this)
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvListStorie.layoutManager = LinearLayoutManager(this)

        getData()

        val userPreference = UserPreference(this)
        val token = userPreference.getUSer().token.toString()
        binding.btnPosting.setOnClickListener {
            val intent = Intent(this@MainActivity, PostingActivity::class.java)
            intent.putExtra("TOKEN", token)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout -> {
                val pref = UserPreference(this)
                pref.removeUser()
                val intent = Intent(this@MainActivity, BeginActivity::class.java)
                startActivity(intent)
            }
            R.id.locationStory -> {
                val intent = Intent(this@MainActivity, StoryMapsActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }
    private fun getData(){
        val adapter = StoryListAdapter()
        binding.rvListStorie.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapter.retry()
            }
        )
        mainViewModel.getStory().observe(this) {
            adapter.submitData(lifecycle, it)
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}