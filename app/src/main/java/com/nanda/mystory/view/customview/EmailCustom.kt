package com.nanda.mystory.view.customview

import android.text.Editable
import android.view.View
import android.content.Context
import androidx.appcompat.widget.AppCompatEditText
import android.view.MotionEvent
import com.nanda.mystory.utils.isValidEmail
import com.nanda.mystory.R
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.core.content.ContextCompat


class EmailCustom @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private val errorDrawable = ContextCompat.getDrawable(context, R.drawable.text_edit_error)
    private val normalDrawable = ContextCompat.getDrawable(context, R.drawable.text_edit)

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                error = null
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateBackgroundAndError(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean = true

    private fun updateBackgroundAndError(s: CharSequence?) {
        if (s?.let { email -> !isValidEmail(email.toString()) } == true) {
            error = context.getString(R.string.error_email)
            background = errorDrawable
        } else {
            error = null
            background = normalDrawable
        }
    }
}
