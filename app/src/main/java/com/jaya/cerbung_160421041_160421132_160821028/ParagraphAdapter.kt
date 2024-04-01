package com.jaya.cerbung_160421041_160421132_160821028

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jaya.cerbung_160421041_160421132_160821028.databinding.ReadItemBinding

class ParagraphAdapter(val paragraphs:ArrayList<Paragraph>, val user: String, val cerbung_id: Int) : RecyclerView.Adapter<ParagraphAdapter.ParagraphViewHolder>(){
    class ParagraphViewHolder(val binding: ReadItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParagraphViewHolder {
        val binding = ReadItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParagraphViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return paragraphs.size
    }

    override fun onBindViewHolder(holder: ParagraphViewHolder, position: Int) {
        val paragraph = paragraphs[position].content
        val author = paragraphs[position].writer

        with(holder.binding) {
            txtParagraphRead.text = paragraph
            txtAuthorRead.text = user

            btnLike.setOnClickListener {
                val q = Volley.newRequestQueue(holder.itemView.context)
                val url_like = "https://ubaya.me/native/160421041/set_likes.php"
                val stringRequest = object: StringRequest(
                    Request.Method.POST, url_like,
                    {
                        Log.d("apiresult", it)
                    },
                    {
                        Log.e("apierror", it.printStackTrace().toString())
                    }
                ) {
                    override fun getParams(): MutableMap<String, String>? {
                        val params = HashMap<String, String>()
                        params["id"] = cerbung_id.toString()
                        return params
                    }
                }
                q.add(stringRequest)
                btnLike.isEnabled = false
            }
        }
    }

}