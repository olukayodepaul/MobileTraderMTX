package com.mobile.mobiletradermtx.datasource

import com.mobile.mobiletradermtx.dto.*
import retrofit2.http.*


interface RetrofitServices {

    @POST("/api/validate/defaulttoken")
    suspend fun sendTokenToday(
        @Query("urno") urno: Int,
        @Query("employee_id") employee_id: Int,
        @Query("curlocation") curlocation: String,
        @Query("region") region: Int
    ): GeneralResponse

    @GET("/api/customer/sendtokens")
    suspend fun sendTokenToday(
        @Query("unro") unro: Int
    ): SendTokenToIndividualCustomer

    @GET("/api/customer/customerorder")
    suspend fun customerOrder(
        @Query("employeeid") employeeid: Int
    ): CustomerProductOrder

    @GET("/api/customer/skuordered")
    suspend fun skuTotalOrder(
        @Query("orderid") orderid: Int
    ): SkuOrdered

    @GET("/api/customer/orderproducts")
    suspend fun orderProduct(
        @Query("employeeid") employeeid: Int,
        @Query("orderid") orderid: Int
    ): RealOrder


    @POST("/api/tm_update_outlet")
    suspend fun updateOutlet(
        @Query("tmid") tmid: Int,
        @Query("urno") urno: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("outletname") outletname: String,
        @Query("contactname") contactname: String,
        @Query("outletaddress") outletaddress: String,
        @Query("contactphone") contactphone: String,
        @Query("outletclassid") outletclassid: Int,
        @Query("outletlanguageid") outletlanguageid: Int,
        @Query("outlettypeid") outlettypeid: Int
    ): OutletUpdateResponse

    @POST("/api/tm_map_outlet")
    suspend fun mapOutlet(
        @Query("repid") repid: Int,
        @Query("tmid") tmid: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("outletname") outletname: String,
        @Query("contactname") contactname: String,
        @Query("outletaddress") outletaddress: String,
        @Query("contactphone") contactphone: String,
        @Query("outletclassid") outletclassid: Int,
        @Query("outletlanguageid") outletlanguageid: Int,
        @Query("outlettypeid") outlettypeid: Int
    ): OutletUpdateResponse


    @POST("/api/tm_outlet_info_async")
    suspend fun isCustomerInfoAsync(
        @Query("urno") urno: Int
    ): OutletAsyn

    @GET("/api/mobileagentslist")
    suspend fun isMobileAgentList(
        @Query("route_id") route_id: String
    ): MoneyAgentResponse


}