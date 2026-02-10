package com.example.petcare.admin.pet

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.petcare.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException
import java.util.UUID
import java.util.concurrent.TimeUnit

class AddPet : AppCompatActivity() {

    private lateinit var imgPet: ImageView
    private lateinit var btnSavePet: AppCompatButton

    private lateinit var etPetName: EditText
    private lateinit var etType: EditText
    private lateinit var etBreed: EditText
    private lateinit var etAge: EditText
    private lateinit var etGender: EditText
    private lateinit var etPrice: EditText
    private lateinit var etTags: EditText

    private var selectedImageUri: Uri? = null

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    // ✅ Cloudinary UNSIGNED details
    private val cloudName = "dlpyxtzxx"          // ✅ YOUR REAL CLOUD NAME
    private val uploadPreset = "petcare_unsigned" // ✅ Unsigned preset name

    // ✅ OkHttp client
    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    // ✅ Image Picker
    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                selectedImageUri = uri
                imgPet.setImageURI(uri)
                Log.d("IMAGE_PICK", "Selected image uri: $uri")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_pet)

        // ✅ AUTH CHECK
        if (auth.currentUser == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        bindViews()
        setupClicks()
    }

    private fun bindViews() {
        imgPet = findViewById(R.id.imgPet)
        btnSavePet = findViewById(R.id.btnSavePet)

        etPetName = findViewById(R.id.etPetName)
        etType = findViewById(R.id.etType)
        etBreed = findViewById(R.id.etBreed)
        etAge = findViewById(R.id.etAge)
        etGender = findViewById(R.id.etGender)
        etPrice = findViewById(R.id.etPrice)
        etTags = findViewById(R.id.etTags)
    }

    private fun setupClicks() {
        imgPet.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        btnSavePet.setOnClickListener {
            validateAndUpload()
        }
    }

    private fun validateAndUpload() {
        val name = etPetName.text.toString().trim()
        val type = etType.text.toString().trim()
        val breed = etBreed.text.toString().trim()
        val gender = etGender.text.toString().trim()

        val age = etAge.text.toString().trim().toIntOrNull()
        val price = etPrice.text.toString().trim().toDoubleOrNull()
        val imageUri = selectedImageUri

        if (name.isEmpty()) {
            etPetName.error = "Pet name required"
            etPetName.requestFocus()
            return
        }

        if (type.isEmpty()) {
            etType.error = "Pet type required"
            etType.requestFocus()
            return
        }

        if (age == null || age <= 0) {
            etAge.error = "Valid age required"
            etAge.requestFocus()
            return
        }

        if (price == null || price <= 0) {
            etPrice.error = "Valid price required"
            etPrice.requestFocus()
            return
        }

        if (imageUri == null) {
            Toast.makeText(this, "Please select pet image", Toast.LENGTH_SHORT).show()
            return
        }

        btnSavePet.isEnabled = false
        Toast.makeText(this, "Uploading image...", Toast.LENGTH_SHORT).show()

        uploadImageToCloudinaryUnsigned(imageUri) { url ->
            if (url != null) {
                savePetToFirestore(name, type, breed, age, gender, price, url)
            } else {
                btnSavePet.isEnabled = true
                Toast.makeText(this, "Image upload failed ❌", Toast.LENGTH_LONG).show()
            }
        }
    }

    // ✅ Upload to Cloudinary (Unsigned)
    private fun uploadImageToCloudinaryUnsigned(uri: Uri, callback: (String?) -> Unit) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            if (inputStream == null) {
                Log.e("CLOUDINARY", "InputStream is null")
                runOnUiThread { callback(null) }
                return
            }

            val bytes = inputStream.readBytes()
            inputStream.close()

            val uploadUrl = "https://api.cloudinary.com/v1_1/$cloudName/image/upload"

            Log.d("CLOUDINARY", "Upload URL = $uploadUrl")
            Log.d("CLOUDINARY", "Cloud Name = $cloudName")
            Log.d("CLOUDINARY", "Upload Preset = $uploadPreset")

            val fileBody = RequestBody.create(
                "application/octet-stream".toMediaTypeOrNull(),
                bytes
            )

            // ✅ IMPORTANT: Do NOT add resource_type here
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "pet_${UUID.randomUUID()}.jpg", fileBody)
                .addFormDataPart("upload_preset", uploadPreset)
                .addFormDataPart("folder", "pets")
                .build()

            val request = Request.Builder()
                .url(uploadUrl)
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {
                    Log.e("CLOUDINARY", "Upload failed", e)
                    runOnUiThread { callback(null) }
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body?.string()

                    Log.d("CLOUDINARY", "Response Code: ${response.code}")
                    Log.d("CLOUDINARY", "Response Body: $responseBody")

                    if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                        runOnUiThread { callback(null) }
                        return
                    }

                    val imageUrl = try {
                        val json = JSONObject(responseBody)
                        json.getString("secure_url")
                    } catch (e: Exception) {
                        Log.e("CLOUDINARY", "JSON parse error", e)
                        null
                    }

                    runOnUiThread { callback(imageUrl) }
                }
            })

        } catch (e: Exception) {
            Log.e("CLOUDINARY", "Error while uploading", e)
            runOnUiThread { callback(null) }
        }
    }

    // ✅ Save Pet to Firestore
    private fun savePetToFirestore(
        name: String,
        type: String,
        breed: String,
        age: Int,
        gender: String,
        price: Double,
        imageUrl: String
    ) {
        val tags = etTags.text.toString()
            .split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        val petRef = firestore.collection("pets").document()

        val petData = hashMapOf(
            "petId" to petRef.id,
            "name" to name,
            "type" to type,
            "breed" to breed,
            "age" to age,
            "gender" to gender,
            "price" to price,
            "tags" to tags,
            "status" to "available",
            "imageUrl" to imageUrl,
            "createdAt" to System.currentTimeMillis(),
            "createdBy" to auth.currentUser?.uid
        )

        petRef.set(petData)
            .addOnSuccessListener {
                Toast.makeText(this, "Pet added successfully ✅", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                btnSavePet.isEnabled = true
                Log.e("FIRESTORE", "Save failed", e)
                Toast.makeText(this, "Firestore error: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
