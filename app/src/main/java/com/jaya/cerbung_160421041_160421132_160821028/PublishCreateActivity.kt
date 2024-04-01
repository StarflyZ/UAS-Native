package com.jaya.cerbung_160421041_160421132_160821028

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jaya.cerbung_160421041_160421132_160821028.databinding.ActivityPublishCreateBinding

class PublishCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPublishCreateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPublishCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val paragraph = intent.getStringExtra("paragraph")
        val user = intent.getStringExtra("user")
        val access = intent.getStringExtra("access")
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("desc")
        val image_url = intent.getStringExtra("imageUrl")
        val genre = intent.getStringExtra("genre")

        binding.txtTitlePublish.text = title.toString()
        binding.txtDescriptionPublish.text = description.toString()
        binding.txtGenrePublish.text = genre.toString()
        binding.txtParagraphPublish.text = paragraph.toString()
        binding.btnPublish.isEnabled = false

        if (access == "Restricted")
        {
            binding.txtAccessPublish.text = "Restricted"
        } else if (access == "Public")
        {
            binding.txtAccessPublish.text = "Public"
        }

        binding.checkBoxRules.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.btnPublish.isEnabled = isChecked
        }


        binding.btnPublish.setOnClickListener {
            val q = Volley.newRequestQueue(this)
            val url = "https://ubaya.me/native/160421041/add_cerbung.php"
            val stringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener {
                    Log.d("cekparams", it)
                    Toast.makeText(this, "Cerbung berhasil ditambahkan.", Toast.LENGTH_SHORT).show()
                },
                Response.ErrorListener {
                    Log.d("cekparams", it.message.toString())
                }

            ) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["title"] = title.toString()
                    params["author"] = user.toString()
                    params["description"] = description.toString()
                    params["url"] = image_url.toString()
                    params["genre"] = genre.toString()
                    params["access"] = access.toString()
                    params["paragraph"] = paragraph.toString()

                    return params
                }
            }

            q.add(stringRequest)

            Log.d("cekparams", "Title: " + title.toString())
            Log.d("cekparams", "Author: " + user.toString())

            val i = Intent(this, MainActivity::class.java)
            this.finish()
            startActivity(i)
        }

        binding.btnPrevAccess.setOnClickListener {
            val intent = Intent(this, AccessCreateActivity::class.java)
            intent.putExtra("title",title)
            intent.putExtra("desc",description)
            intent.putExtra("imageUrl",image_url)
            intent.putExtra("genre",genre)
            intent.putExtra("access",access)
            intent.putExtra("paragraph",paragraph)
            intent.putExtra("user",user)
            startActivity(intent)
        }
    }
}