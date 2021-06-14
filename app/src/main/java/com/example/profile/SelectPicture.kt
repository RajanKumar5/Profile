package com.example.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.profile.databinding.ActivitySelectPictureBinding
import com.example.profile.model.AnalysisPost
import kotlinx.coroutines.flow.combineTransform
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit


class SelectPicture : AppCompatActivity() {

    companion object {
        const val PERMISSION_CODE = 1001
    }

    private lateinit var binding: ActivitySelectPictureBinding
    private lateinit var filePath: Uri
    private lateinit var resized: Bitmap
    private var encImage: String = ""
    private lateinit var buttonText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySelectPictureBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.analyseProgressBar.visibility = View.INVISIBLE
        binding.tvProcessingData.visibility = View.INVISIBLE

        binding.btnUploadImage.setOnClickListener { view ->

            handleButtonClick(view)
            if (buttonText == "REMOVE") {
                binding.ivSelectedImage.setImageResource(R.drawable.image_upload)
                encImage = ""
                binding.btnUploadImage.text = "UPLOAD"
            } else {
                startFileChooser()
            }
        }


        binding.btnAnalyseImage.setOnClickListener {
            if(encImage != ""){

                binding.analyseProgressBar.visibility = View.VISIBLE
                binding.tvProcessingData.visibility = View.VISIBLE
                binding.btnUploadImage.visibility = View.INVISIBLE
                binding.btnAnalyseImage.visibility = View.INVISIBLE

                //RETROFIT BUILDER

                var okHttpClient: OkHttpClient? = OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build()

                val retrofitBuilder = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .baseUrl("http://13.232.59.171:8000")
                    .build()


                // SEND DATA

                val analysisAPI = retrofitBuilder.create(AnalysisAPi::class.java)

                val analysisPost = AnalysisPost(
                    encImage, 0, "", 0.0, 0.0,
                    0.0, 0.0, 0.0, 0.0,
                    0.0, "", 0.0, 0.0, 0.0,
                    0.0, 0.0, 0.0, ""
                )

                val call = analysisAPI.analyseData(analysisPost)

                call.enqueue(object : Callback<AnalysisPost> {
                    override fun onResponse(
                        call: Call<AnalysisPost>,
                        response: Response<AnalysisPost>
                    ) {
                        println("Response Code: ${response.code()}")
                        println("Response Body: ${response.body()}")
                        if (response.code() == 500) {
                            Toast.makeText(
                                this@SelectPicture,
                                "Face could not be detected",
                                Toast.LENGTH_LONG
                            ).show()
                            binding.analyseProgressBar.visibility = View.INVISIBLE
                            binding.tvProcessingData.visibility = View.INVISIBLE
                            binding.btnUploadImage.visibility = View.VISIBLE
                            binding.btnAnalyseImage.visibility = View.VISIBLE
                            binding.btnUploadImage.text = "UPLOAD"
                            encImage = ""
                            binding.ivSelectedImage.setImageResource(R.drawable.image_upload)
                        }
                        else{

                            println(response.body())

                            binding.analyseProgressBar.visibility = View.INVISIBLE
                            binding.tvProcessingData.visibility = View.INVISIBLE
                            binding.btnUploadImage.visibility = View.VISIBLE
                            binding.btnAnalyseImage.visibility = View.VISIBLE

                            var age = response.body()?.age
                            var gender = response.body()?.gender
                            var emotion_angry = response.body()?.emotion_angry
                            var emotion_disgust = response.body()?.emotion_disgust
                            var emotion_fear = response.body()?.emotion_fear
                            var emotion_happy = response.body()?.emotion_happy
                            var emotion_sad = response.body()?.emotion_sad
                            var emotion_surprise = response.body()?.emotion_surprise
                            var emotion_neutral = response.body()?.emotion_neutral
                            var dominant_emotion = response.body()?.dominant_emotion
                            var race_asian = response.body()?.race_asian
                            var race_indian = response.body()?.race_indian
                            var race_black = response.body()?.race_black
                            var race_white = response.body()?.race_white
                            var race_middle_eastern = response.body()?.race_middle_eastern
                            var race_latino_hispanic = response.body()?.race_latino_hispanic
                            var dominant_race = response.body()?.dominant_race


                            val intent = Intent(this@SelectPicture, AnalysisData::class.java)
                            val map = HashMap<String, Any?>()

                            map["age"] = age
                            map["gender"] = gender
                            map["emotion_angry"] = emotion_angry
                            map["emotion_disgust"] = emotion_disgust
                            map["emotion_fear"] = emotion_fear
                            map["emotion_happy"] = emotion_happy
                            map["emotion_sad"] = emotion_sad
                            map["emotion_surprise"] = emotion_surprise
                            map["emotion_neutral"] = emotion_neutral
                            map["dominant_emotion"] = dominant_emotion
                            map["race_asian"] = race_asian
                            map["race_indian"] = race_indian
                            map["race_black"] = race_black
                            map["race_white"] = race_white
                            map["race_middle_eastern"] = race_middle_eastern
                            map["race_latino_hispanic"] = race_latino_hispanic
                            map["dominant_race"] = dominant_race

                            intent.putExtra("responseData", map)
                            startActivity(intent)

                        }
                    }

                    override fun onFailure(call: Call<AnalysisPost>, t: Throwable) {
                        println("Error Message: ${t.message.toString()}")
                        Toast.makeText(
                            this@SelectPicture,
                            "Something went wrong..Please check your Internet connection",
                            Toast.LENGTH_LONG
                        ).show()
                        binding.analyseProgressBar.visibility = View.INVISIBLE
                        binding.tvProcessingData.visibility = View.INVISIBLE
                        binding.btnUploadImage.visibility = View.VISIBLE
                        binding.btnAnalyseImage.visibility = View.VISIBLE
                        binding.btnUploadImage.text = "UPLOAD"
                        encImage = ""
                        binding.ivSelectedImage.setImageResource(R.drawable.image_upload)
                    }

                })
            }

            else{
                Toast.makeText(this, "Please upload an image", Toast.LENGTH_LONG).show()
            }


        }
    }

    private fun startFileChooser() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 111)
    }

    //    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == Activity.RESULT_OK && data != null) {
            filePath = data.data!!
            //println("File Path is: $filePath")
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
            val width = bitmap.width
            val height = bitmap.height
            if (width == height) {
                resized = Bitmap.createScaledBitmap(bitmap, 210, 210, true)
            }
            if (width > height) {
                resized = Bitmap.createScaledBitmap(bitmap, 250, 170, true)
            }
            if (width < height) {
                resized = Bitmap.createScaledBitmap(bitmap, 170, 250, true)
            }
            //val resized = Bitmap.createScaledBitmap(bitmap, 200, 200, true)
            //binding.ivSelectedImage.setImageBitmap(compressBitmap(resized, 1))
            binding.ivSelectedImage.setImageBitmap(bitmap)
            binding.btnUploadImage.text = "REMOVE"

            resized.apply {

                // show base64 string of bitmap
                val text = toBase64String()
                encImage = text
                //println("Base 64 image: $text")
            }


            /*val uriPathHelper = URIPathHelper()
            val actualPath = uriPathHelper.getPath(this, filePath)

            println("New File Path: $actualPath")*/

            /*val base64 = actualPath?.let { encodeImage(it) }
            println("BASE64: $base64")*/

            //encodeImage(actualPath!!)

        }
    }


    private fun Bitmap.toBase64String(): String {
        ByteArrayOutputStream().apply {
            compress(Bitmap.CompressFormat.JPEG, 10, this)
            return Base64.encodeToString(toByteArray(), Base64.NO_WRAP)
        }
    }

    fun handleButtonClick(view: View) {
        with(view as Button) {
            buttonText = text.toString()
        }
    }

    /*@RequiresApi(Build.VERSION_CODES.O)
    private fun encodeImage(filePath: String) {
        println(filePath)
        val bytes = File(filePath).readBytes()
        val base64 = Base64.getEncoder().encodeToString(bytes)
        *//*return Base64.getEncoder().encodeToString(bytes)*//*
        println(base64)
    }*/

    /*private fun compressBitmap(bitmap: Bitmap, quality: Int): Bitmap {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)

        val byteArray = stream.toByteArray()

        // Finally, return the compressed bitmap
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }*/


}