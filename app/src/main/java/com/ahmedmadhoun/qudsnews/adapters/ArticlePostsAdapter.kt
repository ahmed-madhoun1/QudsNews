package com.ahmedmadhoun.qudsnews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ahmedmadhoun.qudsnews.R
import com.ahmedmadhoun.qudsnews.data.TextPost
import kotlinx.android.synthetic.main.article_item.view.*
import kotlinx.android.synthetic.main.item_article_preview.view.tvDescription
import kotlinx.android.synthetic.main.item_article_preview.view.tvTitle

class ArticlePostsAdapter : RecyclerView.Adapter<ArticlePostsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<TextPost>() {
        override fun areItemsTheSame(oldItem: TextPost, newItem: TextPost): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TextPost, newItem: TextPost): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.article_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((TextPost) -> Unit)? = null

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            tvTitle.text = article.title
            tvDescription.text = article.description
            tvAuthor.text = article.author
            tvDate.text = article.createdDateFormatted

            setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }

    fun setOnItemClickListener(listener: (TextPost) -> Unit) {
        onItemClickListener = listener
    }
}













