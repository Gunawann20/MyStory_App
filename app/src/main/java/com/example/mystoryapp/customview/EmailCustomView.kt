package com.example.mystoryapp.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doAfterTextChanged
import com.example.mystoryapp.R

class EmailCustomView: AppCompatEditText {
    constructor(context: Context) :super(context){
        init()
    }
    constructor(context: Context, attrs: AttributeSet) :super(context, attrs){
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) :super(context, attrs, defStyle){
        init()
    }
    private fun init(){
        doAfterTextChanged {
            error = if (isValidEmail(it.toString())){
                null
            }else{
                resources.getString(R.string.isValidEmail)
            }
        }
    }
    private fun isValidEmail(email: CharSequence): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}