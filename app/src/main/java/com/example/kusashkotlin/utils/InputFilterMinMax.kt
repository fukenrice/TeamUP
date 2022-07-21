package com.example.kusashkotlin.utils

import android.text.InputFilter
import android.text.Spanned
import java.lang.Integer.max
import kotlin.math.min


class InputFilterMinMax : InputFilter {
    private var min: Int
    private var max: Int

    constructor(min: Int, max: Int) {
        this.min = min
        this.max = max
    }

    constructor(min: String, max: String) {
        this.min = min.toInt()
        this.max = max.toInt()
    }

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        try {
            val input = (dest.toString() + source.toString()).toInt()
            if (isInRange(min, max, input)) {
                return null
            }
        } catch (nfe: NumberFormatException) {
        }
        return ""
    }

    private fun isInRange(a: Int, b: Int, c: Int): Boolean { // названия параметров должны быть говорящими
        return if (b > a) c >= a && c <= b else c >= b && c <= a
        // можно кстати без if - читаться будет лучше
        // return c >= min(a, b) && c <= max(a, b)
    }
}
