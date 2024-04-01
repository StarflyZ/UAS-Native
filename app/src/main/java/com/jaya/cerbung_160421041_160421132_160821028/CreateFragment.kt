package com.jaya.cerbung_160421041_160421132_160821028

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jaya.cerbung_160421041_160421132_160821028.databinding.FragmentCreateBinding
import com.jaya.cerbung_160421041_160421132_160821028.databinding.FragmentHomeBinding
import org.json.JSONObject

class CreateFragment : Fragment() {
    private var user: String? = null
    private var genre = ""
    private lateinit var binding: FragmentCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        if (savedInstanceState != null) {
//            // Restore input values if there is saved state
//            with(binding) {
//                txtInputTitle.setText(savedInstanceState.getString("title") ?: "")
//                txtInputDesc.setText(savedInstanceState.getString("desc") ?: "")
//                txtInputUrl.setText(savedInstanceState.getString("imageUrl") ?: "")
//                val savedGenre = savedInstanceState.getString("genre") ?: ""
//                val index = Global.genre.indexOfFirst { it.name == savedGenre }
//                spinnerGenre.setSelection(index)
//            }
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateBinding.inflate(inflater, container, false)

        val isDarkMode = (requireActivity().application as MyApplication).getDarkModePreference()
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        arguments?.let {
            user = it.getString("user")
            create(user!!)
            Log.d("OnCreate", user.toString())
        }
        return binding.root
    }

    fun create(user: String) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, Global.genre)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGenre.adapter = adapter

        binding.btnNextCreate.setOnClickListener {
            val genre = binding.spinnerGenre.selectedItem.toString()

            val i = Intent(activity, AccessCreateActivity::class.java)
            i.putExtra("user", user)
            i.putExtra("title", binding.txtInputTitle.text.toString())
            i.putExtra("desc", binding.txtInputDesc.text.toString())
            i.putExtra("imageUrl", binding.txtInputUrl.text.toString())
            i.putExtra("genre", genre)
            startActivity(i)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // Save input values and selected genre when the state is saved
        with(binding) {
            outState.putString("title", txtInputTitle.text.toString())
            outState.putString("desc", txtInputDesc.text.toString())
            outState.putString("imageUrl", txtInputUrl.text.toString())
            outState.putString("genre", spinnerGenre.selectedItem.toString())
        }
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            // Restore input values if there is saved state
            with(binding) {
                txtInputTitle.setText(savedInstanceState.getString("title") ?: "")
                txtInputDesc.setText(savedInstanceState.getString("desc") ?: "")
                txtInputUrl.setText(savedInstanceState.getString("imageUrl") ?: "")
                val savedGenre = savedInstanceState.getString("genre") ?: ""
                val index = Global.genre.indexOfFirst { it.name == savedGenre }
                spinnerGenre.setSelection(index)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(user: String) =
            CreateFragment().apply {
                arguments = Bundle().apply {
                    putString("user", user)
                }
            }
    }
}