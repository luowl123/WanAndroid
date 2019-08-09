package me.luowl.wan.util

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.databinding.BindingConversion
import me.luowl.wan.R
import me.luowl.wan.WanApplication

/*
 *
 * Created by luowl
 * Date: 2019/7/25 
 * Desc：
 */

object BindingConverters {
    @BindingConversion
    @JvmStatic
    fun convertStringToDrawable(value: Long): Drawable {
        val colors = WanApplication.context.resources.obtainTypedArray(R.array.chapter_colors)
        val color = colors.getColor((value % colors.length()).toInt(), 0)
        val gradientDrawable = GradientDrawable()
        gradientDrawable.setColor(color)
        gradientDrawable.cornerRadius = 30f
        return gradientDrawable
    }
}