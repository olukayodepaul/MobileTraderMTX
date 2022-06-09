package com.mobile.mobiletradermtx.util

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sqrt

object GeoFencing {

    fun setGeoFencing(
        currentLat: Double, currentLng: Double,
        customerLat: Double, customerLng: Double
    ): Boolean {
        val ky = 40000 / 360
        val kx = cos(PI * customerLat / 180.0) * ky
        val dx = abs(customerLng - currentLng) * kx
        val dy = abs(customerLat - currentLat) * ky
        return sqrt(dx * dx + dy * dy) <= 1.000 // 100 meters//->0.050 is 50meters..using two kilometer 1 for one kilometer
    }

    val currentDate: String? = SimpleDateFormat("yyyy-MM-dd").format(Date())
    val currentTime: String? = SimpleDateFormat("HH:mm:ss").format(Date())
}