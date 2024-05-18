package com.nanda.mystory.view.home

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.nanda.mystory.data.model.DataModel
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.nanda.mystory.R
import com.nanda.mystory.data.api.remote.response.StoryResponse
import com.nanda.mystory.databinding.ActivityHomeBinding
import com.nanda.mystory.utils.Result
import com.nanda.mystory.utils.ViewModelFactory
import com.nanda.mystory.view.adapter.HomeAdapter
import com.nanda.mystory.view.setting.SettingActivity
import com.nanda.mystory.view.story.StoryActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: HomeAdapter
    private val viewModelFactory = ViewModelFactory.getInstance(this)
    private val viewModel: HomeViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.apply {
            title = null
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        initializeViews()
        observeViewModel()

        binding.btnStory.setOnClickListener {
            navigateToStoryActivity()
        }
    }

    private fun initializeViews() {
        adapter = HomeAdapter()
        binding.rvStory.adapter = adapter
        binding.rvStory.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModel() {
        viewModel.getStories().observe(this) { result ->
            handleStoriesResult(result)
        }

        viewModel.getLoginData().observe(this) { loginData ->
            handleLoginData(loginData)
        }
    }

    private fun handleStoriesResult(result: Result<StoryResponse>) {
        when (result) {
            is Result.Failure -> {
                showLoading(false)
                Toast.makeText(
                    this@HomeActivity,
                    getString(R.string.error_get_story),
                    Toast.LENGTH_SHORT
                ).show()
            }

            Result.Loading -> {
                showLoading(false)
            }

            is Result.Success -> {
                showLoading(false)
                result.data.listStory?.let { stories ->
                    adapter.submitList(stories.filterNotNull())
                }
            }
        }
    }


    private fun handleLoginData(loginData: DataModel) {
        val token = loginData.token
        val loginState = loginData.isLogin
        Log.d("Token_Home", token.toString())
        Log.d("Login_Home", loginState.toString())
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun navigateToStoryActivity() {
        val intent = Intent(this@HomeActivity, StoryActivity::class.java)
        val optionsCompact: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this@HomeActivity,
            Pair(binding.btnStory, "text"),
            Pair(binding.imgLogo, "logo")
        )
        startActivity(intent, optionsCompact.toBundle())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.btn_setting) {
            navigateToSettingActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToSettingActivity() {
        val intent = Intent(this, SettingActivity::class.java)
        val optionsCompat: ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@HomeActivity,
                Pair(binding.btnStory, "name"),
                Pair(binding.imgLogo, "logo"),
                Pair(binding.imgAvatar, "avatar")
            )
        startActivity(intent, optionsCompat.toBundle())
    }
}
