package com.hyunseung.catgalleryapp.application

import android.app.Application
import com.hyunseung.catgalleryapp.model.CatsProvider
import com.hyunseung.catgalleryapp.remote.service.CatService
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class CustomApplication: Application() {

    lateinit var retrofit: Retrofit
    lateinit var catService: CatService
    lateinit var catsProvider: CatsProvider
    override fun onCreate() {
        super.onCreate()
        retrofit = Retrofit.Builder()
            .baseUrl("https://thecatapi.com/")
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
        catService = retrofit.create(CatService::class.java)
        catsProvider = CatsProvider(catService)
    }
}