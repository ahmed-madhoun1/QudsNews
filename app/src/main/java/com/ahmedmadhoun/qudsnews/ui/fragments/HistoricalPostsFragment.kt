package com.ahmedmadhoun.qudsnews.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.ahmedmadhoun.qudsnews.R
import com.ahmedmadhoun.qudsnews.adapters.ArticlePostsAdapter
import com.ahmedmadhoun.qudsnews.adapters.PhotoPostsAdapter
import com.ahmedmadhoun.qudsnews.data.PhotoPost
import com.ahmedmadhoun.qudsnews.data.TextPost
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.fragment_historical_posts.*

class HistoricalPostsFragment : Fragment(R.layout.fragment_historical_posts) {

    private lateinit var articlePostsList: ArrayList<TextPost>
    private lateinit var articlePostsAdapter: ArticlePostsAdapter

    private lateinit var photoPostsList: ArrayList<PhotoPost>
    private lateinit var photoPostsAdapter: PhotoPostsAdapter

    private val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articlePostsAdapter = ArticlePostsAdapter()
        articlePostsList = ArrayList()

        photoPostsAdapter = PhotoPostsAdapter()
        photoPostsList = ArrayList()

        rbText.isChecked = true

        rgPostType.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                rbText.id -> {
                    Toast.makeText(requireActivity(), "TEXT", Toast.LENGTH_SHORT).show()
                    photoPostsList.clear()
                    getTextPostsFromFirestore()
                }
                rbPhoto.id -> {
                    Toast.makeText(requireActivity(), "PHOTO", Toast.LENGTH_SHORT).show()
                    articlePostsList.clear()
                    getPhotoPostsFromFirestore()
                }
                rbVideo.id -> {

                }
            }
        }

        articlePostsAdapter.setOnItemClickListener {

        }

        fabAddPost.setOnClickListener {
            val action =
                HistoricalPostsFragmentDirections.actionHistoricalPostsFragmentToAddPostFragment()
            findNavController().navigate(action)
        }

    }

    private fun getTextPostsFromFirestore() {
        recyclerView.apply {
            photoPostsAdapter.notifyDataSetChanged()
            setHasFixedSize(true)
            adapter = articlePostsAdapter
        }
        firestore.collection("TextPosts").addSnapshotListener { value, error ->
            if (error?.message?.isNotEmpty() == true) {
                Toast.makeText(requireActivity(), error.message.toString(), Toast.LENGTH_SHORT)
                    .show()
                return@addSnapshotListener
            }
            value?.documents?.forEach { documentSnapshot ->
                articlePostsList.add(
                    TextPost(
                        id = documentSnapshot.get("id").toString(),
                        title = documentSnapshot.get("title").toString(),
                        description = documentSnapshot.get("description").toString(),
                        author = documentSnapshot.get("author").toString()
                    )
                )
            }
            articlePostsAdapter.differ.submitList(articlePostsList)
        }
    }

    private fun getPhotoPostsFromFirestore() {
        recyclerView.apply {
            articlePostsAdapter.notifyDataSetChanged()
            setHasFixedSize(true)
            adapter = photoPostsAdapter
        }
        firestore.collection("PhotoPosts").addSnapshotListener { value, error ->
            if (error?.message?.isNotEmpty() == true) {
                Toast.makeText(requireActivity(), error.message.toString(), Toast.LENGTH_SHORT)
                    .show()
                return@addSnapshotListener
            }
            value?.documents?.forEach { documentSnapshot ->
                photoPostsList.add(
                    PhotoPost(
                        id = documentSnapshot.get("id").toString(),
                        title = documentSnapshot.get("title").toString(),
                        description = documentSnapshot.get("description").toString(),
                        author = documentSnapshot.get("author").toString(),
                        photo = documentSnapshot.get("photo").toString()
                    )
                )
            }
            photoPostsAdapter.differ.submitList(photoPostsList)
        }
    }

}