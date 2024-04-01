package com.jaya.cerbung_160421041_160421132_160821028

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.jaya.cerbung_160421041_160421132_160821028.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var fragments:ArrayList<Fragment> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("username")
        val num_follower = intent.getIntExtra("num_follower", -1)

        fragments.add(HomeFragment.newInstance(username.toString(), num_follower))
        fragments.add(FollowingFragment.newInstance(username.toString()))
        fragments.add(CreateFragment.newInstance(username.toString()))
        fragments.add(UsersFragment())
        fragments.add(SettingFragment.newInstance(username.toString()))

        binding.viewPager.adapter =MyAdapter(this, fragments)

        binding.viewPager.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                binding.bottomNavigationView.selectedItemId = binding.bottomNavigationView.menu.getItem(position).itemId
            }
        })

        binding.bottomNavigationView.setOnItemSelectedListener {
            binding.viewPager.currentItem = when(it.itemId){
                R.id.itemHome -> 0
                R.id.itemFollowing -> 1
                R.id.itemCreate -> 2
                R.id.itemUsers -> 3
                R.id.itemSetting -> 4
                else -> 0
            }
            true
        }

        // Initialize default dark mode setting
        val isDarkMode = (application as MyApplication).getDarkModePreference()
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}