package com.example.mystoryapp.preferences

import android.annotation.SuppressLint
import android.content.Context
import com.example.mystoryapp.response.UserModel

class UserPreference(context: Context) {
    private val preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    fun setUser(value: UserModel){
        val editor = preference.edit()
        editor.putString(NAME, value.name)
        editor.putString(USERID, value.userId)
        editor.putString(TOKEN, value.token)
        editor.putBoolean(LOGIN, value.isLogin)
        editor.apply()
    }
    fun getUSer(): UserModel{
        val model = UserModel()
        model.name = preference.getString(NAME,"")
        model.userId = preference.getString(USERID, "")
        model.token = preference.getString(TOKEN, "")
        model.isLogin = preference.getBoolean(LOGIN, false)

        return model
    }
    @SuppressLint("CommitPrefEdits")
    fun removeUser(){
        val editor = preference.edit()
        editor.clear()
        editor.apply()
    }
    companion object{
        private const val PREFS_NAME = "user_pref"
        private const val NAME = "name"
        private const val USERID = "userId"
        private const val TOKEN = "token"
        private const val LOGIN = "islogin"
    }
}