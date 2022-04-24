package com.hyunseung.catgalleryapp.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.hyunseung.catgalleryapp.R
import com.hyunseung.catgalleryapp.databinding.ViewCatItemBinding
import com.hyunseung.catgalleryapp.model.Cat
import java.util.*

class CatAdapter() : RecyclerView.Adapter<CatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ViewCatItemBinding>(LayoutInflater.from(parent.context),
        R.layout.view_cat_item, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = cats.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cats[position])
    }

    val cats = mutableListOf<Cat>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(cats: List<Cat>?) {
        Log.d("test", "update data!!")
        this.cats.clear()
        this.cats.addAll(cats!!)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ViewCatItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cat: Cat) {
            binding.cat = cat
        }
    }
}