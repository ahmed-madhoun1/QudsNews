package com.ahmedmadhoun.qudsnews.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ahmedmadhoun.qudsnews.R
import com.ahmedmadhoun.qudsnews.adapters.ArticlePostsAdapter
import com.ahmedmadhoun.qudsnews.adapters.MessageAdapter
import com.ahmedmadhoun.qudsnews.data.TextPost
import com.ahmedmadhoun.qudsnews.models.message.Message
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_historical_posts.*
import kotlinx.android.synthetic.main.fragment_messages.*

class MessagesFragment : Fragment(R.layout.fragment_messages) {

    private lateinit var messageList: ArrayList<Message>
    private lateinit var messageAdapter: MessageAdapter
    private val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageAdapter = MessageAdapter()
        messageList = ArrayList()

        getTextPostsFromFirestore()

        fabSendMessage.setOnClickListener {
            val action = MessagesFragmentDirections.actionMessagesFragmentToSendMessageFragment()
            findNavController().navigate(action)
        }

    }


    private fun getTextPostsFromFirestore() {
        recyclerViewMessages.apply {
            setHasFixedSize(true)
            adapter = messageAdapter
        }
        firestore.collection("Messages").addSnapshotListener { value, error ->
            if (error?.message?.isNotEmpty() == true) {
                Toast.makeText(requireActivity(), error.message.toString(), Toast.LENGTH_SHORT)
                    .show()
                return@addSnapshotListener
            }
            value?.documents?.forEach { documentSnapshot ->
                messageList.add(
                    Message(
                        id = documentSnapshot.get("id").toString(),
                        title = documentSnapshot.get("title").toString(),
                        message = documentSnapshot.get("message").toString()
                    )
                )
            }
            messageAdapter.differ.submitList(messageList)
        }
    }


}