package com.example.profile

import com.example.profile.model.AnalysisPost
import com.example.profile.model.RecognitionPost
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RecognitionAPI {
    @Headers("content-type:application/json")
    @POST("recognition")
    fun recogniseData(
        @Body recognisePost: RecognitionPost
    ): Call<RecognitionPost>
}