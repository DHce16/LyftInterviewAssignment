package com.example.lyftinterviewassignment.Utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    fun getCurrentTime (): String{
        return SimpleDateFormat("MMMM dd, yyyy @ hh:mm:a", Locale.getDefault()).format(Date())
    }
}