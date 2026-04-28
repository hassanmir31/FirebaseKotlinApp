package com.hassanmir.assignment2

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var ivProfile: ImageView

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            uploadImageToFirebase(uri)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        ivProfile = view.findViewById(R.id.ivProfile)

        view.findViewById<TextView>(R.id.tvEmail).text = auth.currentUser?.email

        loadExistingImage()

        view.findViewById<Button>(R.id.btnUploadImage).setOnClickListener {
            pickImage.launch("image/*")
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri) {
        val uid = auth.currentUser!!.uid
        val ref = storage.reference.child("profileImages/$uid.jpg")

        ref.putFile(imageUri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { url ->
                    Glide.with(this).load(url).into(ivProfile)
                    Toast.makeText(requireContext(), "Image uploaded!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadExistingImage() {
        val uid = auth.currentUser!!.uid
        val ref = storage.reference.child("profileImages/$uid.jpg")

        ref.downloadUrl
            .addOnSuccessListener { url ->
                Glide.with(this).load(url).into(ivProfile)
            }
            .addOnFailureListener {
                // No image yet, default icon stays
            }
    }
}