package com.ahmedmadhoun.qudsnews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ahmedmadhoun.qudsnews.R
import com.ahmedmadhoun.qudsnews.data.VideoPost
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.android.synthetic.main.video_item.view.*


class VideoPostsAdapter : RecyclerView.Adapter<VideoPostsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<VideoPost>() {
        override fun areItemsTheSame(oldItem: VideoPost, newItem: VideoPost): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: VideoPost, newItem: VideoPost): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.video_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((VideoPost) -> Unit)? = null

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {

            val mediaItem = MediaItem.fromUri(article.video)

            SimpleExoPlayer.Builder(context).build().apply {
                videoView.player = this
                setMediaItem(mediaItem)
                prepare()
            }

            tvTitle.text = article.title
            tvDescription.text = article.description


            setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }

    fun setOnItemClickListener(listener: (VideoPost) -> Unit) {
        onItemClickListener = listener
    }
}













