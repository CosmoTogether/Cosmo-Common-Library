package com.cosmotogether.libs.statusbar

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import com.cosmo.loadinganimation.R
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale


object Util {

    const val TIME_FORMAT_WITHSECONDS: Int = 202
    const val TIME_FORMAT_24WITHSECONDS: Int = 203
    const val TIME_FORMAT_24WITHMILLISECONDS: Int = 204
    const val TIME_FORMAT_HH_H_hh_h_mm_ss_SSS_a: Int = 205
    const val DATE_FORMAT_DEFAULT: Int = 101
    const val DATE_FORMAT_LAST7DAYS: Int = 102
    const val DATE_FORMAT_LAST2DAYS: Int = 103
    const val DATE_FORMAT_NUMERIC: Int = 104
    const val DATE_FORMAT_FULLTEXT: Int = 105
    const val DATE_FORMAT_MEDIUMTEXT: Int = 106
    const val TIME_FORMAT_DEFAULT: Int = 201
    const val DATETIME_FORMAT_LAST7DAYS: Int = 302

    const val DATETIME_FORMAT_LAST2DAYS: Int = 303

    const val DATETIME_FORMAT_NUMERIC: Int = 304

    const val DATETIME_FORMAT_NUMERIC_WITHMILLIS: Int = 305
    const val DATETIME_FORMAT_FILENAME: Int = 306

    @SuppressLint("SimpleDateFormat")
    fun getFormattedTime(
        context: Context,
        timestamp: Long,
        format: Int = TIME_FORMAT_DEFAULT,
        localeSelection: String
    ): String {
        val sdfTime: SimpleDateFormat
        var secondFormat = ""
        when (format) {
            TIME_FORMAT_24WITHSECONDS -> sdfTime = SimpleDateFormat("HH:mm:ss")
            TIME_FORMAT_24WITHMILLISECONDS -> sdfTime = SimpleDateFormat("HH:mm:ss.SSS")
            TIME_FORMAT_HH_H_hh_h_mm_ss_SSS_a -> sdfTime = SimpleDateFormat("HH_H_hh_h_mm_ss_SSS_a")
            else -> {
                if (format == TIME_FORMAT_WITHSECONDS) secondFormat = ":ss"
                if (DateFormat.is24HourFormat(context)) {
                    sdfTime = SimpleDateFormat("HH:mm$secondFormat")
                } else {
                    if (Locale.getDefault().language == localeSelection) {
                        val sym = DateFormatSymbols(Locale("hy"))
                        sym.amPmStrings = arrayOf("am", "pm")
                        sdfTime = SimpleDateFormat("h:mm$secondFormat a", sym)
                    } else {
                        sdfTime =
                            SimpleDateFormat("h:mm$secondFormat a", Locale.getDefault())
                    }
                }
            }

        }
        val ts = Calendar.getInstance()
        ts.timeInMillis = timestamp
        val time = sdfTime.format(ts.time)
        return time
    }

    fun getFormattedDateTime(
        context: Context,
        timestamp: Long,
        format: Int,
        localeSelection: String
    ): String {
        var datetime: String = "${
            getFormattedDate(
                context,
                timestamp, format
            )
        }, ${
            getFormattedTime(
                context = context,
                timestamp = timestamp,
                format = format,
                localeSelection = localeSelection
            )
        }"
        when (format) {
            DATETIME_FORMAT_LAST7DAYS -> datetime =
                (getFormattedDate(
                    context,
                    timestamp,
                    DATE_FORMAT_LAST7DAYS
                )
                        + ", "
                        + getFormattedTime(context, timestamp, localeSelection = localeSelection))

            DATETIME_FORMAT_LAST2DAYS -> datetime =
                (getFormattedDate(
                    context,
                    timestamp,
                    DATE_FORMAT_LAST2DAYS
                )
                        + ", "
                        + getFormattedTime(context, timestamp, localeSelection = localeSelection))

            DATETIME_FORMAT_NUMERIC -> datetime =
                (getFormattedDate(
                    context,
                    timestamp,
                    DATE_FORMAT_NUMERIC
                )
                        + " "
                        + getFormattedTime(
                    context,
                    timestamp,
                    TIME_FORMAT_24WITHSECONDS, localeSelection
                ))

            DATETIME_FORMAT_NUMERIC_WITHMILLIS -> datetime =
                (getFormattedDate(
                    context,
                    timestamp,
                    DATE_FORMAT_NUMERIC
                )
                        + " "
                        + getFormattedTime(
                    context, timestamp, TIME_FORMAT_24WITHMILLISECONDS, localeSelection
                ))

            DATETIME_FORMAT_FILENAME -> datetime =
                (getFormattedDate(
                    context,
                    timestamp,
                    DATE_FORMAT_NUMERIC
                )
                    .replace("-".toRegex(), "")
                        + "_"
                        + getFormattedTime(
                    context,
                    timestamp,
                    TIME_FORMAT_24WITHSECONDS, ""
                )
                    .replace(":".toRegex(), ""))
        }
        return datetime
    }

    fun getFormattedDate(context: Context, timestamp: Long, format: Int): String {
        var skeleton =
            DateFormat.getBestDateTimePattern(Locale.getDefault(), "d MMM")
        if (format == DATE_FORMAT_NUMERIC) skeleton = "yyyy-MM-dd"
        else if (format == DATE_FORMAT_FULLTEXT) skeleton =
            "EEEE, MMMM d, yyyy"
        else if (format == DATE_FORMAT_MEDIUMTEXT) skeleton =
            DateFormat.getBestDateTimePattern(
                Locale.getDefault(), "EEE MMM d"
            )
        val sdfDate = SimpleDateFormat(skeleton)
        val ts = Calendar.getInstance()
        ts.timeInMillis = timestamp
        var date = sdfDate.format(ts.time)
        if (format == DATE_FORMAT_LAST7DAYS || format == DATE_FORMAT_LAST2DAYS) {
            if (isToday(timestamp)) date =
                context.getString(R.string.today)
            else if (isYesterday(timestamp)) date =
                context.getString(R.string.yesterday)
            else if (format == DATE_FORMAT_LAST7DAYS && (isLast7Days(
                    timestamp
                ))
            ) date = DateFormat.format("EEEE", ts.time).toString()
        }
        return date
    }

    private fun isToday(timestamp: Long): Boolean {
        return isSameDay(timestamp, System.currentTimeMillis())
    }

    private fun isSameDay(timestamp1: Long, timestamp2: Long): Boolean {
        val ts1 = Calendar.getInstance()
        val ts2 = Calendar.getInstance()
        ts1.timeInMillis = timestamp1
        ts2.timeInMillis = timestamp2
        return (ts1[Calendar.YEAR] == ts2[Calendar.YEAR])
                && (ts1[Calendar.DAY_OF_YEAR] == ts2[Calendar.DAY_OF_YEAR])
    }

    private fun isYesterday(timestamp: Long): Boolean {
        val ts = Calendar.getInstance()
        ts.timeInMillis = timestamp
        ts.add(Calendar.DATE, 1)
        return isSameDay(ts.timeInMillis, System.currentTimeMillis())
    }

    private fun isLast7Days(timestamp: Long): Boolean {
        val to: Calendar = GregorianCalendar()
        val from: Calendar = GregorianCalendar()
        from[Calendar.HOUR_OF_DAY] = 0
        from[Calendar.MINUTE] = 0
        from[Calendar.SECOND] = 0
        from[Calendar.MILLISECOND] = 0
        to.timeInMillis = from.timeInMillis
        to.add(Calendar.DATE, 1)
        from.add(Calendar.DATE, -6)
        return ((timestamp > from.timeInMillis) && (timestamp < to.timeInMillis))
    }

}

fun Context.getCosmoFormattedTime(
    timestamp: Long,
    format: Int,
    localeSelection: String
): String {
    return Util.getFormattedTime(this, timestamp, format, localeSelection)
}

fun Context.getCosmoFormattedDateTime(timestamp: Long, format: Int, localeSelection: String) =
    Util.getFormattedDateTime(this, timestamp, format, localeSelection)