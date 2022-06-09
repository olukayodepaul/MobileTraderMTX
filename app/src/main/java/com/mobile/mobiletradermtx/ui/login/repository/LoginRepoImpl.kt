package com.mobile.mobiletradermtx.ui.login.repository


import com.mobile.mobiletradermtx.datasource.AppDao
import com.mobile.mobiletradermtx.datasource.RetrofitService
import com.mobile.mobiletradermtx.dto.LoginResponse


class LoginRepoImpl (
    private val retrofitClient: RetrofitService,
    private val appdoa: AppDao
) : LoginRepo {

    override suspend fun isUserLogin(
        username: String,
        password: String,
    ): LoginResponse {
        return retrofitClient.login(username, password)
    }

    override suspend fun deleteBasketFromLocalRep() {
        return appdoa.deleteBasketFromLocalRep()
    }

    override suspend fun deleteFromCustomersLocalRep() {
        return appdoa.deleteFromCustomersLocalRep()
    }

    override suspend fun deleteFromSpinnerLocalRep() {
        return appdoa.deleteFromSpinnerLocalRep()
    }

    override suspend fun deleteFromMobileAgent() {
        return appdoa.deleteFromMobileAgent()
    }
}