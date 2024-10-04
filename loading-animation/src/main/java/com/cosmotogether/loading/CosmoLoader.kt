package com.cosmotogether.loading

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.cosmo.cosmoanimator.R


class CosmoLoader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val maxOffset = 70f // Maximum offset in dp
    private lateinit var imgProgress: ImageView
    private lateinit var animator: ObjectAnimator
    private lateinit var layout: RelativeLayout
    private lateinit var msgText :TextView

    init {
        initView()
    }

    private fun initView() {
        // Inflate the layout for the loader
        LayoutInflater.from(context).inflate(R.layout.cosmo_loader, this, true)
        layout = findViewById(R.id.checkProgress)
        imgProgress = findViewById(R.id.imgProgress)
        msgText = findViewById(R.id.msgText)
    }

    // Start the animation
    fun startAnimation(message : String) {
        layout.visibility = VISIBLE // Make the layout visible
        msgText.text = message
        val density = resources.displayMetrics.density
        val maxOffsetPx = maxOffset * density  // Convert dp to pixels

        // Initialize the ObjectAnimator
        animator = ObjectAnimator.ofFloat(imgProgress, "translationX", 0f, maxOffsetPx).apply {
            duration = 2000L  // Duration for smooth movement
            interpolator = AccelerateDecelerateInterpolator()  // Smooth speed variation
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
        }

        // Start the animator
        animator.start()
    }

    // Stop the animation
    fun stopAnimation() {
        if (this::animator.isInitialized) {
            animator.cancel() // Cancel the animator
        }
        layout.visibility = GONE // Hide the layout
    }
}