package com.zhuinden.mvvmaacrxjavaretrofitroom.features.cats

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hyunseung.catgalleryapp.databinding.ViewCatItemBinding
import com.hyunseung.catgalleryapp.model.Cat
import java.util.*

class CatAdapter() : RecyclerView.Adapter<CatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewCatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = cats.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cats[position])
    }

    private var cats: List<Cat> = Collections.emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(cats: List<Cat>?) {
        this.cats = cats ?: Collections.emptyList()
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ViewCatItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cat: Cat) {
            Glide.with(binding.root)
                .load(cat.url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.catImage)
        }
    }
}