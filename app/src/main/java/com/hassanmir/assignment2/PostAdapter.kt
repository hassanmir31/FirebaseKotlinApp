package com.hassanmir.assignment2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(private val posts: List<Map<String, String>>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPostText: TextView = view.findViewById(R.id.tvPostText)
        val tvPostEmail: TextView = view.findViewById(R.id.tvPostEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.tvPostText.text = posts[position]["text"]
        holder.tvPostEmail.text = posts[position]["email"]
    }

    override fun getItemCount() = posts.size
}