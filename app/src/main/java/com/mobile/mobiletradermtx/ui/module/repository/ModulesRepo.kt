package com.mobile.mobiletradermtx.ui.module.repository

import com.mobile.mobiletradermtx.dto.ModulesResponse


interface ModulesRepo {
    suspend fun userModules(employee_id: Int): ModulesResponse
}