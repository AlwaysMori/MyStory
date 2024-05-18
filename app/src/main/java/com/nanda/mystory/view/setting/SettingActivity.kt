package com.nanda.mystory.view.setting

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.nanda.mystory.R
import com.nanda.mystory.databinding.ActivitySettingBinding
import com.nanda.mystory.utils.ViewModelFactory
import com.nanda.mystory.view.main.MainActivity

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private val viewModel: SettingViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActionBar()
        setupViews()
        setupListeners()
        enableEdgeToEdge()

    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            title = getString(R.string.setting)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupViews() {
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getData().observe(this) {
            binding.btnName.text = it.name
        }
    }

    private fun setupListeners() {
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnLanguage.setOnClickListener {
            val languageSettingsIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(languageSettingsIntent)
        }
    }
}
