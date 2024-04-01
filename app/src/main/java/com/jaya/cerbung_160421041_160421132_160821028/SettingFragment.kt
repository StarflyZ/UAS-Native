package com.jaya.cerbung_160421041_160421132_160821028

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jaya.cerbung_160421041_160421132_160821028.databinding.FragmentSettingBinding
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private var user: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        arguments?.let {
            user = it.getString("user") ?: ""
//            Log.d("OnCreateView", user.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtUsername.setText(user.toString())
        if (!user.isNullOrBlank()) {
            binding.txtUsername.setText(user)
            binding.btnSave.setOnClickListener {
                val oldPass = binding.txtOldPass.text.toString()
                val newPass = binding.txtNewPass.text.toString()
                val rePass = binding.txtRePass.text.toString()

                if (newPass == rePass) {
                    changePassword(user!!, oldPass, newPass)
                } else {
                    Toast.makeText(activity, "Password must be the same", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Handle the case where username is null or blank
            Toast.makeText(activity, "Username is not available", Toast.LENGTH_SHORT).show()
        }

        binding.fabLogOut.setOnClickListener {
            logout()
        }
        binding.btnSave.setOnClickListener {
            val oldPass = binding.txtOldPass.text.toString()
            val newPass = binding.txtNewPass.text.toString()
            val rePass = binding.txtRePass.text.toString()

            if (newPass == rePass) {
                changePassword(user!!, oldPass, newPass)
            } else {
                Toast.makeText(activity, "Password must be the same", Toast.LENGTH_SHORT).show()
            }
        }
        binding.darkModeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            // Save dark mode preference in MyApplication
            (requireActivity().application as MyApplication).saveDarkModePreference(isChecked)
            // Set dark mode
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun logout() {
        val sharedPreferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun changePassword(user: String, oldPassword: String, newPassword: String) {
        val url = "https://ubaya.me/native/160421041/change_pass.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                Log.d("apiresult", response)
                try {
                    val obj = JSONObject(response)
                    if (obj.getString("result") == "OK") {
                        Toast.makeText(activity, "Password changed successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, "Failed to change password: ${obj.getString("message")}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, "Error parsing response", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Log.e("apiresult", error.message.toString())
                Toast.makeText(activity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["username"] = user
                params["oldPass"] = oldPassword
                params["newPass"] = newPassword
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(requireActivity())
        requestQueue.add(stringRequest)
        binding.txtOldPass.text?.clear()
        binding.txtNewPass.text?.clear()
        binding.txtRePass.text?.clear()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
        Toast.makeText(activity,"Please Login again",Toast.LENGTH_SHORT).show()
    }

    companion object {
        @JvmStatic
        fun newInstance(user: String) =
            SettingFragment().apply {
                arguments = Bundle().apply {
                    putString("user", user)
                }
            }
    }
}