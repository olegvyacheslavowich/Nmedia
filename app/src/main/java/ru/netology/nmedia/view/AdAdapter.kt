package ru.netology.nmedia.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.ActivityAdBinding
import ru.netology.nmedia.model.ad.Ad

class AdAdapter : ListAdapter<Ad, AdViewHolder>(AdDiffItemCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        val binding = ActivityAdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class AdViewHolder(private val binding: ActivityAdBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(ad: Ad) {

        binding.apply {
            titleTextView.text = ad.title
            descriptionTextView.text = ad.description
        }

    }
}

class AdDiffItemCallBack : DiffUtil.ItemCallback<Ad>() {

    override fun areItemsTheSame(oldItem: Ad, newItem: Ad): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Ad, newItem: Ad): Boolean {
        return oldItem == newItem
    }

}