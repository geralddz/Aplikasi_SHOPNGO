package com.app.shopngo.Object

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class Helper {

    fun gantiRupiah(value: Int): String {
        return NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(value)
    }

}