package com.cosmotogether.libs.statusbar

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.text.format.DateFormat
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.cosmo.loadinganimation.R

class CosmoTextClock : AppCompatTextView {
    private var display_type = 0
    private val mIntentReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            onTimeChanged()
        }
    }
    private var registered = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setAttribs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        setAttribs(context, attrs)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        onTimeChanged()
        if (!registered) {
            registered = true
            registerReceiver()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (registered) {
            unregisterReceiver()
            registered = false
        }
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == VISIBLE) refresh()
    }

    override fun onScreenStateChanged(screenState: Int) {
        super.onScreenStateChanged(screenState)
        if (screenState == SCREEN_STATE_ON) onTimeChanged()
    }

    private fun registerReceiver() {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_TIME_TICK)
        context.registerReceiver(mIntentReceiver, filter)
    }

    private fun unregisterReceiver() {
        try {
            context.unregisterReceiver(mIntentReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun onTimeChanged() {
        val is24 = DateFormat.is24HourFormat(context)
        val elements: List<String> =
            context.getCosmoFormattedTime(
                timestamp = System.currentTimeMillis(),
                format = Util.TIME_FORMAT_HH_H_hh_h_mm_ss_SSS_a, localeSelection = "AE"
            ).split("_")
        val display_text: String
        when (display_type) {
            4 -> display_text =
                Util.getFormattedDate(
                    context,
                    System.currentTimeMillis(),
                    Util.DATE_FORMAT_MEDIUMTEXT
                )

            3 -> display_text = if (is24) "" else elements[7]
            2 -> display_text =
                if (is24) """
     ${elements[0]}
     ${elements[4]}
     """.trimIndent() else """
     ${elements[2]}
     ${elements[4]}
     """.trimIndent()

            1 -> display_text =
                if (is24) elements[0] + ":" + elements[4] else elements[3] + ":" + elements[4]

            0 -> display_text =
                Util.getFormattedTime(context, System.currentTimeMillis(), localeSelection = "")

            else -> display_text =
                Util.getFormattedTime(context, System.currentTimeMillis(), localeSelection = "")
        }
        text = display_text
    }

    fun refresh() {
        onTimeChanged()
    }

    private fun setAttribs(context: Context, attrs: AttributeSet?) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.CosmoTextClock)
        display_type = array.getInteger(R.styleable.CosmoTextClock_display_type, 0)
        array.recycle()
    }
}
