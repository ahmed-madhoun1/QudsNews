package com.ahmedmadhoun.qudsnews.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ahmedmadhoun.qudsnews.R
import com.ahmedmadhoun.qudsnews.data.TextPost
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_add_post.*


class AddPostFragment : Fragment(R.layout.fragment_add_post) {

    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val title = edTitle.text.toString()
        val author = edArticle.text.toString()
        val article = edAuthor.text.toString()

       if(rbText.isSelected){
           addTextPost(title, article, author)
       }else{
           Toast.makeText(requireActivity(), "Text POst not selected", Toast.LENGTH_SHORT).show()
       }

    }

    private fun addTextPost(title: String, article: String, author: String) {
        val postID = firestore.document("").id
        firestore.collection("TextPosts").document(postID)
            .set(TextPost(id = postID, title = title, article = article, author = author))
            .addOnSuccessListener {
                Toast.makeText(requireActivity(), "Text Post Add Successfully", Toast.LENGTH_SHORT).show()
            }
    }

}