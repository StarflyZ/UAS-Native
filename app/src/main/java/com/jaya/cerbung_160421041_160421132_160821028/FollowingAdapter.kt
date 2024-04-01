package com.jaya.cerbung_160421041_160421132_160821028

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaya.cerbung_160421041_160421132_160821028.databinding.CerbungFollowItemBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class FollowingAdapter(val cerbungs: ArrayList<Cerbung>): RecyclerView.Adapter<FollowingAdapter.FollowCerbungViewHolder>() {
    class FollowCerbungViewHolder(val binding: CerbungFollowItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowCerbungViewHolder {
        val binding = CerbungFollowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowCerbungViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cerbungs.size
    }

    override fun onBindViewHolder(holder: FollowCerbungViewHolder, position: Int) {
        val url = cerbungs[position].url
        val builder = Picasso.Builder(holder.itemView.context)
        builder.listener { picasso, uri, exception -> exception.printStackTrace() }
        Picasso.get().load(url).into(holder.binding.imgPosterFollow)

        with(holder.binding) {
            txtTitleFollow.text = cerbungs[position].title
            txtAuthorFollow.text = cerbungs[position].author
            txtLastUpdate.text = cerbungs[position].last_updated
        }
    }
}