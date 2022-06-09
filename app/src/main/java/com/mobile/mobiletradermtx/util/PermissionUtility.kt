package com.mobile.mobiletradermtx.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat


object PermissionUtility {


    private fun hasWriteExternalPermission(context: Context) = ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    private fun hasLocationForegrandPermission(context: Context) = ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun hasBackgroudPermission(context: Context) =
        ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun hasAccessFineLocationPermission(context: Context) = ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun hasPhoneStatePermission(context: Context) = ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.READ_PHONE_STATE
    ) == PackageManager.PERMISSION_GRANTED

    private fun hasCamaraPermission(context: Context) = ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED


    fun requestPermission(context: Context): MutableList<String> {

        val permisonToRequest = mutableListOf<String>()

        if (!hasWriteExternalPermission(context)) {
            permisonToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (!hasLocationForegrandPermission(context)) {
            permisonToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        //for android Q only
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            if (!hasBackgroudPermission(context)) {
                permisonToRequest.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
        }

        if (!hasAccessFineLocationPermission(context)) {
            permisonToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (!hasPhoneStatePermission(context)) {
            permisonToRequest.add(Manifest.permission.READ_PHONE_STATE)
        }

        return permisonToRequest
    }
}

