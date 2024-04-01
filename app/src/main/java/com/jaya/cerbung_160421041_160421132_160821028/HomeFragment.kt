package com.jaya.cerbung_160421041_160421132_160821028

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.content.Intent
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
import com.jaya.cerbung_160421041_160421132_160821028.databinding.FragmentHomeBinding
import org.json.JSONObject

class HomeFragment : Fragment() {
    private var user: String? = null
    private var num_follower: Int? = null
    var cerbungs:ArrayList<Cerbung> = ArrayList()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val q = Volley.newRequestQueue(activity)
        var url = "https://ubaya.me/native/160421041/get_cerbung.php"
        var stringRequest = StringRequest(
            Request.Method.POST, url, {
                Log.d("apiresult", it.toString())
                val obj = JSONObject(it)
                if(obj.getString("result") == "OK"){
                    val data = obj.getJSONArray("data")
                    val sType = object : TypeToken<List<Cerbung>>(){ }.type
                    cerbungs = Gson().fromJson(data.toString(), sType) as ArrayList<Cerbung>
                    if(obj.getString("result") == "OK") {
                        val data = obj.getJSONArray("data")
                        val sType = object : TypeToken<List<Cerbung>>() { }.type
                        cerbungs = Gson().fromJson(data.toString(), sType) as ArrayList<Cerbung>
                        Log.d("apiresult", cerbungs.toString())

                        updateList()
                    }
                }
            },
            {
                Log.e("apiresult", it.message.toString())
            }
        )
        q.add(stringRequest)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Set dark mode based on preference
        val isDarkMode = (requireActivity().application as MyApplication).getDarkModePreference()
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        arguments?.let {
            user = it.getString("user")
            num_follower = it.getInt("num_follower")
        }

        return binding.root
//        return inflater.inflate(R.layout.fragment_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun updateList() {
        binding.cerbungRecView.layoutManager = LinearLayoutManager(activity)
        binding.cerbungRecView.setHasFixedSize(true)
        binding.cerbungRecView.adapter = CerbungAdapter(cerbungs, user.toString(), num_follower)
    }

    companion object {
        @JvmStatic
        fun newInstance(user: String, num_follower: Int) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString("user", user)
                    putInt("num_follower", num_follower)
                }
            }
        var cerbungs: ArrayList<Cerbung> = ArrayList()
    }
}