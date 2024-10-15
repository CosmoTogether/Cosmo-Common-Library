package com.cosmotogether.libs.statusbar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import com.cosmo.loadinganimation.R
import com.cosmo.loadinganimation.databinding.LayoutTopbarBinding

class CosmoStatusbar(context: Context, attrs: AttributeSet?) : LinearLayoutCompat(context, attrs) {
    private var _binding: LayoutTopbarBinding? = null
    private val binding get() = _binding!!

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        _binding = LayoutTopbarBinding.inflate(inflater, this, true)
        setUpViews(attrs)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }

    private fun setUpViews(attrs: AttributeSet?) {
        // Access views using binding.id
        // Update visibility, color, text based on position and data

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.StatusBarWidget)
        val position = typedArray.getInt(R.styleable.StatusBarWidget_position, 0)

        if (position == 0) {
            binding.topBarPageApps.visibility = View.GONE
            binding.topBarPageRoot.visibility = View.VISIBLE
        } else {
            binding.topBarPageRoot.visibility = View.GONE
            binding.topBarPageApps.visibility = View.VISIBLE
        }

        binding.topBarPageApps.setBackgroundColor(Color.TRANSPARENT)
        typedArray.recycle()
    }

    fun refreshData() {
        binding.title.text = "School Mode"
        binding.topBarPageApps.setBackgroundColor(context.getColor(R.color.colorBlack))
        // Update adapter state if applicable
        // adatpter.updateAdapterState()
    }

}