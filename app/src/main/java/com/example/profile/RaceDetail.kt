package com.example.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.profile.databinding.ActivityRaceDetailBinding

class RaceDetail : AppCompatActivity() {

    private lateinit var binding: ActivityRaceDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRaceDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val map: HashMap<String, Any?> = intent.getSerializableExtra("responseData") as HashMap<String, Any?>
        binding.tvRaceAsian.text = map["race_asian"].toString() + "%"
        binding.tvRaceIndian.text = map["race_indian"].toString() + "%"
        binding.tvRaceBlack.text = map["race_black"].toString() + "%"
        binding.tvRaceWhite.text = map["race_white"].toString() + "%"
        binding.tvRaceMiddleEastern.text = map["race_middle_eastern"].toString() + "%"
        binding.tvRaceLatinoHispanic.text = map["race_latino_hispanic"].toString() + "%"
    }
}