package com.mobile.mobiletradermtx.ui.module.repository

import com.mobile.mobiletradermtx.datasource.AppDao
import com.mobile.mobiletradermtx.datasource.RetrofitService
import com.mobile.mobiletradermtx.dto.ModulesResponse


class ModulesRepoImpl(
    private val retrofitClient: RetrofitService,
    private val appdoa: AppDao
) : ModulesRepo {

    override suspend fun userModules(employee_id: Int): ModulesResponse {
        return retrofitClient.userModules(employee_id)
    }
}

