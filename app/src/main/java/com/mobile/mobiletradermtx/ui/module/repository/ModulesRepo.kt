package com.mobile.mobiletradermtx.ui.module.repository

import com.mobile.mobiletradermtx.dto.DataAccuracy
import com.mobile.mobiletradermtx.dto.EntityAccuracy
import com.mobile.mobiletradermtx.dto.ModulesResponse


interface ModulesRepo {
    suspend fun userModules(employee_id: Int): ModulesResponse
    suspend fun dataAccuracy(customercode: String) : DataAccuracy
    suspend fun getDataAccuracy() : List<EntityAccuracy>
    suspend fun updateDataAccuracyStatus(status:Int,id:String)
    suspend fun isCurrentMessage(msg: List<EntityAccuracy>)
}