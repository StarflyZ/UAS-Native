package com.jaya.cerbung_160421041_160421132_160821028

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jaya.cerbung_160421041_160421132_160821028.databinding.ActivityRegisterBinding
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegisterReg.setOnClickListener {
            val username = binding.txtUser.text.toString()
            val email = binding.txtEmail.text.toString()
            val password = binding.txtPassword.text.toString()
            val regPass = binding.txtRePassword.text.toString()
            register(username, email, password, regPass)
        }

        binding.btnLoginReg.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun register(username:String, email:String, password:String, regPass:String) {
        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.me/native/160421041/get_user.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            {
                Log.d("apiresult", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "ERROR") {
                    if (password == regPass) {
                        val regUrl = "https://ubaya.me/native/160421041/register.php"
                        val regStrRequest = object : StringRequest(
                            Request.Method.POST, regUrl,
                            {
                                Toast.makeText(this, "Register Succeed", Toast.LENGTH_SHORT).show()
                            },
                            {
                                Log.e("apiresult", it.printStackTrace().toString())
                                Toast.makeText(this, "Register Failed", Toast.LENGTH_LONG).show()
                            }
                        ) {
                            override fun getParams(): MutableMap<String, String>? {
                                val params = HashMap<String, String>()
                                params["username"] = username
                                params["email"] = email
                                params["password"] = password
                                return params
                            }
                        }
                        q.add(regStrRequest)
                        with(binding){
                            txtUser.text.clear()
                            txtEmail.text.clear()
                            txtPassword.text.clear()
                            txtRePassword.text.clear()
                        }
                    } else {
                        Toast.makeText(this, "Password doesn't match", Toast.LENGTH_LONG).show()
                    }
                } else if (obj.getString("result") == "OK") {
                    Toast.makeText(this, "Username already exists", Toast.LENGTH_LONG).show()
                }
            },
            {
                Log.e("apiresult", it.printStackTrace().toString())
            }
        ) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["username"] = username
                return params
            }
        }
        q.add(stringRequest)
    }
}