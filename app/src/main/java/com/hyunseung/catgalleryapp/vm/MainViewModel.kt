package com.hyunseung.catgalleryapp.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyunseung.catgalleryapp.model.Cat
import com.hyunseung.catgalleryapp.model.CatsProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val catsProvider: CatsProvider
) : ViewModel() {

    private val cats = MutableLiveData<List<Cat>>()
    val _cats: LiveData<List<Cat>> = cats
    // catsProvider를 통해 Cat리스트를 받아온다.
    fun getCatList() {
        Log.d("test", "fetch!!")
        viewModelScope.launch(Dispatchers.IO) {
            if (!catsProvider.isTaskRunning()) {
                val channel = catsProvider.execute(this)
                channel.consumeEach { itemList ->
                    Log.d("test", "cats : ${itemList}")
                    cats.postValue(itemList)
                }
            }
        }
    }
}