package com.nanda.mystory.view.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nanda.mystory.R
import com.nanda.mystory.data.model.DataModel
import com.nanda.mystory.databinding.ActivityCameraBinding
import com.nanda.mystory.databinding.ActivityLoginBinding
import com.nanda.mystory.utils.Result
import com.nanda.mystory.utils.ViewModelFactory
import com.nanda.mystory.utils.isValidEmail
import com.nanda.mystory.utils.showLoading
import com.nanda.mystory.view.home.HomeActivity
import com.nanda.mystory.view.register.RegisterActivity
import com.nanda.mystory.view.story.StoryActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupInputListener()
        buttonDisabled()
        enableEdgeToEdge()

        val viewModelFactory = ViewModelFactory.getInstance(this@LoginActivity)
        val viewModel: LoginViewModel by viewModels { viewModelFactory }

        binding.main.setOnClickListener { view ->
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }


        binding.btnRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            val options = arrayOf<Pair<View, String>>(
                Pair(binding.btnLogin, "login"),
                Pair(binding.btnGuest, "guest"),
                Pair(binding.tvLogDesc, "description"),
                Pair(binding.edEmail, "email2"),
                Pair(binding.edPassword, "password2"),
                Pair(binding.tvRegister, "account1"),
                Pair(binding.btnRegister, "account2")
            )
            val optionsCompact = ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity, *options)
            startActivity(intent, optionsCompact.toBundle())
        }


        binding.btnGuest.setOnClickListener {
            val intent = Intent(this@LoginActivity, StoryActivity::class.java)
            val options = arrayOf<Pair<View, String>>(
                Pair(binding.imgLogo, "logo"),
                Pair(binding.edPassword, "text"),
                Pair(binding.btnGuest, "submit")
            )
            val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity, *options)
            startActivity(intent, optionsCompat.toBundle())
        }


        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            viewModel.postLogin(email, password).observe(this) { result ->
                when (result) {
                    is Result.Failure -> {
                        binding.edLoginEmail.setBackgroundResource(R.drawable.text_edit_error)
                        binding.edLoginPassword.setBackgroundResource(R.drawable.text_edit_error)
                        binding.tvError.visibility = View.VISIBLE
                        showLoading(false, binding.progressBar)
                    }
                    Result.Loading -> {
                        showLoading(true, binding.progressBar)
                    }
                    is Result.Success -> {
                        showLoading(false, binding.progressBar)
                        val token = result.data.loginResult?.token.toString()
                        val name = result.data.loginResult?.name.toString()
                        viewModel.saveData(
                            DataModel(
                                name,
                                token
                            )
                        )
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        Log.i("TOKEN", token)
                        val optionsCompat: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                this@LoginActivity,
                                Pair(binding.imgLogo, "logo")
                            )
                        startActivity(intent, optionsCompat.toBundle())
                        finish()
                    }
                }
            }
        }



    }


    private fun buttonDisabled() {
        val emailValid = isValidEmail(binding.edLoginEmail.text.toString())
        val passwordValid = binding.edLoginPassword.text.toString().length >= 8

        binding.btnLogin.isEnabled = emailValid && passwordValid

        binding.btnLogin.setBackgroundColor(
            if (emailValid && passwordValid) {
                getColor(R.color.light_blue)
            } else {
                getColor(R.color.light_blue)
            }
        )

        binding.btnLogin.setTextColor(
            if (emailValid && passwordValid) {
                getColor(R.color.white)
            } else {
                getColor(R.color.white)
            }
        )
    }


    private fun setupInputListener() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonDisabled()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        binding.edLoginEmail.addTextChangedListener(textWatcher)
        binding.edLoginPassword.addTextChangedListener(textWatcher)
    }
}