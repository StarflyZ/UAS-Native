package com.jaya.cerbung_160421041_160421132_160821028

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.RadioButton
import com.jaya.cerbung_160421041_160421132_160821028.databinding.ActivityAccessCreateBinding
import com.jaya.cerbung_160421041_160421132_160821028.databinding.FragmentHomeBinding

class AccessCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccessCreateBinding
    private var selectedAccess = ""
    private var paragraph: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccessCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            paragraph = savedInstanceState.getString("paragraph") ?: ""
            binding.txtParagraphCreate.setText(paragraph)

            val checkedRadio = findViewById<RadioButton>(binding.groupAccess.checkedRadioButtonId)
            checkedRadio.text = intent.getStringExtra("access").toString()
        } else {
            // Jika tidak, gunakan nilai dari intent
            paragraph = intent.getStringExtra("paragraph")
            binding.txtParagraphCreate.setText(paragraph)
        }

        binding.groupAccess.setOnCheckedChangeListener { radioGroup, id ->
            /*val selectedRadio = findViewById<RadioButton>(id)
            selectedRadio.setTextColor(Color.RED)*/

            val radio = findViewById<RadioButton>(binding.groupAccess.checkedRadioButtonId)
            val tag = radio.tag.toString()
            Log.d("radiorating", tag)
            selectedAccess = radio.text.toString()
        }

        binding.btnNextPublish.setOnClickListener {
            paragraph = binding.txtParagraphCreate.text.toString()
            val user = intent.getStringExtra("user")
            val title = intent.getStringExtra("title")
            val description = intent.getStringExtra("desc")
            val url = intent.getStringExtra("imageUrl")
            val genre = intent.getStringExtra("genre")

            val intent = Intent(this, PublishCreateActivity::class.java)
            intent.putExtra("title",title)
            intent.putExtra("desc",description)
            intent.putExtra("imageUrl",url)
            intent.putExtra("genre",genre)
            intent.putExtra("paragraph", paragraph)
            intent.putExtra("user", user)
            intent.putExtra("access", selectedAccess)

            startActivity(intent)
        }
        binding.btnPrevCreate.setOnClickListener {
            val fragmentManager = supportFragmentManager
            fragmentManager.popBackStack()
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}