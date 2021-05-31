package com.ahmedmadhoun.qudsnews.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ahmedmadhoun.qudsnews.R
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.fragment_historical_posts.*

class HistoricalPostsFragment : Fragment(R.layout.fragment_historical_posts) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fabAddPost.setOnClickListener {
            val action =
                HistoricalPostsFragmentDirections.actionHistoricalPostsFragmentToAddPostFragment()
            findNavController().navigate(action)
        }

    }

}