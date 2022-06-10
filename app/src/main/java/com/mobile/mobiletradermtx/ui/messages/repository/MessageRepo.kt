package com.mobile.mobiletradermtx.ui.messages.repository

import com.mobile.mobiletradermtx.dto.DataAccuracy
import com.mobile.mobiletradermtx.dto.EntityAccuracy


interface MessageRepo {
    suspend fun dataAccuracy(customercode: String) : DataAccuracy
    suspend fun getDataAccuracy() : List<EntityAccuracy>
    suspend fun updateDataAccuracyStatus(status:Int,id:String)
    suspend fun getCountDataAccuracyStatus(): Int
    suspend fun isCurrentMessage(msg: List<EntityAccuracy>)
}