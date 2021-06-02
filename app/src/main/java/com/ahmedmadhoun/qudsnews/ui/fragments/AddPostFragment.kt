package com.ahmedmadhoun.qudsnews.ui.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.fragment.app.Fragment
import com.ahmedmadhoun.qudsnews.R
import com.ahmedmadhoun.qudsnews.data.PhotoPost
import com.ahmedmadhoun.qudsnews.data.TextPost
import com.ahmedmadhoun.qudsnews.data.VideoPost
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.enums.EPickType
import kotlinx.android.synthetic.main.fragment_add_post.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class AddPostFragment : Fragment(R.layout.fragment_add_post) {

    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val firebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }
    private lateinit var photoUri: Uri
    private lateinit var videoUri: Uri


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imgViewAddPhoto.setOnClickListener {
            setupImagePicker(imgViewAddPhoto) { uri ->
                photoUri = uri
            }
        }

        videoViewAddVideo.setOnClickListener {
            setupVideoPicker(videoViewAddVideo) { uri ->
                videoUri = uri
            }
        }

        btnAddPost.setOnClickListener {
            addPhotoPost(edTitle.text.toString(), edDescription.text.toString(), edAuthor.text.toString())
        }

    }


    private fun addTextPost(title: String, description: String, author: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val postID = firestore.collection("TextPosts").document().id
            firestore.collection("TextPosts").document(postID)
                .set(
                    TextPost(
                        id = postID,
                        title = title,
                        description = description,
                        author = author
                    )
                )
                .addOnSuccessListener {
                    Toast.makeText(
                        requireActivity(),
                        "Text Post Add Successfully",
                        Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressed()
                }.addOnFailureListener {
                    Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    private fun addPhotoPost(title: String, description: String, author: String) {
        CoroutineScope(Dispatchers.IO).launch {
            if (::photoUri.isInitialized) {
                convertUriToByte(requireContext(), photoUri)?.let { imageByte ->
                    firebaseStorage.reference.child("Posts").child("PhotoPosts")
                        .child(UUID.randomUUID().toString()).putBytes(imageByte)
                        .addOnSuccessListener { taskSnapshot ->
                         taskSnapshot.storage.downloadUrl.addOnSuccessListener {uri->
                             val postID = firestore.collection("PhotoPosts").document().id
                             firestore.collection("PhotoPosts").document(postID)
                                 .set(
                                     PhotoPost(
                                         postID,
                                         uri.toString(),
                                         title,
                                         description,
                                         author
                                     )
                                 )
                                 .addOnSuccessListener {
                                     Toast.makeText(
                                         requireActivity(),
                                         "Photo Post Added Successfully",
                                         Toast.LENGTH_SHORT
                                     ).show()
                                     requireActivity().onBackPressed()
                                 }.addOnFailureListener {
                                     Log.e("TAG", "onViewCreated: ${it.message.toString()}")
                                     Toast.makeText(
                                         requireActivity(),
                                         it.message.toString(),
                                         Toast.LENGTH_SHORT
                                     ).show()
                                 }
                         }
                        }.addOnFailureListener {
                            Log.e("TAG", "onViewCreated: ${it.message.toString()}")
                            Toast.makeText(
                                requireActivity(),
                                it.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }

        }
    }

    private fun addVideoPost(title: String, description: String, author: String) {
        CoroutineScope(Dispatchers.IO).launch {
            if (::videoUri.isInitialized) {
                    firebaseStorage.reference.child("Posts").child("VideoPosts")
                        .child(UUID.randomUUID().toString()).putFile(videoUri)
                        .addOnSuccessListener { taskSnapshot ->
                            val postID = firestore.collection("VideoPosts").document().id
                            firestore.collection("VideoPosts").document(postID)
                                .set(
                                    VideoPost(
                                        postID,
                                        taskSnapshot.storage.path,
                                        title,
                                        description,
                                        author
                                    )
                                )
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        requireActivity(),
                                        "Video Post Added Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    requireActivity().onBackPressed()
                                }.addOnFailureListener {
                                    Log.e("TAG", "onViewCreated: ${it.message.toString()}")
                                    Toast.makeText(
                                        requireActivity(),
                                        it.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }.addOnFailureListener {
                            Log.e("TAG", "onViewCreated: ${it.message.toString()}")
                            Toast.makeText(
                                requireActivity(),
                                it.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
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
            }.setOnPickCancel {
                Toast.makeText(requireActivity(), "Pick Canceled", Toast.LENGTH_SHORT).show()
            }.show(requireActivity().supportFragmentManager)
    }

    private fun setupVideoPicker(videoView: VideoView, pickResultUri: (uri: Uri) -> Unit) {
        val pickSetup = PickSetup().setPickTypes(EPickType.GALLERY).setVideo(true)
        PickImageDialog.build(pickSetup)
            .setOnPickResult { pickResult ->
                pickResultUri(pickResult.uri)
                val mc = MediaController(requireContext())
                videoView.setMediaController(mc)
                videoView.setVideoURI(pickResult.uri)
                videoView.requestFocus()
                videoView.start()
            }.setOnPickCancel {
                Toast.makeText(requireActivity(), "Pick Canceled", Toast.LENGTH_SHORT).show()
            }.show(requireActivity().supportFragmentManager)
    }

    private fun convertUriToByte(context: Context, uri: Uri): ByteArray? =
        context.contentResolver.openInputStream(uri)?.buffered()?.use { it.readBytes() }
}