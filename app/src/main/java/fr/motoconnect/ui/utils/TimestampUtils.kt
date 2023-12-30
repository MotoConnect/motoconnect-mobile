package fr.motoconnect.ui.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date

class TimestampUtils {

    fun toDateString(timestamp: Timestamp): String {
        val date = Date(timestamp.seconds * 1000)
        val format = SimpleDateFormat("dd MMMM yyyy")
        return format.format(date)
    }
}