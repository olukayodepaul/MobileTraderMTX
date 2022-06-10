package com.mobile.mobiletradermtx.datasource


import com.mobile.mobiletradermtx.dto.*
import retrofit2.http.*


interface RetrofitService {

    @GET("/mtx/users/userslogin")
    suspend fun login(@Query("username") username: String, @Query("password") password: String): LoginResponse

    @GET("/mtx/usermodules")
    suspend fun userModules(@Query("employee_id") employee_id: Int): ModulesResponse

    @GET("/sales/customers")
    suspend fun getCustomers(@Query("employee_id") employee_id: Int): CustomersResponse

    @GET("/sales/basketlimit")
    suspend fun fetchBasketFromRemoteRep(@Query("employee_id") employee_id: Int): BasketLimitResponse

    @POST("/sales/dailysales")
    suspend fun postSales(@Body data: OrderPosted): PostSalesResponse

    @POST("/users/task")
    suspend fun task(
        @Query("employee_id") employee_id: Int,
        @Query("task_id") task_id: Int,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("taskname") taskname: String
    ): GeneralResponse

    @GET("/mtx/userspinner")
    suspend fun fetchSpinners(): UserSpinnerResponse

    @POST("/sales/newmapoutlet")
    suspend fun createCustomers(
        @Query("outletLanguageId") outletLanguageId: Int,
        @Query("outletClassId") outletClassId: Int,
        @Query("outletTypeId") outletTypeId: Int,
        @Query("outletName") outletName: String,
        @Query("contactPerson") contactPerson: String,
        @Query("mobileNumber") mobileNumber: String,
        @Query("contactAddress") contactAddress: String,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("employee_id") employee_id: Int,
        @Query("division") division: String
    ): GeneralResponse

    @GET("/sales/customerorder")
    suspend fun orderPurchase(@Query("employee_id") employee_id: Int, @Query("urno") urno: Int): OrderParentList

    @GET("/sales/errorcorrection")
    suspend fun resetError(@Query("employee_id") employee_id: Int, @Query("product_code") product_code: String, @Query("qty") qty: Double): OrderError

    @GET("/zoho/msg")
    suspend fun dataAccuracy(@Query("customercode") customercode: String): DataAccuracy

    @GET("/zoho/soq")
    suspend fun soqPrediction(@Query("urno") urno: Int): SoqPrediction

}