package com.jaya.cerbung_160421041_160421132_160821028

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jaya.cerbung_160421041_160421132_160821028.databinding.ActivityReadRestrictedBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject

class ReadRestrictedActivity : AppCompatActivity() {
    var user:String = ""
    var paragraphs:ArrayList<Paragraph> = ArrayList()
    var cerbung_id:Int = 0
    private lateinit var binding: ActivityReadRestrictedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadRestrictedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cerbungId = intent.getIntExtra("id", -1)
        val title = intent.getStringExtra("title")
        val author = intent.getStringExtra("author")
        val imageUrl = intent.getStringExtra("imageUrl")
        val genre = intent.getStringExtra("genre")
        val likes = intent.getIntExtra("likes", -1)
        val follower = intent.getIntExtra("follower",-1)
        val access = intent.getStringExtra("access")

        user = intent.getStringExtra("user").toString()

        val selectedCerbung = HomeFragment.cerbungs.find { it.id == cerbungId }

        ambilDataParagraf()

        binding.txtTitleRestricted.text = title;
        binding.txtAuthorRestricted.text = author;
        binding.txtGenreRestricted.text = genre;
        binding.txtLikesRestricted.text = likes.toString()
        binding.txtFollowRestricted.text = follower.toString()

        cerbung_id = cerbungId

        val builder = Picasso.Builder(this)
        builder.listener { picasso, uri, exception -> exception.printStackTrace() }
        builder.build().load(imageUrl).into(binding.imgPosterRestricted)

        if (selectedCerbung != null) {
            binding.txtTitleRestricted.text = selectedCerbung.title
            binding.txtAuthorRestricted.text = selectedCerbung.author
            binding.txtGenreRestricted.text = selectedCerbung.genre
            binding.txtLikesRestricted.text = selectedCerbung.likes.toString()
            binding.txtFollowRestricted.text = selectedCerbung.follower.toString()

            val builder = Picasso.Builder(this)
            builder.listener { picasso, uri, exception -> exception.printStackTrace() }
            builder.build().load(selectedCerbung.url).into(binding.imgPosterRestricted)

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
                    binding.btnFollowRestricted.text = "Followed"
                } else if (obj.getString("result") == "ERROR") {
                    binding.btnFollowRestricted.text = "Follow"

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

        binding.btnFollowRestricted.setOnClickListener {
            if (binding.btnFollowRestricted.text === "Follow") {
                val follower = binding.txtFollowRestricted.text.toString().toInt() + 1
                binding.txtFollowRestricted.text = follower.toString()
                binding.btnFollowRestricted.text = "Followed"

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
                val follower = binding.txtFollowRestricted.text.toString().toInt() - 1
                binding.txtFollowRestricted.text = follower.toString()
                binding.btnFollowRestricted.text = "Follow"

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
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["cerbung_id"] = cerbung_id.toString()
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
        binding.recyclerViewRead?.layoutManager = lm
        binding.recyclerViewRead?.adapter = ParagraphAdapter(paragraphs, user, cerbung_id)
    }
}