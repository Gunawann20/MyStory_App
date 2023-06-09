package com.example.mystoryapp.utils

import com.example.mystoryapp.response.ListStoryItem
import com.example.mystoryapp.response.StoriesResponse

object DataDummy {
    fun generateDummyData(): List<ListStoryItem>{
        val listStory = ArrayList<ListStoryItem>()
        for (i in 0..5){
            val story = ListStoryItem(
                "http:kkkkk",
                "2022-02-22T22:22:22Z",
                "gauna",
                "hakahaj",
                "123",
                "436w7g",
                "1232",
            )
            listStory.add(story)
        }
        return listStory
    }

}