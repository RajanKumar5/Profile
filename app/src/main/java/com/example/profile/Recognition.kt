package com.example.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.profile.databinding.ActivityRecognitionBinding
import com.example.profile.model.AnalysisPost
import com.example.profile.model.RecognitionPost
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

class Recognition : AppCompatActivity() {

    private lateinit var binding: ActivityRecognitionBinding
    private lateinit var filePath: Uri
    private lateinit var resized: Bitmap
    private var encImage1: String = ""
    private var encImage2: String = ""
    private lateinit var buttonText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRecognitionBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.progressBar.visibility = View.INVISIBLE
        binding.tvProcessing.visibility = View.INVISIBLE

        binding.btnUploadImage1.setOnClickListener { view ->
            handleButtonClick(view)
            if (buttonText == "REMOVE") {
                binding.ivFirstImage.setImageResource(R.drawable.image_upload)
                encImage1 = ""
                binding.btnUploadImage1.text = "UPLOAD IMAGE 1"
            } else {
                startFileChooser()
            }
        }

        binding.btnUploadImage2.setOnClickListener { view ->
            handleButtonClick(view)
            if (buttonText == "REMOVE") {
                binding.ivSecondImage.setImageResource(R.drawable.image_upload)
                encImage2 = ""
                binding.btnUploadImage2.text = "UPLOAD IMAGE 2"
            } else {
                startFileChooser()
            }
        }

        binding.btnVerifyImages.setOnClickListener {
            if (!(encImage1 == "" || encImage2 == "")) {
                binding.progressBar.visibility = View.VISIBLE
                binding.tvProcessing.visibility = View.VISIBLE
                binding.btnUploadImage1.visibility = View.INVISIBLE
                binding.btnUploadImage2.visibility = View.INVISIBLE
                binding.btnVerifyImages.visibility = View.INVISIBLE
                binding.tvPredictionResult.text = ""
                binding.tvPredictionResult.background = ContextCompat.getDrawable(this, R.drawable.default_text_view)
                //RETROFIT BUILDER

                var okHttpClient: OkHttpClient? = OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build()

                val retrofitBuilder = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://13.232.59.171:8000")
                    .client(okHttpClient)
                    .build()

                val recognitionAPI = retrofitBuilder.create(RecognitionAPI::class.java)

                val recognitionPost = RecognitionPost(
                    encImage1, encImage2, ""
                )

                /*println(encImage1)
                println(encImage2)*/

                val call = recognitionAPI.recogniseData(recognitionPost)

                call.enqueue(object : Callback<RecognitionPost> {
                    override fun onResponse(
                        call: Call<RecognitionPost>,
                        response: Response<RecognitionPost>
                    ) {
                        //println("Response Code: ${response.code()}")
                        //println("Response Body: ${response.body()}")
                        if (response.code() == 500) {
                            Toast.makeText(
                                this@Recognition,
                                "Face could not be detected",
                                Toast.LENGTH_LONG
                            ).show()
                            binding.progressBar.visibility = View.INVISIBLE
                            binding.tvProcessing.visibility = View.INVISIBLE
                            binding.btnUploadImage1.visibility = View.VISIBLE
                            binding.btnUploadImage2.visibility = View.VISIBLE
                            binding.btnVerifyImages.visibility = View.VISIBLE
                            binding.btnUploadImage1.text = "UPLOAD IMAGE 1"
                            binding.btnUploadImage2.text = "UPLOAD IMAGE 2"
                            encImage1 = ""
                            encImage2 = ""
                            binding.ivFirstImage.setImageResource(R.drawable.image_upload)
                            binding.ivSecondImage.setImageResource(R.drawable.image_upload)

                        }
                        if (response.body()?.verified == "true") {
                            binding.progressBar.visibility = View.INVISIBLE
                            binding.tvProcessing.visibility = View.INVISIBLE
                            binding.btnUploadImage1.visibility = View.VISIBLE
                            binding.btnUploadImage2.visibility = View.VISIBLE
                            binding.btnVerifyImages.visibility = View.VISIBLE
                            binding.tvPredictionResult.text = "SAME PERSON"
                            binding.tvPredictionResult.background = ContextCompat.getDrawable(this@Recognition, R.drawable.correct_answer)
                        }
                        if (response.body()?.verified == "false") {
                            binding.progressBar.visibility = View.INVISIBLE
                            binding.tvProcessing.visibility = View.INVISIBLE
                            binding.btnUploadImage1.visibility = View.VISIBLE
                            binding.btnUploadImage2.visibility = View.VISIBLE
                            binding.btnVerifyImages.visibility = View.VISIBLE
                            binding.tvPredictionResult.text = "DIFFERENT PERSON"
                            binding.tvPredictionResult.background = ContextCompat.getDrawable(this@Recognition, R.drawable.wrong_answer)
                        }
                    }

                    override fun onFailure(call: Call<RecognitionPost>, t: Throwable) {
                        println(t.message.toString())
                        Toast.makeText(
                            this@Recognition,
                            "Something went wrong",
                            Toast.LENGTH_LONG
                        ).show()
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.tvProcessing.visibility = View.INVISIBLE
                        binding.btnUploadImage1.visibility = View.VISIBLE
                        binding.btnUploadImage2.visibility = View.VISIBLE
                        binding.btnVerifyImages.visibility = View.VISIBLE
                        binding.btnUploadImage1.text = "UPLOAD IMAGE 1"
                        binding.btnUploadImage2.text = "UPLOAD IMAGE 2"
                        encImage1 = ""
                        encImage2 = ""
                        binding.ivFirstImage.setImageResource(R.drawable.image_upload)
                        binding.ivSecondImage.setImageResource(R.drawable.image_upload)


                        //println("Error Message: ${t.message.toString()}")
                    }

                })
            } else {
                Toast.makeText(this, "Please upload images", Toast.LENGTH_LONG).show()
            }

        }

    }

    private fun startFileChooser() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 111)
    }

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
            if (buttonText == "UPLOAD IMAGE 1") {
                binding.ivFirstImage.setImageBitmap(bitmap)
                binding.btnUploadImage1.text = "REMOVE"
            }
            if (buttonText == "UPLOAD IMAGE 2") {
                binding.ivSecondImage.setImageBitmap(bitmap)
                binding.btnUploadImage2.text = "REMOVE"
            }

            resized.apply {

                // show base64 string of bitmap
                val text = toBase64String()
                if (buttonText == "UPLOAD IMAGE 1") {
                    encImage1 = text
                }
                if (buttonText == "UPLOAD IMAGE 2") {
                    encImage2 = text
                }
                //println("Base 64 image: $text")
            }
        }
    }


    private fun Bitmap.toBase64String(): String {
        ByteArrayOutputStream().apply {
            compress(Bitmap.CompressFormat.JPEG, 10, this)
            return Base64.encodeToString(toByteArray(), Base64.NO_WRAP)
        }
    }

    private fun handleButtonClick(view: View) {
        with(view as Button) {
            buttonText = text.toString()
        }
    }

}