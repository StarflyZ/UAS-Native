package com.jaya.cerbung_160421041_160421132_160821028

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jaya.cerbung_160421041_160421132_160821028.databinding.ActivityReadPublicBinding
import com.jaya.cerbung_160421041_160421132_160821028.databinding.ReadItemBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject

class ReadPublicActivity : AppCompatActivity() {
    var user:String = ""
    var paragraphs:ArrayList<Paragraph> = ArrayList()
    var cerbung_id:Int = 0
    private lateinit var binding: ActivityReadPublicBinding
//    private lateinit var binding1: ReadItemBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadPublicBinding.inflate(layoutInflater)
//        binding1 = ReadItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cerbungId = intent.getIntExtra("id", -1)
        val title = intent.getStringExtra("title")
        val author = intent.getStringExtra("author")
        val imageUrl = intent.getStringExtra("imageUrl")
        val genre = intent.getStringExtra("genre")
        val likes = intent.getIntExtra("likes", -1)
        val follower = intent.getIntExtra("follower",-1)
//        val num_follower_user = intent.getIntExtra("num_follower_user",-1)
//        val access = intent.getStringExtra("access")

        user = intent.getStringExtra("user").toString()

//        Log.d("CerbungAdapter", "user: $user, num_follower: $num_follower_user")

        val selectedCerbung = HomeFragment.cerbungs.find { it.id == cerbungId }

        ambilDataParagraf()

        binding.txtTitlePublic.text = title
        binding.txtAuthorPublic.text = author
        binding.txtGenrePublic.text = genre
        binding.txtLikesPublic.text = likes.toString()
        binding.txtFollowPublic.text = follower.toString()

        cerbung_id = cerbungId

        val builder = Picasso.Builder(this)
        builder.listener { picasso, uri, exception -> exception.printStackTrace() }
        builder.build().load(imageUrl).into(binding.imgPosterPublic)

        if (selectedCerbung != null) {
            binding.txtTitlePublic.text = selectedCerbung.title
            binding.txtAuthorPublic.text = selectedCerbung.author
            binding.txtGenrePublic.text = selectedCerbung.genre
            binding.txtLikesPublic.text = selectedCerbung.likes.toString()
            binding.txtFollowPublic.text = selectedCerbung.follower.toString()

            val builder = Picasso.Builder(this)
            builder.listener { picasso, uri, exception -> exception.printStackTrace() }
            builder.build().load(selectedCerbung.url).into(binding.imgPosterPublic)

            val lm = LinearLayoutManager(this)
            binding.recyclerViewRead.layoutManager = lm

            binding.recyclerViewRead.adapter = ParagraphAdapter(paragraphs, user, cerbung_id)
        }

        binding.btnSubmitPublic.setOnClickListener {
            val q = Volley.newRequestQueue(this)
            val url = "https://ubaya.me/native/160421041/add_paragraph.php"
            val stringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener {
                    Log.d("cekparams", it)
                },
                Response.ErrorListener {
                    Log.d("cekparams", it.message.toString())
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["new_paragraph"] = binding.txtAddParagraph.text.toString()
                    params["new_writer"] = user.toString()
                    params["cerbung_id"] = cerbungId.toString()
                    Log.e("cerbungid", "cerbung_id: $cerbung_id")
                    return params
                }
            }

            q.add(stringRequest)

            Toast.makeText(this, "Paragraf baru berhasil ditambahkan.", Toast.LENGTH_SHORT).show()

            binding.txtAddParagraph.text?.clear()

            updateList()
        }

        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.me/native/160421041/get_followed_cerbung.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            {
                Log.d("apiresult", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    binding.btnFollowPublic.text = "Followed"
                } else if (obj.getString("result") == "ERROR") {
                    binding.btnFollowPublic.text = "Follow"

                }
            },
            {
                Log.e("apiresult", it.printStackTrace().toString())
            }
        ) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["user"] = user
                params["cerbung_id"] = cerbung_id.toString()
                return params
            }
        }
        q.add(stringRequest)

        binding.btnFollowPublic.setOnClickListener {
            if (binding.btnFollowPublic.text === "Follow") {
                val follower = binding.txtFollowPublic.text.toString().toInt() + 1
                binding.txtFollowPublic.text = follower.toString()
                binding.btnFollowPublic.text = "Followed"

                val q = Volley.newRequestQueue(this)
                val url = "https://ubaya.me/native/160421041/set_follower.php"
                val stringRequest = object : StringRequest(
                    Request.Method.POST, url,
                    Response.Listener {
                        Log.d("cekparams", it)
                    },
                    Response.ErrorListener {
                        Log.d("cekparams", it.message.toString())
                    }
                ) {
                    override fun getParams(): MutableMap<String, String> {
                        val params = HashMap<String, String>()
                        params["id"] = cerbung_id.toString()
                        return params
                    }
                }

                q.add(stringRequest)

                addFollowing(user, cerbung_id)
            } else {
                val follower = binding.txtFollowPublic.text.toString().toInt() - 1
                binding.txtFollowPublic.text = follower.toString()
                binding.btnFollowPublic.text = "Follow"

                val q = Volley.newRequestQueue(this)
                val url = "https://ubaya.me/native/160421041/set_follower_unfol.php"
                val stringRequest = object : StringRequest(
                    Request.Method.POST, url,
                    Response.Listener {
                        Log.d("cekparams", it)
                    },
                    Response.ErrorListener {
                        Log.d("cekparams", it.message.toString())
                    }
                ) {
                    override fun getParams(): MutableMap<String, String> {
                        val params = HashMap<String, String>()
                        params["id"] = cerbung_id.toString()
                        return params
                    }
                }

                q.add(stringRequest)

                minFollowing(user, cerbung_id)
            }
        }

//        binding1.btnLike.setOnClickListener {
//            val currentLikes = binding.txtLikesPublic.text.toString().toInt()
//            val newLikes = currentLikes + 1
//            binding.txtLikesPublic.text = newLikes.toString()
//        }
    }

    fun ambilDataParagraf() {
        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.me/native/160421041/get_paragraphs.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST, url, {
                Log.d("apiresult", it.toString())
                val obj = JSONObject(it)
                if(obj.getString("result") == "OK"){
                    val data = obj.getJSONArray("data")
                    val sType = object : TypeToken<List<Paragraph>>(){ }.type
                    paragraphs = Gson().fromJson(data.toString(), sType) as ArrayList<Paragraph>
                    if(obj.getString("result") == "OK") {
                        val data = obj.getJSONArray("data")
                        val sType = object : TypeToken<List<Paragraph>>() { }.type
                        paragraphs = Gson().fromJson(data.toString(), sType) as ArrayList<Paragraph>
                        Log.d("apiresult", paragraphs.toString())

                        updateList()
                    }
                }
            },
            {
                Log.e("apiresult", it.message.toString())
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["cerbung_id"] = cerbung_id.toString()
                Log.e("cerbungid", "cerbung_id: $cerbung_id")
                return params
            }
        }
        q.add(stringRequest)
    }

    fun addFollowing(user:String, cerbung_id:Int) {
        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.me/native/160421041/add_following.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener {
                Log.d("cekparams", it)
            },
            Response.ErrorListener {
                Log.d("cekparams", it.message.toString())
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["user"] = user
                params["cerbung_id"] = cerbung_id.toString()
                return params
            }
        }

        q.add(stringRequest)
    }

    fun minFollowing(user:String, cerbung_id:Int) {
        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.me/native/160421041/min_following.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener {
                Log.d("cekparams", it)
            },
            Response.ErrorListener {
                Log.d("cekparams", it.message.toString())
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["user"] = user
                params["cerbung_id"] = cerbung_id.toString()
                return params
            }
        }

        q.add(stringRequest)
    }

    fun updateList() {
        val lm = LinearLayoutManager(this)
        binding.recyclerViewRead.layoutManager = lm
        binding.recyclerViewRead.adapter = ParagraphAdapter(paragraphs, user,cerbung_id)
    }
}