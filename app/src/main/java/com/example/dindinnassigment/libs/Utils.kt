package com.example.dindinnassigment.libs

import android.content.Context
import android.util.TypedValue
import com.example.dindinnassigment.R

object Utils {
    fun getActionBarHeightPixel(context: Context): Int {
        val tv = TypedValue()
        return if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            TypedValue.complexToDimensionPixelSize(
                tv.data,
                context.resources.displayMetrics
            )
        } else if (context.theme.resolveAttribute(R.attr.actionBarSize, tv, true)) {
            TypedValue.complexToDimensionPixelSize(
                tv.data,
                context.resources.displayMetrics
            )
        } else {
            0
        }
    }

    fun getTabHeight(context: Context): Int {
        return context.resources.getDimensionPixelSize(R.dimen.collapse_tab_height)
    }
}