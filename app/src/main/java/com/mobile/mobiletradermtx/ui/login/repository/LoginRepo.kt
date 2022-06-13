package com.mobile.mobiletradermtx.ui.login.repository

import androidx.lifecycle.LiveData
import com.mobile.mobiletradermtx.dto.BreadCastNotification
import com.mobile.mobiletradermtx.dto.LoginResponse


interface LoginRepo {
    suspend fun isUserLogin(username: String, password: String) : LoginResponse
    suspend fun deleteBasketFromLocalRep()
    suspend fun deleteFromCustomersLocalRep()
    suspend fun deleteFromSpinnerLocalRep()
    suspend fun deleteFromMobileAgent()
}