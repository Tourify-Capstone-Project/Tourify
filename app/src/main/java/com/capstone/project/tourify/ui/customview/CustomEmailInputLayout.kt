package com.capstone.project.tourify.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import com.capstone.project.tourify.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CustomEmailInputLayout(context: Context, attrs: AttributeSet) :
    TextInputLayout(context, attrs) {

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let {
                if (Patterns.EMAIL_ADDRESS.matcher(it.toString()).matches()) {
                    error = null
                } else {
                    error = context.getString(R.string.email_not_valid)
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    fun setEditText(editText: TextInputEditText) {
        editText.addTextChangedListener(textWatcher)
    }
}