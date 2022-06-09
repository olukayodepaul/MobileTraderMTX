package com.mobile.mobiletradermtx.ui.login.repository

import com.mobile.mobiletradermtx.dto.LoginResponse


interface LoginRepo {
    suspend fun isUserLogin(username: String, password: String) : LoginResponse
    suspend fun deleteBasketFromLocalRep()
    suspend fun deleteFromCustomersLocalRep()
    suspend fun deleteFromSpinnerLocalRep()
    suspend fun deleteFromMobileAgent()
}