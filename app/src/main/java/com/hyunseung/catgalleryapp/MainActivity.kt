package com.hyunseung.catgalleryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hyunseung.catgalleryapp.application.CustomApplication
import com.hyunseung.catgalleryapp.model.CatsProvider
import com.hyunseung.catgalleryapp.vm.MainViewModel
import com.hyunseung.catgalleryapp.vm.MainViewModelFactory
import com.zhuinden.mvvmaacrxjavaretrofitroom.features.cats.CatAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

// delegation 패턴을 활용하여 CoroutineScope 사용
class MainActivity : AppCompatActivity(), CoroutineScope {
    lateinit var job: Job

    private lateinit var viewModel: MainViewModel

    private lateinit var catAdapter: CatAdapter
    lateinit var mCatsProvider: CatsProvider

    override val coroutineContext: CoroutineContext
        get() = Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

        // 리사이클러뷰 초기화 및 스크롤 리스너 등록
        findViewById<RecyclerView>(R.id.catRecyclerView).apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = CatAdapter().also {
                catAdapter = it
            }
            addOnScrollListener(watcher)
        }

        viewModel.catList.observe(this, { catList ->
            Log.d("test", "catList:  ${catList}")
            catAdapter.updateData(catList)
        })
    }

    // 리사이클러뷰 끝까지 스크롤할 때마다 고양이 업데이트하도록 오버라이딩
    val watcher = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!recyclerView.canScrollVertically(1)) {
                viewModel.getCatList()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 작업 중이던 모든 job을 종료처리한다.
        job.cancel()
    }
}