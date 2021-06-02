package com.ahmedmadhoun.qudsnews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ahmedmadhoun.qudsnews.R
import com.ahmedmadhoun.qudsnews.data.PhotoPost
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_add_post.view.*
import kotlinx.android.synthetic.main.item_article_preview.view.*

class PhotoPostsAdapter : RecyclerView.Adapter<PhotoPostsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<PhotoPost>() {
        override fun areItemsTheSame(oldItem: PhotoPost, newItem: PhotoPost): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PhotoPost, newItem: PhotoPost): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_article_preview,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((PhotoPost) -> Unit)? = null

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.photo).into(ivArticleImage)
            tvTitle.text = article.title
            tvDescription.text = article.description
            tvSource.text = article.author
            tvPublishedAt.text = article.createdDateFormatted

            setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }

    fun setOnItemClickListener(listener: (PhotoPost) -> Unit) {
        onItemClickListener = listener
    }
}













