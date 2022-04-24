package com.hyunseung.catgalleryapp.presenter

import com.hyunseung.catgalleryapp.model.Cat
import com.hyunseung.catgalleryapp.model.CatsProvider

class MainContract {

    interface  View {
        fun addCatItems(catList: List<Cat>)
    }

    interface Presenter {
        fun fetch()
        fun init()
        fun deInit()
    }
}