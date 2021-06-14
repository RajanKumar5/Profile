package com.example.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.profile.databinding.ActivityMainBinding
import com.example.profile.databinding.ActivitySelectPictureBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Glide.with(this).load(R.drawable.animated).into(binding.ivLogo)

        val r = Runnable {
            val intent = Intent(this, Options::class.java)
            startActivity(intent)
            finish()
        }

        Handler().postDelayed(r, 6000)

    }


}