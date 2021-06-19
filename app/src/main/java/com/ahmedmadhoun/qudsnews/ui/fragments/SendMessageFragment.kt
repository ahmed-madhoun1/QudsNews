package com.ahmedmadhoun.qudsnews.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ahmedmadhoun.qudsnews.R
import com.ahmedmadhoun.qudsnews.models.message.Message
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_send_message.*

class SendMessageFragment : Fragment(R.layout.fragment_send_message) {

    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSendMessage.setOnClickListener {
            if(edMessage.text.toString().isNotEmpty() && edTitleMessage.text.toString().isNotEmpty()){
                val messageID = firestore.collection("Messages").document().id
                firestore.collection("Messages").document(messageID)
                    .set(Message(messageID, edTitleMessage.text.toString(), edMessage.text.toString()))
                    .addOnSuccessListener {
                        Toast.makeText(requireActivity(), "Message Sent Successfully", Toast.LENGTH_SHORT).show()
                        requireActivity().onBackPressed()
                    }
            }else{
                Toast.makeText(requireActivity(), "Fill All The Fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}