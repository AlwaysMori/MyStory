package com.nanda.mystory.view.camera

import androidx.camera.core.ImageCaptureException
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.nanda.mystory.databinding.ActivityCameraBinding
import androidx.camera.core.CameraSelector
import android.os.Build
import androidx.camera.core.Preview
import android.view.OrientationEventListener
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.view.ViewCompat
import android.os.Bundle
import com.nanda.mystory.utils.temptFile
import android.view.WindowManager
import android.view.WindowInsets
import android.content.Intent
import androidx.camera.core.ImageCapture.OutputFileOptions
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import android.util.Log
import android.view.Surface
import android.view.WindowInsetsController
import com.nanda.mystory.R
import java.io.FileOutputStream


class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding

    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    private var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideUI()

        binding.switchCamera.setOnClickListener {
            toggleCamera()
        }

        binding.captureImage.setOnClickListener {
            captureImage()
        }

    }

    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }

    override fun onResume() {
        super.onResume()
        hideUI()
        startCamera()
    }

    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }

    private val orientationEventListener by lazy {
        object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) {
                    return
                }
                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }
                imageCapture?.targetRotation = rotation
            }

        }
    }

    private fun startCamera() {
        val processCameraProvider = ProcessCameraProvider.getInstance(this)

        processCameraProvider.addListener({
            val cameraProvider: ProcessCameraProvider = processCameraProvider.get()
            val preview = Preview.Builder()
                .build().also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Toast.makeText(this@CameraActivity, R.string.error_open_camera, Toast.LENGTH_SHORT)
                    .show()
                Log.e("StartCamera", "startCamera: $e")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun toggleCamera() {
        cameraSelector =
            if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA
        startCamera()
    }

    private fun captureImage() {
        val imageCapture = imageCapture ?: return

        val file = temptFile(application)
        val outputFiles = OutputFileOptions.Builder(file).build()


        imageCapture.takePicture(
            outputFiles,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val compressedFile = compressImage(file)
                    val intent = Intent()
                    intent.putExtra(EXTRA_URI, compressedFile.toURI().toString())
                    setResult(EXTRA_RESULT, intent)
                    finish()
                }

                override fun onError(exception: ImageCaptureException) {

                    Toast.makeText(
                        this@CameraActivity,
                        getString(R.string.camera_capture_error), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }


    private fun compressImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        val compressedFile = File(file.parent, "compressed_${file.name}")

        FileOutputStream(compressedFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)
        }

        return compressedFile
    }


    private fun hideUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let { controller ->
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }


    companion object {
        const val EXTRA_URI = "extra_uri"
        const val EXTRA_RESULT = 101
    }
}
