package com.example.mystoryapp.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(
    var name: String? = null,
    var userId: String? = null,
    var token: String? = null,
    var isLogin: Boolean = false
) : Parcelable
