package com.nanda.mystory.view.story

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nanda.mystory.R
import com.nanda.mystory.databinding.ActivityStoryBinding
import com.nanda.mystory.utils.Result
import com.nanda.mystory.utils.ViewModelFactory
import com.nanda.mystory.utils.reduceFileImage
import com.nanda.mystory.utils.showLoading
import com.nanda.mystory.utils.uriToFile
import com.nanda.mystory.view.camera.CameraActivity
import com.nanda.mystory.view.home.HomeActivity
import com.nanda.mystory.view.main.MainActivity
import com.nanda.mystory.view.setting.SettingActivity
import com.yalantis.ucrop.UCrop
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding
    private var currentImageUri: Uri? = null

    private val viewModelFactory = ViewModelFactory.getInstance(this@StoryActivity)
    private val viewModel: StoryViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.apply {title = null
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        enableEdgeToEdge()

        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.main.setOnClickListener { view ->
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }


        binding.btnGallery.setOnClickListener {
            val pickVisualMediaRequest = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            galleryLauncher.launch(pickVisualMediaRequest)
        }


        binding.btnCamera.setOnClickListener {
            if (!cameraPermission()) {
                permissionLauncher.launch(REQUIRED_PERMISSION)
            } else {
                val cameraIntent = Intent(this, CameraActivity::class.java)
                cameraXLauncher.launch(cameraIntent)
            }
        }


        binding.buttonAdd.setOnClickListener {
            viewModel.getLoginData().observe(this) { loginData ->
                if (loginData.isLogin) {
                    postStory()
                } else {
                    postStoryGuest()
                }
            }
        }

    }


    private fun postStory() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this@StoryActivity).reduceFileImage()
            val description = binding.edAddDescription.text.toString()

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile?.asRequestBody("image/jpeg".toMediaType())

            val multipartBody = requestImageFile?.let { file ->
                MultipartBody.Part.createFormData(
                    "photo",
                    imageFile.name,  // Menggunakan nama file untuk bagian nama file multipart
                    file
                )
            }

            if (multipartBody != null) {
                viewModel.postStory(multipartBody, requestBody).observe(this) {
                    if (it != null) {
                        when (it) {
                            is Result.Failure -> {
                                Toast.makeText(
                                    this@StoryActivity,
                                    getString(R.string.error_post_story),
                                    Toast.LENGTH_SHORT
                                ).show()
                                showLoading(false, binding.progressBar)
                            }

                            Result.Loading -> {
                                showLoading(true, binding.progressBar)
                            }

                            is Result.Success -> {
                                showLoading(false, binding.progressBar)
                                Toast.makeText(
                                    this@StoryActivity,
                                    it.data.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this, HomeActivity::class.java)
                                val optionsCompat: ActivityOptionsCompat =
                                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                                        this@StoryActivity,
                                        Pair(binding.imgStoryPhoto, "logo"),
                                        Pair(binding.edAddDescription, "text")
                                    )
                                startActivity(intent, optionsCompat.toBundle())
                                finish()
                            }
                        }
                    }
                }
            }


        }
    }


    private fun postStoryGuest() {
        currentImageUri?.let {
            val imageFile = uriToFile(it, this@StoryActivity)
            val description = binding.edAddDescription.text.toString()

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())

            val multipartBody =
                MultipartBody.Part.createFormData(
                    "photo",
                    description,
                    requestImageFile
                )

            viewModel.postStoryGuest(multipartBody, requestBody).observe(this@StoryActivity) { result ->
                result?.let {
                    when (it) {
                        is Result.Failure -> {
                            Toast.makeText(
                                this@StoryActivity,
                                getString(R.string.error_post_story),
                                Toast.LENGTH_SHORT
                            ).show()
                            showLoading(false, binding.progressBar)
                        }

                        Result.Loading -> {
                            showLoading(true, binding.progressBar)
                        }

                        is Result.Success -> {
                            showLoading(false, binding.progressBar)
                            Toast.makeText(
                                this@StoryActivity,
                                it.data.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this@StoryActivity, MainActivity::class.java)
                            val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                this@StoryActivity,
                                Pair(binding.imgStoryPhoto, "logo")
                            )
                            startActivity(intent, optionsCompat.toBundle())
                            finish()
                        }
                    }
                }
            }

        }
    }

    private fun cameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED
    }


    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        val message = if (isGranted) {
            getString(R.string.permission_granted)
        } else {
            getString(R.string.permission_denied)
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private val cameraXLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == CameraActivity.EXTRA_RESULT) {
            val data = result.data
            val uriString = data?.getStringExtra(CameraActivity.EXTRA_URI)
            currentImageUri = uriString?.toUri()
            showImage()
        }
    }


    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { selectedUri ->
        selectedUri?.let {
            currentImageUri = it
            showImage()
        }
    }


    private fun cropImage(imageUri: Uri): Intent {
        val destinationUri = Uri.fromFile(uriToFile(imageUri, this@StoryActivity))
        val options = UCrop.Options().apply {
            setCompressionQuality(90)
            setFreeStyleCropEnabled(true)
        }
        val uCrop = UCrop.of(imageUri, destinationUri).withOptions(options)
        val intent = uCrop.getIntent(this)

        cropImageLauncher.launch(intent)
        return intent
    }



    private val cropImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        when {
            result.resultCode == Activity.RESULT_OK && data != null -> {
                val croppedUri = UCrop.getOutput(data)
                if (croppedUri != null) {
                    val bitmap = BitmapFactory.decodeFile(croppedUri.encodedSchemeSpecificPart)
                    binding.imgStoryPhoto.setImageBitmap(bitmap)
                    currentImageUri = croppedUri
                }
            }
            result.resultCode == UCrop.RESULT_ERROR -> {
                val error = UCrop.getError(data!!)
                Log.e("StoryActivity", error.toString())
            }
        }
    }


    private fun showImage() {
        currentImageUri?.let { uri ->
            cropImage(uri)
            binding.imgStoryPhoto.setImageURI(uri)
            Log.d("ImageUri", "showImage: $uri")
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        viewModel.getLoginData().observe(this@StoryActivity) { loginData ->
            if (loginData.isLogin) {
                menuInflater.inflate(R.menu.menu_setting, menu)
            } else {
                val settingItem = menu?.findItem(R.id.btn_setting)
                settingItem?.icon?.setTint(ContextCompat.getColor(this, R.color.white))
            }
        }
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.btn_setting) {
            val intent = Intent(this, SettingActivity::class.java)
            val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@StoryActivity,
                Pair(binding.edAddDescription, "name"),
                Pair(binding.imgStoryPhoto, "logo"),
                Pair(binding.buttonAdd, "logout")
            )
            startActivity(intent, optionsCompat.toBundle())
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}