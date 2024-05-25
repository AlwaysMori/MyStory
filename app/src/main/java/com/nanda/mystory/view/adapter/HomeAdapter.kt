package com.nanda.mystory.view.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nanda.mystory.data.api.remote.response.ListStoryItem
import com.nanda.mystory.databinding.ItemStoryBinding
import com.nanda.mystory.utils.toDateFormat
import com.nanda.mystory.view.detail.DetailActivity

class HomeAdapter : PagingDataAdapter<ListStoryItem, HomeAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStoryBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.bind(item)
        }
    }

    class MyViewHolder(
        private val binding: ItemStoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(story: ListStoryItem) {
            with(binding) {
                tvItemName.text = story.name
                tvItemDesc.text = story.description
                tvItemDate.text = story.createdAt?.toDateFormat()

                Glide.with(root)
                    .load(story.photoUrl)
                    .into(ivItemPhoto)

                cardView.setOnClickListener {
                    val context = root.context
                    val intent = Intent(context, DetailActivity::class.java).apply {
                        putExtra(DetailActivity.EXTRA_ID, story.id)
                    }

                    val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context as Activity,
                        Pair(ivItemPhoto, "profile"),
                        Pair(tvItemName, "name"),
                        Pair(tvItemDesc, "description"),
                        Pair(tvItemDate, "date")
                    )

                    context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }





    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }


}