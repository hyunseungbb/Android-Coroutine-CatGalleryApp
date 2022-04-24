package com.hyunseung.catgalleryapp.util

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hyunseung.catgalleryapp.adapter.CatAdapter
import com.hyunseung.catgalleryapp.model.Cat
import com.hyunseung.catgalleryapp.vm.MainViewModel

object BindingAdapters {

    @BindingAdapter("bind_item")
    @JvmStatic
    fun bindCats(recyclerView: RecyclerView, cats: LiveData<List<Cat>>) {
        Log.d("test", "bindcats!!")
        val adapter = recyclerView.adapter
        if (adapter is CatAdapter) {
            cats.value?.let {
                adapter.updateData(it)
            }
        }
    }

    @BindingAdapter("load_image")
    @JvmStatic
    fun loadImage(imageView: ImageView, imgUrl: String) {
        Log.d("test", "loadimage!!")
        Glide.with(imageView.context)
            .load(imgUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)
    }

    @BindingAdapter("bottom_scrolled_event")
    @JvmStatic
    fun bottomScrolledEvent(recyclerView: RecyclerView, vm: ViewModel) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    if (vm is MainViewModel) {
                        vm.getCatList()
                    }
                }
            }
        })
    }
}