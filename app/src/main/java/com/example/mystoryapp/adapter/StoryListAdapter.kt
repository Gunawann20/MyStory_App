package com.example.mystoryapp.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.core.content.ContextCompat.startActivity
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mystoryapp.DetailStorieActivity
import com.example.mystoryapp.databinding.ItemStoryBinding
import com.example.mystoryapp.response.ListStoryItem

class StoryListAdapter: PagingDataAdapter<ListStoryItem, StoryListAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListStoryItem) {
            binding.tvName.text = data.name
            binding.tvDescription.text = data.description
            Glide.with(itemView.context).load(data.photoUrl).into(binding.ivStory)
        }
    }
    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>(){
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.photoUrl == newItem.photoUrl && oldItem.name == newItem.name && oldItem.description == newItem.description
            }

        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null){
            Log.d("StoryListAdapter", "onBindViewHolder: $data")
            holder.bind(data)
            holder.itemView.setOnClickListener{
                val intent = Intent(holder.itemView.context, DetailStorieActivity::class.java)
                intent.putExtra("ID", data.id)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
}