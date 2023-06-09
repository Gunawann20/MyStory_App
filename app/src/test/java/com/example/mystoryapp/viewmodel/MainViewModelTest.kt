package com.example.mystoryapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.flatMap
import androidx.recyclerview.widget.ListUpdateCallback
import com.android.example.livedatabuilder.util.getOrAwaitValue
import com.example.mystoryapp.adapter.StoryListAdapter
import com.example.mystoryapp.data.StoryRepository
import com.example.mystoryapp.response.ListStoryItem
import com.example.mystoryapp.utils.CoroutineTestRule
import com.example.mystoryapp.utils.DataDummy
import com.example.mystoryapp.utils.StoryPagingDataSourceTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    private lateinit var mainViewModel: MainViewModel
    private val dummyData = DataDummy.generateDummyData()
    @Mock private lateinit var storyRepository: StoryRepository

    @Before
    fun setUp(){
        mainViewModel = MainViewModel(storyRepository)
    }

    @Test
    fun `data is not null and the number of data is as expected and the first data returned is true`() = runTest{
        val observer = Observer<PagingData<List<ListStoryItem>>> {}
        try {
            val data = StoryPagingDataSourceTest.snapshot(dummyData)

            val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
            expectedStory.value = data
            `when`(storyRepository.getStory()).thenReturn(expectedStory)

            val actualStory = mainViewModel.getStory().getOrAwaitValue()

            val differ = AsyncPagingDataDiffer(
                diffCallback = StoryListAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                mainDispatcher = coroutinesTestRule.testDispatcher,
                workerDispatcher = coroutinesTestRule.testDispatcher
            )
            differ.submitData(actualStory)

            assertNotNull(differ.snapshot())
            assertEquals(dummyData.size, differ.snapshot().size)
            assertEquals(dummyData[0], differ.snapshot()[0])
        }finally {
            mainViewModel.getStory().removeObserver { observer }
        }

    }
    @Test
    fun `the amount of data returned is zero when there is no story data`() = runTest{
        val expectedStory = PagingData.empty<ListStoryItem>()
        `when`(storyRepository.getStory()).thenReturn(MutableLiveData(expectedStory))

        val actualStory = mainViewModel.getStory().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = coroutinesTestRule.testDispatcher,
            workerDispatcher = coroutinesTestRule.testDispatcher
        )
        differ.submitData(actualStory)

        assertEquals(0, differ.itemCount)
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

}


