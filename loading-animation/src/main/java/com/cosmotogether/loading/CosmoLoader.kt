package com.cosmotogether.loading

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.RelativeLayout
import com.cosmo.loadinganimation.R
import com.cosmo.loadinganimation.databinding.CosmoLoaderBinding

class CosmoLoader private constructor(
    context: Context,
    private val message: String?,
    private val speed: Long,
    private val maxOffset: Float
) : RelativeLayout(context) {

    private val binding: CosmoLoaderBinding = CosmoLoaderBinding.inflate(LayoutInflater.from(context), this, true)
    private lateinit var animator: ObjectAnimator

    init {
        message?.let { binding.msgText.text = it }
    }


    fun startAnimation() {
        val density = resources.displayMetrics.density
        val maxOffsetPx = maxOffset * density


        animator = ObjectAnimator.ofFloat(binding.imgProgress, "translationX", 0f, maxOffsetPx).apply {
            duration = speed  // Use custom speed
            interpolator = AccelerateDecelerateInterpolator()  // Smooth speed variation
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
        }


        animator.start()
    }


    fun stopAnimation() {
        if (this::animator.isInitialized && animator.isRunning) {
            animator.end()
        }
    }

    fun updateMessage(newMessage: String) {
        binding.msgText.text = newMessage
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