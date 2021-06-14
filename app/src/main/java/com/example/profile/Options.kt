package com.example.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.profile.databinding.ActivityOptionsBinding

class Options : AppCompatActivity() {

    private lateinit var binding: ActivityOptionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityOptionsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, SelectPicture.PERMISSION_CODE)
            }
        }

        binding.btnImageAnalysis.setOnClickListener {
            val intent = Intent(this, SelectPicture::class.java)
            startActivity(intent)
        }

        binding.btnImageVerification.setOnClickListener {
            val intent = Intent(this, Recognition::class.java)
            startActivity(intent)
        }
    }
}