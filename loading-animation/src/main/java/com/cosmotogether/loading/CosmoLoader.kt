package com.cosmotogether.loading

import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.cosmo.loadinganimation.R

class CosmoLoader private constructor(
    private val context: Context,
    private val message: String?,
    private var speed: Long,
    private val maxOffset: Float
) : RelativeLayout(context) {

    private lateinit var imgProgress: ImageView
    private lateinit var animator: ObjectAnimator
    private lateinit var layout: RelativeLayout
    private lateinit var msgText: TextView

    init {
        initView()
    }

    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.cosmo_loader, this, true)
        layout = findViewById(R.id.checkProgress)
        imgProgress = findViewById(R.id.imgProgress)
        msgText = findViewById(R.id.msgText)

        message?.let { msgText.text = it }
    }


    fun startAnimation() {
        layout.visibility = VISIBLE
        val density = resources.displayMetrics.density
        val maxOffsetPx = maxOffset * density


        animator = ObjectAnimator.ofFloat(imgProgress, "translationX", 0f, maxOffsetPx).apply {
            duration = speed
            interpolator = AccelerateDecelerateInterpolator()
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
        }


        animator.start()
    }


    fun stopAnimation() {
        if (this::animator.isInitialized && animator.isRunning) {
            animator.end() // End the animator gracefully
        }
        layout.visibility = GONE // Hide the layout
    }


    fun updateMessage(newMessage: String) {
        msgText.text = newMessage
    }

    fun updateSpeed(newSpeed: Long) {
        speed = newSpeed
    }


    class Builder(private val context: Context) {
        private var message: String? = null
        private var speed: Long = 2000L  // Default duration for animation
        private var maxOffset: Float = 70f  // Default max offset in dp

        fun setMessage(message: String) = apply {
            this.message = message
        }

        fun setSpeed(speed: Long) = apply {
            this.speed = speed
        }

        fun setMaxOffset(maxOffset: Float) = apply {
            this.maxOffset = maxOffset
        }

        fun build(): CosmoLoader {
            return CosmoLoader(context, message, speed, maxOffset)
        }
    }
}