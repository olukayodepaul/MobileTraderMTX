package com.mobile.mobiletradermtx.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.mobile.mobiletradermtx.R
import kotlin.math.roundToInt

object CircleImage {

    fun getNameInitials(fullName: String): String {
        return fullName[0].uppercase()
    }

    fun getNameInitialsBg(context: Context): Drawable? {
        val drawables = arrayOf(
            R.drawable.name_bg1, R.drawable.name_bg2, R.drawable.name_bg,
            R.drawable.name_bg3, R.drawable.name_bg4, R.drawable.name_bg5,
            R.drawable.name_bg6, R.drawable.name_bg7, R.drawable.name_bg8
        )
        return ContextCompat.getDrawable(context!!, drawables[(Math.random() * 8).roundToInt()])
    }

}