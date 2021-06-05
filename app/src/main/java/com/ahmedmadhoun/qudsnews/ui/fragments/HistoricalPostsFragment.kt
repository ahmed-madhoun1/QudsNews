package com.ahmedmadhoun.qudsnews.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.view.size
import androidx.navigation.fragment.findNavController
import com.ahmedmadhoun.qudsnews.R
import com.ahmedmadhoun.qudsnews.adapters.ArticlePostsAdapter
import com.ahmedmadhoun.qudsnews.adapters.PhotoPostsAdapter
import com.ahmedmadhoun.qudsnews.adapters.VideoPostsAdapter
import com.ahmedmadhoun.qudsnews.data.PhotoPost
import com.ahmedmadhoun.qudsnews.data.TextPost
import com.ahmedmadhoun.qudsnews.data.VideoPost
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.fragment_historical_posts.*

class HistoricalPostsFragment : Fragment(R.layout.fragment_historical_posts) {

    private lateinit var articlePostsList: ArrayList<TextPost>
    private lateinit var articlePostsAdapter: ArticlePostsAdapter

    private lateinit var photoPostsList: ArrayList<PhotoPost>
    private lateinit var photoPostsAdapter: PhotoPostsAdapter

    private lateinit var videoPostsList: ArrayList<VideoPost>
    private lateinit var videoPostsAdapter: VideoPostsAdapter

    private val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articlePostsAdapter = ArticlePostsAdapter()
        articlePostsList = ArrayList()

        photoPostsAdapter = PhotoPostsAdapter()
        photoPostsList = ArrayList()

        videoPostsAdapter = VideoPostsAdapter()
        videoPostsList = ArrayList()

        rbText.isChecked = true
        getTextPostsFromFirestore()

        rgPostType.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                rbText.id -> {
                    Toast.makeText(requireActivity(), "TEXT", Toast.LENGTH_SHORT).show()
                    getTextPostsFromFirestore()
                }
                rbPhoto.id -> {
                    Toast.makeText(requireActivity(), "PHOTO", Toast.LENGTH_SHORT).show()
                    getPhotoPostsFromFirestore()
                }
                rbVideo.id -> {
                    Toast.makeText(requireActivity(), "VIDEOS", Toast.LENGTH_SHORT).show()
                    getVideoPostsFromFirestore()
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
            adapter = null
            articlePostsAdapter.notifyItemRangeRemoved(0, size)
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
            adapter = null
            articlePostsAdapter.notifyItemRangeRemoved(0, size)
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

    private fun getVideoPostsFromFirestore() {
        recyclerView.apply {
            adapter = null
            videoPostsAdapter.notifyItemRangeRemoved(0, size)
            setHasFixedSize(true)
            adapter = videoPostsAdapter
        }
        firestore.collection("VideoPosts").addSnapshotListener { value, error ->
            if (error?.message?.isNotEmpty() == true) {
                Toast.makeText(requireActivity(), error.message.toString(), Toast.LENGTH_SHORT)
                    .show()
                return@addSnapshotListener
            }
            value?.documents?.forEach { documentSnapshot ->
                videoPostsList.add(
                    VideoPost(
                        id = documentSnapshot.get("id").toString(),
                        title = documentSnapshot.get("title").toString(),
                        description = documentSnapshot.get("description").toString(),
                        author = documentSnapshot.get("author").toString(),
                        video = documentSnapshot.get("video").toString()
                    )
                )
            }
            videoPostsAdapter.differ.submitList(videoPostsList)
        }
    }

}