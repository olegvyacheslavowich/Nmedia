package ru.netology.nmedia.api.auth

import android.content.SharedPreferences
import androidx.core.content.edit

private const val tokenKey = "token"

var SharedPreferences.token: String?
    get() = getString(tokenKey, null)
    set(value) {
        edit {
            putString(tokenKey, value)
            apply()
        }
    }