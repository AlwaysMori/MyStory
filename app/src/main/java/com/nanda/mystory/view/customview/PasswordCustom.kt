package com.nanda.mystory.view.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.nanda.mystory.R

class PasswordCustom @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private val errorDrawable = ContextCompat.getDrawable(context, R.drawable.text_edit_error)
    private val normalDrawable = ContextCompat.getDrawable(context, R.drawable.text_edit)

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateBackground(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean = true

    private fun updateBackground(s: CharSequence?) {
        if ((s?.length ?: 0) < 8) {
            setError(context.getString(R.string.error_password), null)
            background = errorDrawable
        } else {
            error = null
            background = normalDrawable
        }
    }
}