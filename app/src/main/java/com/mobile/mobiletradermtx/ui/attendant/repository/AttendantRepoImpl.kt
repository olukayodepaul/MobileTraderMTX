package com.mobile.mobiletradermtx.ui.attendant.repository

import com.mobile.mobiletradermtx.datasource.AppDao
import com.mobile.mobiletradermtx.datasource.RetrofitService
import com.mobile.mobiletradermtx.datasource.RetrofitServices
import com.mobile.mobiletradermtx.dto.*


class AttendantRepoImpl(
    private val retrofitClient: RetrofitService,
    private val appdoa: AppDao,
    private val retrofitService: RetrofitServices
) :
    AttendantRepo {

    override suspend fun fetchBasketFromRemoteRep(employee_id: Int): BasketLimitResponse {
        return retrofitClient.fetchBasketFromRemoteRep(employee_id)
    }

    override suspend fun setBasket(cust: List<BasketLimitList>) {
        return appdoa.setBasket(cust)
    }

    override suspend fun fetchBasketFromLocalRep(): List<BasketLimitList> {
        return appdoa.fetchBasketFromLocalRep()
    }

    override suspend fun deleteBasketFromLocalRep() {
        return appdoa.deleteBasketFromLocalRep()
    }

    override suspend fun setBasketToInitState() {
        return appdoa.setBasketToInitState()
    }

    override suspend fun task(
        employee_id: Int,
        task_id: Int,
        latitude: String,
        longitude: String,
        taskname: String
    ): GeneralResponse {
        return retrofitClient.task(employee_id, task_id, latitude, longitude, taskname)
    }

    override suspend fun setAttendantTime(timeago: String, sort: Int) {
        return appdoa.setAttendantTime(timeago, sort)
    }

    override suspend fun resetError(employee_id: Int, product_code: String, qty:Double ): OrderError {
        return retrofitClient.resetError(employee_id,product_code, qty)
    }

    override suspend fun resetPostEntry(auto: Int, total: Double) {
        return appdoa.resetPostEntry(auto, total)
    }


    //This is for the agent
    override suspend fun mobileMoneyAgentCacheOnLocalDb(route_id: String): List<IsMoneyAgent> {
        return appdoa.getAllMobileAgents(route_id.lowercase())
    }

    override suspend fun remoteMoneyAgent(route_id: String): MoneyAgentResponse {
        return retrofitService.isMobileAgentList(route_id)
    }

    override suspend fun saveRemoteMoneyAgentOnLocalCache(agents: List<IsMoneyAgent>) {
        return appdoa.setMobileAgent(agents)
    }


}