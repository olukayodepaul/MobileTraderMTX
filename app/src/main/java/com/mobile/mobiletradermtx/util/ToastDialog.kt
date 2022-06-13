package com.mobile.mobiletradermtx.util

import android.content.Context
import android.widget.Toast


class ToastDialog (context: Context, msg: String) {
    val toast = Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
}


