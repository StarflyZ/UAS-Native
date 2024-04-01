package com.jaya.cerbung_160421041_160421132_160821028

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.jaya.cerbung_160421041_160421132_160821028.databinding.CerbungItemBinding
import com.squareup.picasso.Picasso

class CerbungAdapter(val cerbungs:ArrayList<Cerbung>, val user: String, val num_follower: Int?): RecyclerView.Adapter<CerbungAdapter.CerbungViewHolder>(){
    class CerbungViewHolder(val binding: CerbungItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CerbungViewHolder {
        val binding = CerbungItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CerbungViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cerbungs.size
    }

    override fun onBindViewHolder(holder: CerbungViewHolder, position: Int) {
        val url = cerbungs[position].url
        val builder = Picasso.Builder(holder.itemView.context)
        builder.listener { picasso, uri, exception -> exception.printStackTrace() }
        Picasso.get().load(url).into(holder.binding.imgPoster)

        with(holder.binding) {
            txtTitle.text = cerbungs[position].title
            txtAuthor.text = cerbungs[position].author
            txtParagraph.text = cerbungs[position].description
            txtLikes.text = cerbungs[position].likes.toString() + " Likes"
            txtFollow.text = cerbungs[position].follower.toString()

            btnRead.setOnClickListener {
                val cerbung = cerbungs[position]
                val intent = Intent(holder.itemView.context, if (cerbung.access == "Restricted") ReadRestrictedActivity::class.java else ReadPublicActivity::class.java)

                intent.putExtra("id", cerbung.id)
                intent.putExtra("title", cerbung.title)
                intent.putExtra("author", cerbung.author)
                intent.putExtra("imageUrl", cerbung.url)
                intent.putExtra("genre", cerbung.genre)
                intent.putExtra("access", cerbung.access)
                intent.putExtra("likes", cerbung.likes)
                intent.putExtra("follower", cerbung.follower)
                intent.putExtra("user", user)
                intent.putExtra("num_follower_user", num_follower)

                holder.itemView.context.startActivity(intent)
            }
        }
    }
}