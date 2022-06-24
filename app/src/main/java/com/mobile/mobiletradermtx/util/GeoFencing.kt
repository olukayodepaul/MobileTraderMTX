package com.mobile.mobiletradermtx.util

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.*

object GeoFencing {

//    fun setGeoFencing(
//        currentLat: Double, currentLng: Double,
//        customerLat: Double, customerLng: Double
//    ): Boolean {
//        val ky = 40000 / 360
//        val kx = cos(PI * customerLat / 180.0) * ky
//        val dx = abs(customerLng - currentLng) * kx
//        val dy = abs(customerLat - currentLat) * ky
//        return sqrt(dx * dx + dy * dy) <= 1.000 // 100 meters//->0.050 is 50meters..using two kilometer 1 for one kilometer
//    }

    //Haversine formula
    fun setGeoFencing(
        userLat: Double, userLng: Double,
        venueLat: Double, venueLng: Double
    ): Boolean {

        val averageRadiusOfTheEarth = 6371
        val latDistance = Math.toRadians(userLat - venueLat)
        val lngDistance = Math.toRadians(userLng - venueLng)

        val a = sin(latDistance/2) * sin(latDistance/2) +
                cos(Math.toRadians(userLat)) * cos(Math.toRadians(venueLat)) * sin(lngDistance/2) * sin(lngDistance/2)

        val c = atan2(sqrt(a), sqrt(1-a))
        val e = (averageRadiusOfTheEarth * c)
        return e <= 10.0 //5kilometer
    }

    val currentDate: String? = SimpleDateFormat("yyyy-MM-dd").format(Date())
    val currentTime: String? = SimpleDateFormat("HH:mm:ss").format(Date())

}