package com.nanda.mystory.view.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.core.util.Pair
import android.view.LayoutInflater
import com.nanda.mystory.databinding.ItemStoryBinding
import androidx.core.app.ActivityOptionsCompat
import android.content.Intent
import com.nanda.mystory.view.detail.DetailActivity
import com.bumptech.glide.Glide
import com.nanda.mystory.data.api.remote.response.ListStoryItem
import android.view.ViewGroup
import com.nanda.mystory.utils.toDateFormat
import android.app.Activity


class HomeAdapter : RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {
    private val storyList = ArrayList<ListStoryItem>()

    override fun getItemCount(): Int = storyList.size

    fun submitList(stories: List<ListStoryItem>) {
        storyList.apply {
            clear()
            addAll(stories)
        }
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val storyItem = storyList[position]
        holder.bind(storyItem)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    class MyViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: ListStoryItem) {
            binding.tvItemName.text = story.name
            binding.tvItemDesc.text = story.description
            binding.tvItemDate.text = story.createdAt?.toDateFormat()
            Glide.with(binding.root)
                .load(story.photoUrl)
                .into(binding.ivItemPhoto)

            binding.cardView.setOnClickListener {
                val intent = Intent(binding.root.context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_ID, story.id)
                }

                val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    binding.root.context as Activity,
                    Pair(binding.ivItemPhoto, "profile"),
                    Pair(binding.tvItemName, "name"),
                    Pair(binding.tvItemDesc, "description"),
                    Pair(binding.tvItemDate, "date")
                )
                binding.root.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }
}
