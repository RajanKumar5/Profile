package com.example.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.profile.databinding.ActivityAnalysisDataBinding
import java.util.*
import kotlin.collections.HashMap

class AnalysisData : AppCompatActivity() {

    private lateinit var bindind: ActivityAnalysisDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        bindind = ActivityAnalysisDataBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(bindind.root)

        val map: HashMap<String, Any?> = intent.getSerializableExtra("responseData") as HashMap<String, Any?>

        bindind.tvAge.text = map["age"].toString()
        bindind.tvGender.text = map["gender"].toString().uppercase(Locale.getDefault())
        bindind.tvEmotion.text = map["dominant_emotion"].toString().uppercase(Locale.getDefault())
        bindind.tvRace.text = map["dominant_race"].toString().uppercase(Locale.getDefault())

        bindind.tvOnClickEmotion.setOnClickListener{
            val intent = Intent(this, EmotionDetail::class.java)
            intent.putExtra("responseData", map)
            startActivity(intent)
        }

        bindind.tvOnClickRace.setOnClickListener {
            val intent = Intent(this, RaceDetail::class.java)
            intent.putExtra("responseData", map)
            startActivity(intent)
        }

    }
}