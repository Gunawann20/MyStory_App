package com.example.mystoryapp

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mystoryapp.api.ApiConfig
import com.example.mystoryapp.databinding.ActivityPostingBinding
import com.example.mystoryapp.response.RegisterResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

class PostingActivity : AppCompatActivity() {
    private var getFile: File? = null
    private lateinit var binding: ActivityPostingBinding

    companion object{
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS){
            if (!allPermissionsGranted()){
                Toast.makeText(this, "Tidak mendapatkan permission.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()){
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        binding.btnCameraX.setOnClickListener { startCameraX() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener { uploadImage() }
    }
    private fun startCameraX(){
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                it.data?.getSerializableExtra("picture", File::class.java)
            }else{
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File

            val isBackCamera = it.data?.getBooleanExtra("isbackCamera", true) as Boolean

            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                getFile = file
                binding.ivImageResult.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }
    private fun startGallery(){
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)

    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){  result ->
        if (result.resultCode == RESULT_OK){
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri->
                val myFile = uriToFile(uri, this@PostingActivity)
                getFile = myFile
                binding.ivImageResult.setImageURI(uri)
            }
        }
    }

    private fun uploadImage() {
        if (getFile != null){
            val file = reduceFileImage(getFile as File)
            val token = intent.getStringExtra("TOKEN").toString()

            val description = binding.editTextTextMultiLine.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
                val client = ApiConfig.getApiService().uploadStorie("Bearer $token", imageMultipart, description)
                client.enqueue(object : retrofit2.Callback<RegisterResponse>{
                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {
                            if (response.isSuccessful){
                                val responseBody = response.body()
                                if (responseBody != null && !responseBody.error){
                                    Toast.makeText(this@PostingActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this@PostingActivity, MainActivity::class.java))
                                }
                            }else{
                                Toast.makeText(this@PostingActivity, response.message(), Toast.LENGTH_SHORT).show()
                            }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                            Toast.makeText(this@PostingActivity, t.message, Toast.LENGTH_SHORT).show()
                    }

                })
        }else{
            Toast.makeText(this@PostingActivity, "Silakan masukan berkas gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }
}