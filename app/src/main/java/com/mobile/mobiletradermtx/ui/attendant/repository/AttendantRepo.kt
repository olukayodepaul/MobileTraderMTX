package com.mobile.mobiletradermtx.ui.attendant.repository

import com.mobile.mobiletradermtx.dto.*


interface AttendantRepo {
    suspend fun fetchBasketFromRemoteRep(employee_id: Int): BasketLimitResponse
    suspend fun setBasket(cust: List<BasketLimitList>)
    suspend fun fetchBasketFromLocalRep() : List<BasketLimitList>
    suspend fun deleteBasketFromLocalRep()
    suspend fun setBasketToInitState()
    suspend fun task(employee_id: Int, task_id: Int, latitude: String, longitude: String, taskname: String): GeneralResponse
    suspend fun setAttendantTime(timeago:String, sort:Int )
    suspend fun resetError(employee_id:Int, product_code:String, qty:Double ): OrderError
    suspend fun resetPostEntry(auto:Int, total:Double )
    suspend fun mobileMoneyAgentCacheOnLocalDb(route_id:String) : List<IsMoneyAgent>
    suspend fun remoteMoneyAgent(route_id:String) : MoneyAgentResponse
    suspend fun saveRemoteMoneyAgentOnLocalCache(agents: List<IsMoneyAgent>)
}