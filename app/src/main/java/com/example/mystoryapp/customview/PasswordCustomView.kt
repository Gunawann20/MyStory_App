package com.example.mystoryapp.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doAfterTextChanged
import com.example.mystoryapp.R

class PasswordCustomView: AppCompatEditText {
    constructor(context: Context) : super(context){
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle){
        init()
    }
    private fun init(){
        doAfterTextChanged {
            if (it != null){
                if (it.length < 8){
                   error =  resources.getString(R.string.passwordLength)
                }else{
                    error = null
                }
            }
        }
    }
}