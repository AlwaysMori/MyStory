package com.nanda.mystory.view.detail

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.bumptech.glide.Glide
import com.nanda.mystory.R
import com.nanda.mystory.databinding.ActivityDetailBinding
import com.nanda.mystory.utils.Result
import com.nanda.mystory.utils.ViewModelFactory
import com.nanda.mystory.utils.showLoading
import com.nanda.mystory.utils.toDateFormat
import com.nanda.mystory.view.map.MapsActivity
import com.nanda.mystory.view.story.StoryActivity

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var latitude: Float? = null
    private var longitude: Float? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.apply {
            title = null
            setHomeButtonEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val viewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: DetailViewModel by viewModels { viewModelFactory }

        val id = intent.getStringExtra(EXTRA_ID)

        if (id != null) {
            viewModel.getDetailStory(id).observe(this) {
                if (it != null) {
                    when (it) {
                        is Result.Failure -> {
                            Toast.makeText(
                                this@DetailActivity,
                                getString(R.string.error_get_story),
                                Toast.LENGTH_SHORT
                            ).show()
                            showLoading(false, binding.progressBar)
                        }

                        Result.Loading -> {
                            showLoading(true, binding.progressBar)
                        }

                        is Result.Success -> {
                            showLoading(false, binding.progressBar)
                            Glide.with(this)
                                .load(it.data.story?.photoUrl)
                                .into(binding.imgStory)
                            binding.apply {
                                tvDetailName.text = it.data.story?.name
                                tvDetailDescription.text = it.data.story?.description
                                tvDetailDate.text = it.data.story?.createdAt?.toDateFormat()
                                latitude = it.data.story?.lat
                                longitude = it.data.story?.lon
                                tvLatLng.text =
                                    String.format(getString(R.string.lat_long), latitude, longitude)
                            }
                            if (latitude != null) {
                                binding.tvLatLng.visibility = View.VISIBLE
                                binding.imgLatLng.visibility = View.VISIBLE
                                binding.tvLatLng.setOnClickListener {
                                    val intent = Intent(this, MapsActivity::class.java)
                                    intent.putExtra(EXTRA_LAT, latitude.toString())
                                    intent.putExtra(EXTRA_LNG, longitude.toString())
                                    val transition = ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
                                    startActivity(intent, transition)
                                }

                            } else {
                                binding.tvLatLng.text = getString(R.string.no_location)
                            }
                        }
                    }
                }
            }
        }

        binding.btnCreate.setOnClickListener {
            val intent = Intent(this, StoryActivity::class.java)
            val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@DetailActivity,
                Pair(binding.imgDetailLogo, "logo"),
                Pair(binding.tvDetailDescription, "text"),
                Pair(binding.btnCreate, "submit")
            )
            startActivity(intent, optionsCompat.toBundle())
        }

    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_LAT = "extra_lat"
        const val EXTRA_LNG = "extra_lng"
    }
}