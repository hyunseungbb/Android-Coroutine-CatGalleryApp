package com.hyunseung.catgalleryapp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hyunseung.catgalleryapp.model.CatsProvider
import java.lang.IllegalArgumentException

class MainViewModelFactory(
    private val catsProvider: CatsProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(catsProvider) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}