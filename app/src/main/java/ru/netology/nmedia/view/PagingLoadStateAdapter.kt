package ru.netology.nmedia.view

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.ItemLoadStateBinding

class PagingLoadStateAdapter(private val onRetryLister: () -> Unit) :
    LoadStateAdapter<PagingLoadStateAdapter.PagingLoadStateViewHolder>() {


    override fun onBindViewHolder(holder: PagingLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PagingLoadStateViewHolder {
        val binding =
            ItemLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagingLoadStateViewHolder(onRetryLister, binding)
    }

    class PagingLoadStateViewHolder(
        private val onRetryLister: () -> Unit,
        private val binding: ItemLoadStateBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(loadState: LoadState) {

            with(binding) {
                errorTextView.isVisible = loadState is LoadState.Error
                horizontalProgressBar.isVisible = loadState is LoadState.Loading
                button.isVisible = loadState is LoadState.Error
                button.setOnClickListener {
                    onRetryLister()
                }
            }

        }


    }
}


