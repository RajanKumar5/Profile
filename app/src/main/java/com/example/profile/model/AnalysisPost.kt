package com.example.profile.model

data class AnalysisPost (
    val image: String,
    val age: Int,
    val gender: String,
    val emotion_angry: Double,
    val emotion_disgust: Double,
    val emotion_fear: Double,
    val emotion_happy: Double,
    val emotion_sad: Double,
    val emotion_surprise: Double,
    val emotion_neutral: Double,
    val dominant_emotion: String,
    val race_asian: Double,
    val race_indian: Double,
    val race_black: Double,
    val race_white: Double,
    val race_middle_eastern: Double,
    val race_latino_hispanic: Double,
    val dominant_race: String
        )