package com.example.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.profile.databinding.ActivityEmotionDetailBinding

class EmotionDetail : AppCompatActivity() {

    private lateinit var binding: ActivityEmotionDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEmotionDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val map: HashMap<String, Any?> = intent.getSerializableExtra("responseData") as HashMap<String, Any?>
        binding.tvEmotionAngry.text = map["emotion_angry"].toString() + "%"
        binding.tvEmotionDisgust.text = map["emotion_disgust"].toString() + "%"
        binding.tvEmotionFear.text = map["emotion_fear"].toString() + "%"
        binding.tvEmotionHappy.text = map["emotion_happy"].toString() + "%"
        binding.tvEmotionSad.text = map["emotion_sad"].toString() + "%"
        binding.tvEmotionSurprise.text = map["emotion_surprise"].toString() + "%"
        binding.tvEmotionNeutral.text = map["emotion_neutral"].toString() + "%"
    }

}