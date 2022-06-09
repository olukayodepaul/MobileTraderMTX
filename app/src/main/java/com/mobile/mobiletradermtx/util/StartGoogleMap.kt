package com.mobile.mobiletradermtx.util

import android.content.Context
import android.content.Intent
import android.net.Uri

object StartGoogleMap {

    fun startGoogleMapIntent(ctx: Context, ads: String, mode: Char, avoid: Char): Any {
        val uri = Uri.parse("google.navigation:q=$ads&mode=$mode&avoid=$avoid")
        val mIntent = Intent(Intent.ACTION_VIEW, uri)
        mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        mIntent.`package` = "com.google.android.apps.maps"
        return if (mIntent.resolveActivity(ctx.packageManager) != null) {
            ctx.startActivity(mIntent)
            true
        } else
            false
    }

}