package com.ahmedmadhoun.qudsnews.ui.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import com.ahmedmadhoun.qudsnews.R
import com.ahmedmadhoun.qudsnews.data.PhotoPost
import com.ahmedmadhoun.qudsnews.data.TextPost
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.enums.EPickType
import kotlinx.android.synthetic.main.fragment_add_post.*
import java.util.*


class AddPostFragment : Fragment(R.layout.fragment_add_post) {

    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val firebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }
    private lateinit var photoUri: Uri


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imgViewAddPhoto.setOnClickListener {
            setupImagePicker(imgViewAddPhoto) { uri ->
                photoUri = uri
            }
        }

        btnAddPost.setOnClickListener {
//            addTextPost(
//                edTitle.text.toString(),
//                edDescription.text.toString(),
//                edAuthor.text.toString()
//            )

            if(::photoUri.isInitialized){
                convertUriToByte(requireContext(), photoUri)?.let { imageByte ->
                    firebaseStorage.reference.child("Posts").child("PhotoPosts")
                        .child(UUID.randomUUID().toString()).putBytes(imageByte).addOnSuccessListener {
                            Toast.makeText(requireActivity(), "Image Added To Storage Successfully", Toast.LENGTH_SHORT).show()
                            val postID = firestore.collection("PhotoPosts").document().id
                            firestore.collection("PhotoPosts").document(postID)
                                .set(PhotoPost(postID, it.storage.path,edTitle.text.toString(),edDescription.text.toString(),edAuthor.text.toString()))
                        }.addOnFailureListener {
                            Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                }
            }

        }

    }

    private fun setupImagePicker(imageView: ImageView, pickResultUri: (uri: Uri) -> Unit) {
        val pickSetup = PickSetup().setPickTypes(EPickType.GALLERY)
        PickImageDialog.build(pickSetup)
            .setOnPickResult { pickResult ->
                imageView.setImageBitmap(pickResult.bitmap)
                pickResultUri(pickResult.uri)
                Toast.makeText(requireActivity(), pickResult.uri.toString(), Toast.LENGTH_SHORT)
                    .show()
            }.setOnPickCancel {
                Toast.makeText(requireActivity(), "Pick Canceled", Toast.LENGTH_SHORT).show()
            }.show(requireActivity().supportFragmentManager)
    }

    private fun addTextPost(title: String, description: String, author: String) {
        val postID = firestore.collection("TextPosts").document().id
        firestore.collection("TextPosts").document(postID)
            .set(TextPost(id = postID, title = title, description = description, author = author))
            .addOnSuccessListener {
                Toast.makeText(requireActivity(), "Text Post Add Successfully", Toast.LENGTH_SHORT)
                    .show()
            }.addOnFailureListener {
                Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun addPhotoPost(photo: String, title: String, description: String, author: String) {
        val postID = firestore.collection("PhotoPosts").document().id
        firestore.collection("PhotoPosts").document(postID)
            .set(
                PhotoPost(
                    id = postID,
                    photo = photo,
                    title = title,
                    description = description,
                    author = author
                )
            )
            .addOnSuccessListener {
                Toast.makeText(requireActivity(), "Text Post Add Successfully", Toast.LENGTH_SHORT)
                    .show()
            }.addOnFailureListener {
                Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun convertUriToByte(context: Context, uri: Uri): ByteArray? =
        context.contentResolver.openInputStream(uri)?.buffered()?.use { it.readBytes() }
}