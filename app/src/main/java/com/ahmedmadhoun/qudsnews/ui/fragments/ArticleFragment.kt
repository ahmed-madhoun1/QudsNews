package com.ahmedmadhoun.qudsnews.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.ahmedmadhoun.qudsnews.R
import com.ahmedmadhoun.qudsnews.ui.NewsActivity
import com.ahmedmadhoun.qudsnews.ui.NewsViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleFragment : Fragment(R.layout.fragment_article) {

    lateinit var viewModel: NewsViewModel
    val args: ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        val article = args.article
        Glide.with(requireContext()).load(article.urlToImage).into(ivArticleImage)
        tvTitle.text = article.title
        tvDescription.text = article.description
        tvDate.text = article.publishedAt
        tvAuthor.text = article.author



        fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
        }
    }
}