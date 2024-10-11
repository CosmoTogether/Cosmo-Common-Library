package com.cosmotogether.loading

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.RelativeLayout
import androidx.core.content.res.ResourcesCompat
import com.cosmo.loadinganimation.R
import com.cosmo.loadinganimation.databinding.CosmoLoaderBinding

class CosmoLoader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val binding: CosmoLoaderBinding =
        CosmoLoaderBinding.inflate(LayoutInflater.from(context), this, true)
    private lateinit var animator: ObjectAnimator
    private var message: String? = null
    private var subTitle: String? = ""
    private var speed: Long = 2000L
    private var maxOffset: Float = 70f

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CosmoLoader, 0, 0)
            message = typedArray.getString(R.styleable.CosmoLoader_message)
            speed = typedArray.getInt(R.styleable.CosmoLoader_speed, 2000).toLong()
            maxOffset = typedArray.getFloat(R.styleable.CosmoLoader_maxOffset, 70f)
            typedArray.recycle()
        }
        binding.logo.visibility = VISIBLE
        binding.logo.setImageDrawable(resources.getDrawable(R.drawable.ic_cosmo_logo, null))
        message?.let { binding.msgText.text = it }
        subTitle?.let {if (it.isNotBlank()) binding.msgText2.text = it }
    }

    fun startAnimation() {
        val density = resources.displayMetrics.density
        val maxOffsetPx = maxOffset * density

        animator =
            ObjectAnimator.ofFloat(binding.imgProgress, "translationX", 0f, maxOffsetPx).apply {
                duration = speed
                interpolator = AccelerateDecelerateInterpolator()
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
        message = newMessage
        binding.msgText.text = newMessage
    }

    fun updateSubTitle(subtitle: String) {
        subTitle = subtitle
        binding.msgText2.text = subtitle
    }

    class Builder(private val context: Context) {
        private var message: String? = null
        private var speed: Long = 2000L
        private var maxOffset: Float = 70f
        private var subTitle: String? = null

        fun setMessage(message: String) = apply {
            this.message = message
        }

        fun setSubTitle(subtitle: String) = apply {
            this.subTitle = subtitle
        }

        fun setSpeed(speed: Long) = apply {
            this.speed = speed
        }

        fun setMaxOffset(maxOffset: Float) = apply {
            this.maxOffset = maxOffset
        }

        fun build(): CosmoLoader {
            return CosmoLoader(context).apply {
                this.message = this@Builder.message
                this.subTitle = this@Builder.subTitle
                this.speed = this@Builder.speed
                this.maxOffset = this@Builder.maxOffset
            }
        }
    }
}