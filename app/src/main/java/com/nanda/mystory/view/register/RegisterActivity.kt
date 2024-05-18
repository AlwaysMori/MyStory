package com.nanda.mystory.view.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nanda.mystory.R
import com.nanda.mystory.databinding.ActivityRegisterBinding
import com.nanda.mystory.utils.Result
import com.nanda.mystory.utils.ViewModelFactory
import com.nanda.mystory.utils.isValidEmail
import com.nanda.mystory.utils.showLoading
import com.nanda.mystory.view.login.LoginActivity
import com.nanda.mystory.view.story.StoryActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        setupViews()
        setupListeners()
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            title = getString(R.string.register)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupViews() {
        enableEdgeToEdge()
        setupInputListener()
        buttonDisabled()
    }

    private fun setupListeners() {
        binding.main.setOnClickListener {
            hideKeyboard()
        }

        binding.btnGuest.setOnClickListener {
            startStoryActivityWithTransition()
        }

        binding.btnRegister.setOnClickListener {
            viewModel.postRegister(
                binding.edRegisterName.text.toString(),
                binding.edRegisterEmail.text.toString(),
                binding.edRegisterPassword.text.toString()
            ).observe(this) { result ->
                when (result) {
                    is Result.Failure -> handleFailure(result)
                    Result.Loading -> showLoading(true, binding.progressBar)
                    is Result.Success -> handleSuccess()
                }
            }
        }

        binding.btnLoginHere.setOnClickListener {
            startLoginActivityWithTransition()
        }
    }

    private fun handleFailure(result: Result.Failure) {
        with(binding) {
            edRegisterEmail.setBackgroundResource(R.drawable.text_edit_error)
            edRegisterPassword.setBackgroundResource(R.drawable.text_edit_error)
            tvError.visibility = View.VISIBLE
            showLoading(false, progressBar)
        }
    }

    private fun handleSuccess() {
        with(binding) {
            showLoading(false, progressBar)
            buttonDisabled()
            Toast.makeText(
                this@RegisterActivity,
                getString(R.string.register_success),
                Toast.LENGTH_SHORT
            ).show()
            startLoginActivityWithTransition()
        }
    }

    private fun startStoryActivityWithTransition() {
        val intent = Intent(this, StoryActivity::class.java)
        val transitionOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            Pair(binding.imgLogo, "logo"),
            Pair(binding.edRegPassword, "text"),
            Pair(binding.btnGuest, "submit")
        )
        startActivity(intent, transitionOptions.toBundle())
    }

    private fun startLoginActivityWithTransition() {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        val transitionOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            Pair(binding.btnRegister, "register"),
            Pair(binding.btnGuest, "guest"),
            Pair(binding.tvtitle, "title"),
            Pair(binding.tvdesc, "description"),
            Pair(binding.tvRegEmail, "email"),
            Pair(binding.edRegEmail, "email2"),
            Pair(binding.tvRegPassword, "password"),
            Pair(binding.edRegPassword, "password2"),
            Pair(binding.tvLoginHere, "account1"),
            Pair(binding.btnLoginHere, "account2")
        )
        startActivity(intent, transitionOptions.toBundle())
    }

    private fun buttonDisabled() {
        val emailValid = isValidEmail(binding.edRegisterEmail.text.toString())
        val nameEmpty = binding.edRegisterName.text.toString().isEmpty()
        val passwordLength = binding.edRegisterPassword.text.toString().length >= 8

        with(binding.btnRegister) {
            isEnabled = emailValid && !nameEmpty && passwordLength
            setBackgroundColor(
                if (isEnabled) getColor(R.color.light_blue) else getColor(R.color.light_blue)
            )
            setTextColor(
                if (isEnabled) getColor(R.color.white) else getColor(R.color.white)
            )
        }
    }

    private fun setupInputListener() {
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonDisabled()
            }
        }
        with(binding) {
            edRegisterEmail.addTextChangedListener(textWatcher)
            edRegisterPassword.addTextChangedListener(textWatcher)
            edRegisterName.addTextChangedListener(textWatcher)
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
