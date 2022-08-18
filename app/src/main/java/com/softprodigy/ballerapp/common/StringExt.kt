package com.softprodigy.ballerapp.common

import android.util.Patterns


private const val MIN_PASS_LENGTH = 6
private const val PASS_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$"

fun String.isValidEmail(): Boolean {
    if (this.isEmpty()) {
        return false
    }
    val pattern = Patterns.EMAIL_ADDRESS
    return pattern.matcher(this).matches()
}

fun String.isValidPassword(): Boolean {
    val regex = ("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{6,}$")
    val p = regex.toRegex()
    return p.matches(this) && this.length >= 6
}

fun String.passwordMatches(repeated: String): Boolean {
    return this == repeated
}

fun String.idFromParameter(): String {
    return this.substring(1, this.length - 1)
}

fun String.isValidFullName(): Boolean {
    if (this.isEmpty()) {
        return false
    }
    return (this.trim().contains(" ") && this.length >= 5)
}