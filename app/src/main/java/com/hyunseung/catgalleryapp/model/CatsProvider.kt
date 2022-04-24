package com.hyunseung.catgalleryapp.model

import android.util.Log
import com.hyunseung.catgalleryapp.remote.response.CatBO
import com.hyunseung.catgalleryapp.remote.response.CatsBO
import com.hyunseung.catgalleryapp.remote.service.CatService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.atomic.AtomicBoolean

class CatsProvider(catService: CatService) {

    val catService: CatService = catService

    private val isTaskRunning = AtomicBoolean(false)
    fun isTaskRunning(): Boolean = isTaskRunning.get()
    private var catData = mutableListOf<Cat>()

    fun execute(coroutineScope: CoroutineScope) : ReceiveChannel<List<Cat>> {
        return coroutineScope.produce() {
            isTaskRunning.set(true)
            val cats: List<CatBO>
            try {
                cats = catService.getCats().cats
                cats.mapIndexed { index, catBO ->
                    with(catBO) {
                        Cat(url, sourceUrl, index)
                    }
                } .let { catList ->
                    catData.addAll(catList)
                    send(catData)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            isTaskRunning.set(false)
        }
    }

}