package com.jaya.cerbung_160421041_160421132_160821028

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jaya.cerbung_160421041_160421132_160821028.databinding.FragmentFollowingBinding
import com.jaya.cerbung_160421041_160421132_160821028.databinding.FragmentHomeBinding
import org.json.JSONObject


class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowingBinding
    var cerbungs: ArrayList<Cerbung> = arrayListOf()
    private var user:String? = null

    fun updateList() {
        val lm = LinearLayoutManager(activity)
        with (binding.followingRecView) {
            layoutManager = lm
            setHasFixedSize(true)
            adapter = FollowingAdapter(cerbungs)
        }
    }

    fun ambilDataFollowing(user: String) {
        val q = Volley.newRequestQueue(activity)
        val url = "https://ubaya.me/native/160421041/get_following_cerbung.php"
        val stringRequest = object : StringRequest(Request.Method.POST, url,
            {
                Log.d("apiresult", it.toString())
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")
                    val sType = object : TypeToken<List<Cerbung>>() { }.type
                    cerbungs = Gson().fromJson(data.toString(), sType) as ArrayList<Cerbung>
                    updateList()
                }
            },
            {
                Log.e("apiresult", it.message.toString())
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["user"] = user
                return params
            }
        }
        q.add(stringRequest)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getString("user") ?: ""
            ambilDataFollowing(user!!)
            Log.d("OnCreateView", user.toString())
        }

    }

    override fun onResume() {
        super.onResume()
        arguments?.let {
            user = it.getString("user") ?: ""
            ambilDataFollowing(user!!)
            Log.d("OnCreateView", user.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)

        // Set dark mode based on preference
        val isDarkMode = (requireActivity().application as MyApplication).getDarkModePreference()
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(user: String) =
            FollowingFragment().apply {
                arguments = Bundle().apply {
                    putString("user", user)
                }
            }
    }
}