package com.nanda.mystory.view.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.nanda.mystory.R
import com.nanda.mystory.databinding.ActivityMainBinding
import com.nanda.mystory.utils.ViewModelFactory
import com.nanda.mystory.view.home.HomeActivity
import com.nanda.mystory.view.login.LoginActivity
import com.nanda.mystory.view.register.RegisterActivity
import com.nanda.mystory.view.story.StoryActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        viewModel.getLoginData().observe(this) { loginData ->
            if (loginData.isLogin) {
                navigateToHomeActivity()
            }
        }
        binding.btnLogin.setOnClickListener { navigateToLoginActivity() }
        binding.btnRegister.setOnClickListener { navigateToRegisterActivity() }
        binding.btnGuest.setOnClickListener { navigateToStoryActivity() }
    }

    private fun navigateToHomeActivity() {
        val intent = Intent(this@MainActivity, HomeActivity::class.java)
        val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this@MainActivity,
            Pair(binding.imgLogo, "logo")
        )
        startActivity(intent, optionsCompat.toBundle())
        finish()
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this@MainActivity,
            Pair(binding.imgLogo, "logo"),
            Pair(binding.btnLogin, "login"),
            Pair(binding.btnGuest, "guest")
        )
        startActivity(intent, optionsCompat.toBundle())
    }

    private fun navigateToRegisterActivity() {
        val intent = Intent(this@MainActivity, RegisterActivity::class.java)
        val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this@MainActivity,
            Pair(binding.imgLogo, "logo"),
            Pair(binding.btnRegister, "register"),
            Pair(binding.btnGuest, "guest")
        )
        startActivity(intent, optionsCompat.toBundle())
    }

    private fun navigateToStoryActivity() {
        val intent = Intent(this@MainActivity, StoryActivity::class.java)
        val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this@MainActivity,
            Pair(binding.imgLogo, "logo"),
            Pair(binding.btnGuest, "submit")
        )
        startActivity(intent, optionsCompat.toBundle())
    }
}
