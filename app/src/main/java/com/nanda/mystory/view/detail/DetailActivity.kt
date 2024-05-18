package com.nanda.mystory.view.detail

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import com.nanda.mystory.R
import com.nanda.mystory.databinding.ActivityDetailBinding
import com.nanda.mystory.utils.Result
import com.nanda.mystory.utils.ViewModelFactory
import com.nanda.mystory.utils.showLoading
import com.nanda.mystory.utils.toDateFormat
import com.nanda.mystory.view.story.StoryActivity
import com.bumptech.glide.Glide
import com.nanda.mystory.databinding.ActivityCameraBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        supportActionBar?.apply {
            title = null
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val viewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: DetailViewModel by viewModels { viewModelFactory }

        val id = intent.getStringExtra(EXTRA_ID)
        Log.d("Detail", id.toString())

        if (id != null) {
            viewModel.getDetailStory(id).observe(this) {
                when (it) {
                    is Result.Failure -> {
                        Toast.makeText(
                            this@DetailActivity,
                            getString(R.string.error_get_story),
                            Toast.LENGTH_SHORT
                        ).show()
                        showLoading(false, binding.progressBar)

                        binding.apply {
                            tvDetailName.setTextColor(ContextCompat.getColor(this@DetailActivity, R.color.black))
                            tvDetailDescription.setTextColor(ContextCompat.getColor(this@DetailActivity, R.color.blue_900))
                            tvDetailDate.setTextColor(ContextCompat.getColor(this@DetailActivity, R.color.black))
                        }
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

                            tvDetailName.setTextColor(ContextCompat.getColor(this@DetailActivity, R.color.black))
                            tvDetailDescription.setTextColor(ContextCompat.getColor(this@DetailActivity, R.color.black))
                            tvDetailDate.setTextColor(ContextCompat.getColor(this@DetailActivity, R.color.blue_900))
                        }
                    }
                }
            }
        }


        binding.btnCreate.setOnClickListener {
            val intent = Intent(this, StoryActivity::class.java)
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this@DetailActivity,
                    Pair(binding.imgDetailLogo, "logo"),
                    Pair(binding.tvDetailDescription, "text"),
                    Pair(binding.btnCreate, "create")
                )
            startActivity(intent, optionsCompat.toBundle())
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}