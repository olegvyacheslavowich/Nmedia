package ru.netology.nmedia.util

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.math.BigDecimal


object Util {

    fun parseNumber(number: Int): String {

        val currentNumber = BigDecimal.valueOf(number.toDouble())

        return when (number) {
            in 0..999 -> number.toString()
            in 1000..9999 -> parseDecimal(currentNumber.divide(BigDecimal(1000.0))) + "K"
            in 10_000..999_999 -> (number / 1000).toString() + "K"
            else -> parseDecimal(currentNumber.divide(BigDecimal(1_000_000.0))) + "M"
        }
    }

    private fun parseDecimal(number: BigDecimal): String {

        val currentNumber = number.setScale(1, BigDecimal.ROUND_DOWN)

        val decimalPart = currentNumber.remainder(BigDecimal.ONE).multiply(BigDecimal(10))

        return when (decimalPart.compareTo(BigDecimal(0.0))) {
            0 -> currentNumber.setScale(0, BigDecimal.ROUND_DOWN).toString()
            else -> currentNumber.toString()
        }
    }

    public fun hideKeyboard(view: View) {
        val imm: InputMethodManager =
            view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}