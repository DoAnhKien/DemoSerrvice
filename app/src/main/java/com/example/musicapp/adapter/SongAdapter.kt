package com.example.musicapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.callback.HandleMyOnclick
import com.example.musicapp.databinding.ItemSongBinding
import com.example.musicapp.model.SongItem

class SongAdapter(private val handleMyOnclick: HandleMyOnclick) :
    ListAdapter<SongItem, SongAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentScore = getItem(position)
        holder.bind(currentScore)
    }

    inner class ViewHolder(private val binding: ItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val songItem = getItem(position)
                        handleMyOnclick.onClick(songItem, position)
                    }
                }
                root.setOnLongClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val songItem = getItem(position)
                        handleMyOnclick.onLongClick(songItem, position)
                    }
                    true
                }
            }
        }


        fun bind(songItem: SongItem) {
            binding.songItem = songItem
            binding.executePendingBindings()
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<SongItem>() {
        override fun areItemsTheSame(oldItem: SongItem, newItem: SongItem): Boolean {
            return oldItem.path == newItem.path
        }

        override fun areContentsTheSame(oldItem: SongItem, newItem: SongItem): Boolean {
            return oldItem.path == newItem.path
        }
    }


}