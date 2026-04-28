package com.hassanmir.assignment2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FeedFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private val postList = mutableListOf<Map<String, String>>()
    private lateinit var adapter: PostAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val btnPost = view.findViewById<Button>(R.id.btnPost)
        val etPost = view.findViewById<EditText>(R.id.etPost)

        adapter = PostAdapter(postList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        loadPosts()

        btnPost.setOnClickListener {
            val text = etPost.text.toString().trim()
            if (text.isEmpty()) {
                Toast.makeText(requireContext(), "Enter something to post", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            uploadPost(text)
            etPost.setText("")
        }
    }

    private fun uploadPost(text: String) {
        val post = hashMapOf(
            "text" to text,
            "email" to (auth.currentUser?.email ?: "unknown")
        )
        db.collection("posts").add(post)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Posted!", Toast.LENGTH_SHORT).show()
                loadPosts()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadPosts() {
        db.collection("posts").get()
            .addOnSuccessListener { result ->
                postList.clear()
                for (doc in result) {
                    postList.add(
                        mapOf(
                            "text" to (doc.getString("text") ?: ""),
                            "email" to (doc.getString("email") ?: "")
                        )
                    )
                }
                adapter.notifyDataSetChanged()
            }
    }
}