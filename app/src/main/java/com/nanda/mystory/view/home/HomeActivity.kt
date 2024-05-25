package com.nanda.mystory.view.home

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.nanda.mystory.R
import com.nanda.mystory.databinding.ActivityHomeBinding
import com.nanda.mystory.utils.ViewModelFactory
import com.nanda.mystory.view.adapter.HomeAdapter
import com.nanda.mystory.view.adapter.LoadingStateAdapter
import com.nanda.mystory.view.map.MapsActivity
import com.nanda.mystory.view.setting.SettingActivity
import com.nanda.mystory.view.story.StoryActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val viewModelFactory = ViewModelFactory.getInstance(this)
    private val viewModel: HomeViewModel by viewModels { viewModelFactory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.apply {
            title = null
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val adapter = HomeAdapter()
        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        binding.rvStory.layoutManager = layoutManager
        viewModel.stories.observe(this) {
            adapter.submitData(lifecycle, it)
        }



        binding.btnStory.setOnClickListener {
            val intent = Intent(this@HomeActivity, StoryActivity::class.java)
            val optionsCompact: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this@HomeActivity,
                    Pair(binding.btnStory, "text"),
                    Pair(binding.imgLogo, "logo")
                )
            startActivity(intent, optionsCompact.toBundle())
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.btn_setting) {
            val intent = Intent(this, SettingActivity::class.java)
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this@HomeActivity,
                    Pair(binding.btnStory, "name"),
                    Pair(binding.imgAvatar, "avatar"),
                    Pair(binding.imgLogo, "logo")
                )
            startActivity(intent, optionsCompat.toBundle())
        }
        if (item.itemId == R.id.btn_map) {
            val intent = Intent(this, MapsActivity::class.java)
            val mapString = "mapString"
            intent.putExtra(EXTRA_MAP, mapString)
            val transition = ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
            startActivity(intent, transition)
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_MAP = "extra_map"
    }

}