package com.hyunseung.catgalleryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hyunseung.catgalleryapp.adapter.CatAdapter
import com.hyunseung.catgalleryapp.application.CustomApplication
import com.hyunseung.catgalleryapp.databinding.ActivityMainBinding
import com.hyunseung.catgalleryapp.model.CatsProvider
import com.hyunseung.catgalleryapp.vm.MainViewModel
import com.hyunseung.catgalleryapp.vm.MainViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

// delegation 패턴을 활용하여 CoroutineScope 사용
class MainActivity : AppCompatActivity(), CoroutineScope {
    lateinit var job: Job

    private lateinit var viewModel: MainViewModel
    lateinit var mCatsProvider: CatsProvider

    override val coroutineContext: CoroutineContext
        get() = Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

        // job 생성
        job = Job()
        // catprovider 초기화
        val application = applicationContext as CustomApplication
        with(application) {
            mCatsProvider = catsProvider
        }
        //viewmodel
        viewModel = ViewModelProvider(this, MainViewModelFactory(mCatsProvider))
            .get(MainViewModel::class.java)

        with(binding) {
            lifecycleOwner = this@MainActivity
            vm = viewModel
            catRecyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                setHasFixedSize(true)
                adapter = CatAdapter()
            }
        }

        viewModel.getCatList()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 작업 중이던 모든 job을 종료처리한다.
        job.cancel()
    }
}