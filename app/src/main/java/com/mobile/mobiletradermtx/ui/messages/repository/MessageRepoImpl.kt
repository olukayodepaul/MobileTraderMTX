package com.mobile.mobiletradermtx.ui.messages.repository

import com.mobile.mobiletradermtx.datasource.AppDao
import com.mobile.mobiletradermtx.datasource.RetrofitService
import com.mobile.mobiletradermtx.dto.DataAccuracy
import com.mobile.mobiletradermtx.dto.EntityAccuracy


class MessageRepoImpl (
    private val retrofitClient: RetrofitService,
    private val appdoa: AppDao
) : MessageRepo {

    override suspend fun dataAccuracy(customercode: String): DataAccuracy {
        return retrofitClient.dataAccuracy(customercode)
    }

    override suspend fun getDataAccuracy(): List<EntityAccuracy> {
        return appdoa.getDataAccuracy()
    }

    override suspend fun updateDataAccuracyStatus(status: Int, id: String) {
        return appdoa.updateDataAccuracyStatus(status, id)
    }

    override suspend fun isCurrentMessage(msg: List<EntityAccuracy>) {
        return appdoa.isCurrentMessage(msg)
    }

}