package com.example.expensemanager.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Utils {
    companion object {
        fun dateFormat(dateToString : Date) : String {
            val sdf = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
            return sdf.format(dateToString)
        }

    }
}