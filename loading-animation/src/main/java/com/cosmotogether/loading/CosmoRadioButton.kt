package com.cosmotogether.loading

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.shapes.RoundRectShape
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.cosmo.loadinganimation.R

class CosmoRadioButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatRadioButton(context, attrs, defStyleAttr) {

    private var isChecked: Boolean = false
    private var title: String = ""
    private var subtitle: String = ""

    private var titleTextSize = 0f
    private var subtitleTextSize = 0f

    // Background-related properties

    private var backgroundHeight = 0
    private var customBackgroundRadius: Float = 20f
    private var titleTextColor: Int = Color.BLACK
    private var subtitleTextColor: Int = Color.BLACK
    private var customBackgroundColor: Int = Color.BLACK

    // Padding-related properties
    private var paddingStart: Float = 0f
    private var paddingEnd: Float = 0f

    // Radio button appearance-related properties
    private var radioButtonColor: Int = Color.BLUE

    // Drawable resources for selected/unselected states
    private var selectedDrawable: Drawable? = null
    private var unselectedDrawable: Drawable? = null

    // Background drawable
    private val shapeDrawable = android.graphics.drawable.ShapeDrawable()
    private var backgroundDrawable: Drawable? = null

    // Font related properties
    private var fontFamily: Typeface? = null

    // Paint object to handle drawing
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CosmoRadioButton,
            0,
            0
        ).apply {
            try {
                title = getString(R.styleable.CosmoRadioButton_title).toString()
                getString(R.styleable.CosmoRadioButton_subTitle)?.let {
                    subtitle = it
                } ?: run {
                    subtitle = ""
                }

                titleTextSize = getDimension(R.styleable.CosmoRadioButton_titleTextSize, 0f)
                subtitleTextSize = getDimension(R.styleable.CosmoRadioButton_subtitleTextSize, 0f)

                titleTextColor = getColor(R.styleable.CosmoRadioButton_titleTextColor, Color.BLACK)
                subtitleTextColor = getColor(R.styleable.CosmoRadioButton_subtitleTextColor, Color.BLACK)

                customBackgroundRadius = getDimension(R.styleable.CosmoRadioButton_backgroundRadius, 20f)
                customBackgroundColor = getColor(R.styleable.CosmoRadioButton_backgroundColorCode, Color.BLACK)
                backgroundHeight = getDimensionPixelSize(R.styleable.CosmoRadioButton_backgroundHeight, 0)
                radioButtonColor = getColor(R.styleable.CosmoRadioButton_radioButtonColor, Color.BLUE)
                isChecked = getBoolean(R.styleable.CosmoRadioButton_checked, false)
                paddingStart = getDimension(R.styleable.CosmoRadioButton_paddingStart, 15f)
                paddingEnd = getDimension(R.styleable.CosmoRadioButton_paddingEnd, 15f)

                val selectedDrawableId =
                    getResourceId(R.styleable.CosmoRadioButton_selectedDrawable, -1)
                val unselectedDrawableId =
                    getResourceId(R.styleable.CosmoRadioButton_unselectedDrawable, -1)
                if (selectedDrawableId != -1) {
                    selectedDrawable = ContextCompat.getDrawable(context, selectedDrawableId)
                }
                if (unselectedDrawableId != -1) {
                    unselectedDrawable = ContextCompat.getDrawable(context, unselectedDrawableId)
                }

                // Initialize the font family
                val fontFamilyId = getResourceId(R.styleable.CosmoRadioButton_font, -1)
                if (fontFamilyId != -1) {
                    fontFamily = ResourcesCompat.getFont(context, fontFamilyId)
                }

                shapeDrawable.paint.color = customBackgroundColor
                backgroundDrawable = shapeDrawable.apply {
                    height = backgroundHeight
                    shape = RoundRectShape(
                        floatArrayOf(
                            customBackgroundRadius,
                            customBackgroundRadius,
                            customBackgroundRadius,
                            customBackgroundRadius,
                            customBackgroundRadius,
                            customBackgroundRadius,
                            customBackgroundRadius,
                            customBackgroundRadius
                        ),
                        null, null
                    )

                }


            } finally {
                recycle()
            }
        }

        setOnClickListener {
            isChecked = isChecked.not()
            invalidate()
        }
    }


    override fun onDraw(canvas: Canvas) {
        // Draw the background circle with radius

        background = backgroundDrawable

        // Draw the drawable for selected/unselected state
        val drawable = if (isChecked) selectedDrawable else unselectedDrawable
        drawable?.let {
            val drawableSize =
                it.intrinsicWidth.coerceAtMost(it.intrinsicHeight) // Use the drawable's intrinsic size
            val drawableLeft = (20f + paddingStart - drawableSize / 2).toInt()
            val drawableTop = (height / 2 - drawableSize / 2).toInt()
            val drawableRight = (20f + paddingStart + drawableSize / 2).toInt()
            val drawableBottom = (height / 2 + drawableSize / 2).toInt()
            it.setBounds(drawableLeft, drawableTop, drawableRight, drawableBottom)
            it.draw(canvas)
        }


        if (subtitle.isNotBlank()) {

            paint.color = titleTextColor
            paint.textSize = titleTextSize
            paint.typeface = fontFamily

            val topPadding = ((backgroundHeight - (titleTextSize + subtitleTextSize)) / 2) + 10f
            Log.d("CosmoRadioButton", "onDraw: TopPadding $topPadding")

            val titleX = 20f * 2 + 10f + paddingStart + 10f
            val titleY = topPadding + titleTextSize / 2
            canvas.drawText(title, titleX, titleY, paint)

            // Calculate Y position for the subtitle (subtitle follows the title with no gap)
            val subtitleY = titleY + 5f + subtitleTextSize
            paint.color = subtitleTextColor
            paint.textSize = subtitleTextSize
            paint.typeface = fontFamily
            canvas.drawText(subtitle, titleX, subtitleY, paint)

        } else {

            paint.color = titleTextColor
            paint.textSize = titleTextSize
            paint.typeface = fontFamily

            val titleX = 20f * 2 + 10f + paddingStart + 10f
            val titleY = backgroundHeight / 2f - (paint.descent() + paint.ascent()) / 2
            canvas.drawText(title, titleX, titleY, paint)

        }


    }
}
