package com.jaya.cerbung_160421041_160421132_160821028

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jaya.cerbung_160421041_160421132_160821028.databinding.ActivityLoginBinding
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    companion object {
        val KEY_USERNAME = "username"
        val KEY_NUM_FOLLOWER = "num_follower"
    }

    fun cekLogin(username:String, password:String) {
        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.me/native/160421041/get_user.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            {
                Log.d("apiresult", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")
                    if (data.length() > 0) {
                        val dataUser = data.getJSONObject(0)
                        val sType = object : TypeToken<User>() { }.type
                        val user = Gson().fromJson(dataUser.toString(), sType) as User
                        Toast.makeText(this,"Welcome ${user.username}", Toast.LENGTH_SHORT).show()

                        val i = Intent(this, MainActivity::class.java)
                        i.putExtra(KEY_USERNAME, user.username)
                        i.putExtra(KEY_NUM_FOLLOWER, user.num_follower)
                        finishAffinity()
                        startActivity(i)
                    } else {
                        Toast.makeText(this, "Username or Password is incorrect", Toast.LENGTH_LONG).show()
                    }
                }
            },
            {
                Log.e("apiresult", it.printStackTrace().toString())
            }
        ) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["username"] = username
                params["password"] = password
                return params
            }
        }
        q.add(stringRequest)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val username = binding.txtUser.text.toString()
            val password = binding.txtPass.text.toString()
            Log.d("apiresult", "Username: $username")
            Log.d("apiresult", "Password: $password")
            cekLogin(username, password)
        }
    }
}