package com.cosmotogether.loading

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.shapes.RoundRectShape
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.cosmo.loadinganimation.R

class CosmoIconTextButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    private var title: String = ""

    private var titleTextSize = 0f
    private var titleTextColor: Int = Color.BLACK
    private var titleFontFamily: Typeface? = null

    private var startDrawableSize: Float = 0f
    private var backgroundHeight = 0
    private var customBackgroundRadius: Float = 20f
    private var customBackgroundColor: Int = Color.BLACK

    private var paddingStart: Float = 0f
    private var paddingEnd: Float = 0f

    private val shapeDrawable = android.graphics.drawable.ShapeDrawable()
    private var startDrawable: Drawable? = null
    private var backgroundDrawable: Drawable? = null

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CosmoIconTextButton,
            0,
            0
        ).apply {
            try {

                val startDrawableId =
                    getResourceId(R.styleable.CosmoIconTextButton_citStartDrawable, -1)
                if (startDrawableId != -1) {
                    startDrawable = ContextCompat.getDrawable(context, startDrawableId)
                }

                startDrawableSize = getDimension(R.styleable.CosmoIconTextButton_citStartDrawableSize, 40f)
                title = getString(R.styleable.CosmoIconTextButton_citTitle).toString()

                val titleStyleResId =
                    getResourceId(R.styleable.CosmoIconTextButton_citTitleStyle, 0)
                if (titleStyleResId != 0) {
                    val styledAttrs = context.obtainStyledAttributes(
                        titleStyleResId,
                        R.styleable.CosmoIconTextButton
                    )
                    titleTextSize = styledAttrs.getDimension(
                        R.styleable.CosmoIconTextButton_citTitleTextSize,
                        titleTextSize
                    )
                    titleTextColor = styledAttrs.getColor(
                        R.styleable.CosmoIconTextButton_citTitleTextColor,
                        titleTextColor
                    )

                    val fontFamilyId =
                        styledAttrs.getResourceId(R.styleable.CosmoIconTextButton_citTitleFont, -1)
                    if (fontFamilyId != -1) {
                        titleFontFamily = ResourcesCompat.getFont(context, fontFamilyId)
                    }
                    styledAttrs.recycle()
                }

                customBackgroundRadius =
                    getDimension(R.styleable.CosmoIconTextButton_citBackgroundRadius, 20f)
                customBackgroundColor =
                    getColor(R.styleable.CosmoIconTextButton_citBackgroundColor, Color.BLACK)
                backgroundHeight =
                    getDimensionPixelSize(R.styleable.CosmoIconTextButton_citBackgroundHeight, 0)

                paddingStart = getDimension(R.styleable.CosmoIconTextButton_citPaddingStart, 15f)
                paddingEnd = getDimension(R.styleable.CosmoIconTextButton_citPaddingEnd, 15f)

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

    }


    override fun onDraw(canvas: Canvas) {

        background = backgroundDrawable

        startDrawable?.let {

            val drawableSize = (startDrawableSize * resources.displayMetrics.density).toInt()

            // Calculate the drawable bounds
            val drawableLeft = (startDrawableSize + paddingStart - drawableSize / 2).toInt()
            val drawableTop = (height / 2 - drawableSize / 2).toInt()
            val drawableRight = (startDrawableSize + paddingStart + drawableSize / 2).toInt()
            val drawableBottom = (height / 2 + drawableSize / 2).toInt()

            // Set the bounds and draw the drawable
            it.setBounds(drawableLeft, drawableTop, drawableRight, drawableBottom)
            it.draw(canvas)
        }

        paint.color = titleTextColor
        paint.textSize = titleTextSize
        paint.typeface = titleFontFamily

        val titleX = paddingStart + ((startDrawable?.intrinsicWidth?.plus(0)) ?: 0)
        val titleY = backgroundHeight / 2f - (paint.descent() + paint.ascent()) / 2

        // Ellipsis logic for the title text
        val availableWidth = width - titleX  // Adjust based on your view width and position
        val titleWidth = paint.measureText(title)
        val ellipsis = "..."
        if (titleWidth > availableWidth) {
            var truncatedTitle = title
            while (paint.measureText(truncatedTitle + ellipsis) > availableWidth) {
                truncatedTitle = truncatedTitle.dropLast(3)
            }
            truncatedTitle += ellipsis
            canvas.drawText(truncatedTitle, titleX, titleY, paint)
        } else {
            // If title fits, draw normally
            canvas.drawText(title, titleX, titleY, paint)
        }


    }
}
