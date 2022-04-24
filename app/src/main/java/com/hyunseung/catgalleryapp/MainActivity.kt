package com.hyunseung.catgalleryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hyunseung.catgalleryapp.application.CustomApplication
import com.hyunseung.catgalleryapp.model.CatsProvider
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

    // Job 등록을 위해 초기화
    lateinit var job: Job
    private lateinit var catAdapter: CatAdapter
    lateinit var mCatsProvider: CatsProvider

    // 기본 스레드와 job을 합친 새로운 context를 사용한다.
    override val coroutineContext: CoroutineContext
        get() = Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val application = applicationContext as CustomApplication
        with(application) {
            mCatsProvider = catsProvider
        }

        // job 생성
        job = Job()

        // 리사이클러뷰 초기화 및 스크롤 리스너 등록
        findViewById<RecyclerView>(R.id.catRecyclerView).apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = CatAdapter().also {
                catAdapter = it
            }
            addOnScrollListener(watcher)
        }

        updateCat()
    }

    // 리사이클러뷰 끝까지 스크롤할 때마다 고양이 업데이트하도록 오버라이딩
    val watcher = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!recyclerView.canScrollVertically(1)) {
                updateCat()
            }
        }
    }

    // mCatsProvider에서 Cat리스트를 받아 UI업데이트한다.
    private fun updateCat() {
        Log.d("test", "fetch!!")
        launch {
            if (!mCatsProvider.isTaskRunning()) {
                val channel = mCatsProvider.execute(this)
                channel.consumeEach { catList ->
                    withContext(Main) {
                        catAdapter.updateData(catList)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 작업 중이던 모든 job을 종료처리한다.
        job.cancel()
    }
}