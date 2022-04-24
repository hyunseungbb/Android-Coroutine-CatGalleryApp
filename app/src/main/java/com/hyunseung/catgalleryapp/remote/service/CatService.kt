package com.hyunseung.catgalleryapp.remote.service

import com.hyunseung.catgalleryapp.remote.response.CatsBO
import retrofit2.http.GET

interface CatService {
    @GET("api/images/get?format=xml&results_per_page=20")
    suspend fun getCats(): CatsBO
}