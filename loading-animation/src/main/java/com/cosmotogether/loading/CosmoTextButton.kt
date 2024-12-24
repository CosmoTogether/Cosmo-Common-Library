package com.cosmotogether.loading

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.shapes.RoundRectShape
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.content.res.ResourcesCompat
import com.cosmo.loadinganimation.R

class CosmoTextButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatRadioButton(context, attrs, defStyleAttr) {

    private var title: String = ""

    // Text-related properties
    private var titleTextSize = 0f
    private var titleTextColor: Int = Color.BLACK
    private var titleFontFamily: Typeface? = null

    // Background-related properties
    private var backgroundHeight = 0
    private var customBackgroundRadius: Float = 20f
    private var customBackgroundColor: Int = Color.BLACK

    // Padding-related properties
    private var paddingStart: Float = 0f
    private var paddingEnd: Float = 0f

    // Background drawable
    private val shapeDrawable = android.graphics.drawable.ShapeDrawable()
    private var backgroundDrawable: Drawable? = null

    // Paint object to handle drawing
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CosmoTextButton,
            0,
            0
        ).apply {
            try {

                title = getString(R.styleable.CosmoTextButton_ctbTitle).toString()

                val titleStyleResId = getResourceId(R.styleable.CosmoTextButton_ctbTitleStyle, 0)
                if (titleStyleResId != 0) {
                    val styledAttrs = context.obtainStyledAttributes(titleStyleResId, R.styleable.CosmoTextButton)
                    titleTextSize = styledAttrs.getDimension(R.styleable.CosmoTextButton_ctbTitleTextSize, titleTextSize)
                    titleTextColor = styledAttrs.getColor(R.styleable.CosmoTextButton_ctbTitleTextColor, titleTextColor)

                    val fontFamilyId = styledAttrs.getResourceId(R.styleable.CosmoTextButton_ctbTitleFont, -1)
                    if (fontFamilyId != -1) {
                        titleFontFamily = ResourcesCompat.getFont(context, fontFamilyId)
                    }
                    styledAttrs.recycle()
                }

                customBackgroundRadius = getDimension(R.styleable.CosmoTextButton_ctbBackgroundRadius, 20f)
                customBackgroundColor = getColor(R.styleable.CosmoTextButton_ctbBackgroundColor, Color.BLACK)
                backgroundHeight = getDimensionPixelSize(R.styleable.CosmoTextButton_ctbBackgroundHeight, 0)
                paddingStart = getDimension(R.styleable.CosmoTextButton_ctbPaddingStart, 15f)
                paddingEnd = getDimension(R.styleable.CosmoTextButton_ctbPaddingEnd, 15f)


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

        background = backgroundDrawable

        paint.color = titleTextColor
        paint.textSize = titleTextSize
        paint.typeface = titleFontFamily

        val titleX = paddingStart
        val titleY = backgroundHeight / 2f - (paint.descent() + paint.ascent()) / 2

        // Ellipsis logic for the title text
        val availableWidth = width - titleX  // Adjust based on your view width and position
        val titleWidth = paint.measureText(title)
        val ellipsis = "..."
        if (titleWidth > availableWidth) {
            // Truncate title text to fit, remove last 6 characters and add ellipsis
            var truncatedTitle = title
            while (paint.measureText(truncatedTitle + ellipsis) > availableWidth) {
                // Drop the last 6 characters at a time
                truncatedTitle = truncatedTitle.dropLast(8)
            }
            truncatedTitle += ellipsis
            canvas.drawText(truncatedTitle, titleX, titleY, paint)
        } else {
            // If title fits, draw normally
            canvas.drawText(title, titleX, titleY, paint)
        }


    }
}
