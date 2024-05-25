package com.nanda.mystory.view.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.nanda.mystory.R

class ButtonCustom : AppCompatButton {

    constructor(context: Context) : super(context) {
        initButton()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initButton()
    }

    private fun initButton() {
        val outlineButton: Drawable? = ContextCompat.getDrawable(context, R.drawable.outline_btn)
        outlineButton?.let {
            background = it
        }

        val textColor = ContextCompat.getColor(context, R.color.light_blue)
        setTextColor(textColor)

        if (id == R.id.btn_story) {
            textSize = 11F
        }
    }
}
