package com.hyunseung.catgalleryapp.presenter

import android.util.Log
import com.hyunseung.catgalleryapp.model.CatsProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import kotlin.coroutines.CoroutineContext

class MainPresenter(
    private val view: MainContract.View,
    private val catsProvider: CatsProvider
    ) : MainContract.Presenter, CoroutineScope {

    lateinit var job: Job

    override fun init() {
        job = Job()
    }

    override fun deInit() {
        job.cancel()
    }

    override fun fetch() {
        Log.d("test", "fetch!!")
        launch {
            if (!catsProvider.isTaskRunning()) {
                val channel = catsProvider.execute(this)
                channel.consumeEach { catList ->
                    withContext(Dispatchers.Main) {
                        view.addCatItems(catList)
                    }
                }
            }
        }
    }

    @DelicateCoroutinesApi
    override val coroutineContext: CoroutineContext
        get() = job + newSingleThreadContext("Presenter")
}