package com.mobile.mobiletradermtx.dto


import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class LoginResponse(
    @SerializedName("status")
    @Expose
    var status: Int? = null,
    @SerializedName("msg")
    @Expose
    var msg: String? = null,
    @SerializedName("login")
    @Expose
    var login: Employees? = null
)

data class Employees(
    @SerializedName("depots_id")
    @Expose
    var depots_id: Int? = null,
    @SerializedName("region_id")
    @Expose
    var region_id: Int? = null,
    @SerializedName("dates")
    @Expose
    var dates: String? = null,
    @SerializedName("syscategory_id")
    @Expose
    var syscategory_id: Int? = null,
    @SerializedName("employee_id")
    @Expose
    var employee_id: Int? = null,
    @SerializedName("name")
    @Expose
    var name: String? = null,
    @SerializedName("employee_code")
    @Expose
    var employee_code: String? = null,
    @SerializedName("phone_imei")
    @Expose
    var phone_imei: String? = null,
    @SerializedName("username")
    @Expose
    var username: String? = null,
    @SerializedName("password")
    @Expose
    var password: String? = null,
    @SerializedName("depotlat")
    @Expose
    var depotlat: String? = null,
    @SerializedName("depotlng")
    @Expose
    var depotlng: String? = null,
    @SerializedName("depotwaiver")
    @Expose
    var depotwaiver: String? = null,

    )

data class ModulesResponse(
    @SerializedName("status")
    @Expose
    var status: Int? = null,
    @SerializedName("msg")
    @Expose
    var msg: String? = null,
    @SerializedName("modules")
    @Expose
    var modules: List<UserModules>? = null
)

data class UserModules(
    @SerializedName("dates")
    @Expose
    var dates: String? = null,
    @SerializedName("employee_id")
    @Expose
    var employee_id: Int? = null,
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("navigationid")
    @Expose
    var navigationid: Int? = null,
    @SerializedName("name")
    @Expose
    var name: String? = null,
    @SerializedName("imageurl")
    @Expose
    var imageurl: String? = null
)

data class CustomersResponse(
    @SerializedName("status")
    @Expose
    var status: Int? = null,
    @SerializedName("msg")
    @Expose
    var msg: String? = null,
    @SerializedName("customers")
    @Expose
    var customers: List<Customers>? = null
)

@Parcelize
data class Customers(
    @SerializedName("auto")
    @Expose
    var auto: Int? = null,
    @SerializedName("dates")
    @Expose
    var dates: String? = null,
    @SerializedName("timeago")
    @Expose
    var timeago: String? = null,
    @SerializedName("_id")
    @Expose
    var _id: String? = null,
    @SerializedName("employee_id")
    @Expose
    var employee_id: Int? = null,
    @SerializedName("urno")
    @Expose
    var urno: Int? = null,
    @SerializedName("customerno")
    @Expose
    var customerno: String? = null,
    @SerializedName("outletname")
    @Expose
    var outletname: String? = null,
    @SerializedName("latitude")
    @Expose
    var latitude: String? = null,
    @SerializedName("longitude")
    @Expose
    var longitude: String? = null,
    @SerializedName("sort")
    @Expose
    var sort: Int? = null,
    @SerializedName("notice")
    @Expose
    var notice: String? = null,
    @SerializedName("outlet_waiver")
    @Expose
    var outlet_waiver: String? = null,
    @SerializedName("cust_token")
    @Expose
    var cust_token: String? = null,
    @SerializedName("defaulttoken")
    @Expose
    var defaulttoken: String? = null,
    @SerializedName("seq")
    @Expose
    var seq: String? = null,
    @SerializedName("modes")
    @Expose
    var modes: Int? = null,
    @SerializedName("rep_id")
    @Expose
    var rep_id: Int? = null,
    @SerializedName("outletclassid")
    @Expose
    var outletclassid: Int? = null,
    @SerializedName("outletlanguageid")
    @Expose
    var outletlanguageid: Int? = null,
    @SerializedName("outlettypeid")
    @Expose
    var outlettypeid: Int? = null,
    @SerializedName("outletaddress")
    @Expose
    var outletaddress: String? = null,
    @SerializedName("contactphone")
    @Expose
    var contactphone: String? = null,
    @SerializedName("contactname")
    @Expose
    var contactname: String? = null,
    @SerializedName("outlet_pic")
    @Expose
    var outlet_pic: String? = null,
    @SerializedName("repname")
    @Expose
    var repname: String? = null,
    @SerializedName("volumeclass")
    @Expose
    var volumeclass: String? = null,
    @SerializedName("employee_code")
    @Expose
    var employee_code: String? = null,
    @SerializedName("customer_code")
    @Expose
    var customer_code: String? = null,
    @SerializedName("distance")
    @Expose
    var distance: String? = null,
    @SerializedName("duration")
    @Expose
    var duration: String? = null,
    @SerializedName("depotwaivers")
    @Expose
    var depotwaivers: String? = null,
    @SerializedName("spec")
    @Expose
    var spec: String? = null,
    @SerializedName("__v")
    @Expose
    var __v: String? = null,
    @SerializedName("th")
    @Expose
    var th: String? = null,
) : Parcelable

data class BasketLimitResponse(
    @SerializedName("status")
    @Expose
    var status: Int? = null,
    @SerializedName("msg")
    @Expose
    var msg: String? = null,
    @SerializedName("basketlimit")
    @Expose
    var basketlimit: List<BasketLimit>? = null
)

data class BasketLimit(
    @SerializedName("auto")
    @Expose
    var auto: Int = 0,
    @SerializedName("employee_id")
    @Expose
    var employee_id: Int? = null,
    @SerializedName("product_id")
    @Expose
    var product_id: Int? = null,
    @SerializedName("product_code")
    @Expose
    var product_code: String? = null,
    @SerializedName("qty")
    @Expose
    var qty: Float? = null,
    @SerializedName("soq")
    @Expose
    var soq: String? = null,
    @SerializedName("order_sold")
    @Expose
    var order_sold: Float? = null,
    @SerializedName("price")
    @Expose
    var price: Float? = null,
    @SerializedName("product_name")
    @Expose
    var product_name: String? = null,
    @SerializedName("seperator")
    @Expose
    var seperator: String? = null,
    @SerializedName("seperatorname")
    @Expose
    var seperatorname: String? = null,
    @SerializedName("dates")
    @Expose
    var dates: String? = null,
    @SerializedName("pricing")
    @Expose
    var pricing: Float? = null,
    @SerializedName("inventory")
    @Expose
    var inventory: Float? = null,
    @SerializedName("orders")
    @Expose
    var orders: Float? = null,
    @SerializedName("entry_time")
    @Expose
    var entry_time: String? = null,
    @SerializedName("controlpricing")
    @Expose
    var controlpricing: String? = null,
    @SerializedName("controlinventory")
    @Expose
    var controlinventory: String? = null,
    @SerializedName("controlorder")
    @Expose
    var controlorder: String? = null,
    @SerializedName("blimit")
    @Expose
    var blimit: String? = null
)

data class PostSalesResponse(
    @SerializedName("status")
    @Expose
    var status: Int? = null,
    @SerializedName("msg")
    @Expose
    var msg: String? = null
)

data class OrderPosted(
    @SerializedName("clat")
    @Expose
    var clat: String? = null,
    @SerializedName("clng")
    @Expose
    var clng: String? = null,

    @SerializedName("etime")
    @Expose
    var etime: String? = null,

    @SerializedName("edate")
    @Expose
    var edate: String? = null,

    @SerializedName("uiid")
    @Expose
    var uiid: String? = null,

    @SerializedName("employee_id")
    @Expose
    var employee_id: Int? = null,

    @SerializedName("urno")
    @Expose
    var urno: Int? = null,

    @SerializedName("customerno")
    @Expose
    var customerno: String? = null,

    @SerializedName("outletname")
    @Expose
    var outletname: String? = null,

    @SerializedName("outletlatitude")
    @Expose
    var outletlatitude: String? = null,

    @SerializedName("outletlongitude")
    @Expose
    var outletlongitude: String? = null,

    @SerializedName("volumeclass")
    @Expose
    var volumeclass: String? = null,

    @SerializedName("token")
    @Expose
    var token: String? = null,

    @SerializedName("remark")
    @Expose
    var remark: String? = null,
    @SerializedName("order")
    @Expose
    var order: List<BasketLimit>? = null,
    @SerializedName("distance")
    @Expose
    var distance: String? = null,
    @SerializedName("duration")
    @Expose
    var duration: String? = null,
    @SerializedName("atime")
    @Expose
    var atime: String? = null
)

data class GeneralResponse(
    @SerializedName("status")
    @Expose
    var status: Int? = null,
    @SerializedName("msg")
    @Expose
    var msg: String? = null
)

data class UserSpinnerResponse(
    @SerializedName("status")
    @Expose
    var status: Int? = null,
    @SerializedName("msg")
    @Expose
    var msg: String? = null,
    @SerializedName("spinners")
    @Expose
    var spinners: List<UserSpinner>? = null
)

data class UserSpinner(
    @SerializedName("auto")
    @Expose
    var auto: Int? = null,
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("name")
    @Expose
    var name: String? = null,
    @SerializedName("sep")
    @Expose
    var sep: Int? = null
)

data class GetRequestToken(
    val token: String? = null,
    val status: String? = null
)

data class RealOrder(
    @SerializedName("status")
    @Expose
    var status: Int = 0,
    @SerializedName("msg")
    @Expose
    var msg: String = ""
)

data class SendTokenToIndividualCustomer(
    @SerializedName("status")
    @Expose
    var status: String = ""
)

data class CustomerProductOrder(
    @SerializedName("order")
    @Expose
    var order: List<AllCustomerProductOrder>? = null
)

@Parcelize
data class AllCustomerProductOrder(
    @SerializedName("orderid")
    @Expose
    var orderid: Int = 0,
    @SerializedName("outletname")
    @Expose
    var outletname: String = "",
    @SerializedName("urno")
    @Expose
    var urno: Int = 0,
    @SerializedName("customerno")
    @Expose
    var customerno: String = "",
    @SerializedName("latitude")
    @Expose
    var latitude: String = "",
    @SerializedName("longitude")
    @Expose
    var longitude: String = "",
    @SerializedName("employeeid")
    @Expose
    var employeeid: Int = 0,
    @SerializedName("contactphone")
    @Expose
    var contactphone: String = "",
    @SerializedName("outletaddress")
    @Expose
    var outletaddress: String = "",
    @SerializedName("uid")
    @Expose
    var uid: String = "",
    @SerializedName("token")
    @Expose
    var token: Int = 0,
    @SerializedName("dates")
    @Expose
    var dates: String = "",
    @SerializedName("trantime")
    @Expose
    var trantime: String = "",
    @SerializedName("trantype")
    @Expose
    var trantype: String = ""
) : Parcelable

data class SkuOrdered(
    @SerializedName("skuorder")
    @Expose
    var skuorder: List<AllSkuOrdered>? = null,
    @SerializedName("totalqty")
    @Expose
    var totalqty: Double = 0.0,
    @SerializedName("totalamount")
    @Expose
    var totalamount: Double = 0.0
)

data class AllSkuOrdered(
    @SerializedName("id")
    @Expose
    var id: Int = 0,
    @SerializedName("customerinfo")
    @Expose
    var customerinfo: Int = 0,
    @SerializedName("qtyordered")
    @Expose
    var qtyordered: Double? = 0.0,
    @SerializedName("skuname")
    @Expose
    var skuname: String = "",
    @SerializedName("productcode")
    @Expose
    var productcode: String = "",
    @SerializedName("amount")
    @Expose
    var amount: Double = 0.0
)

data class OutletUpdateResponse(
    @SerializedName("status")
    @Expose
    var status: Int = 0,
    @SerializedName("notis")
    @Expose
    var notis: String = ""
)

data class OutletAsyn(
    @SerializedName("status")
    @Expose
    var status: Int = 0,
    @SerializedName("outletclassid")
    @Expose
    var outletclassid: Int = 0,
    @SerializedName("outletlanguageid")
    @Expose
    var outletlanguageid: Int = 0,
    @SerializedName("outlettypeid")
    @Expose
    var outlettypeid: Int = 0,
    @SerializedName("outletname")
    @Expose
    var outletname: String = "",
    @SerializedName("outletaddress")
    @Expose
    var outletaddress: String = "",
    @SerializedName("contactname")
    @Expose
    var contactname: String = "",
    @SerializedName("contactphone")
    @Expose
    var contactphone: String = "",
    @SerializedName("longitude")
    @Expose
    var longitude: String = "",
    @SerializedName("latitude")
    @Expose
    var latitude: String = ""
)

data class OrderParentList(
    @SerializedName("msg")
    @Expose
    var msg: String? = null,
    @SerializedName("status")
    @Expose
    var status: Int? = null,
    @SerializedName("orderlist")
    @Expose
    var orderlist: List<Orders>? = null
)

data class Orders(
    @SerializedName("remark")
    @Expose
    var remark: String? = null,
    @SerializedName("etime")
    @Expose
    var etime: String? = null,
    @SerializedName("volumeclass")
    @Expose
    var volumeclass: String? = null,
    @SerializedName("expandable")
    @Expose
    var expandable: Boolean? = false,
    @SerializedName("order")
    @Expose
    var order: List<OrderItems>? = null
) {
    data class OrderItems(
        @SerializedName("entry_time")
        @Expose
        var entry_time: String? = null,
        @SerializedName("inventory")
        @Expose
        var inventory: Double? = null,
        @SerializedName("orders")
        @Expose
        var orders: Double? = null,
        @SerializedName("price")
        @Expose
        var price: Double? = null,
        @SerializedName("pricing")
        @Expose
        var pricing: Double? = null,
        @SerializedName("product_code")
        @Expose
        var product_code: String? = null,
        @SerializedName("product_id")
        @Expose
        var product_id: String? = null,
        @SerializedName("product_name")
        @Expose
        var product_name: String? = null,
        @SerializedName("seperatorname")
        @Expose
        var seperatorname: String? = null,
    )
}

data class OrderError(
    @SerializedName("status")
    @Expose
    var status: Int? = null,
    @SerializedName("msg")
    @Expose
    var msg: String? = null,
    @SerializedName("sum")
    @Expose
    var sum: Double? = null
)

data class MoneyAgentResponse(
    @SerializedName("status")
    @Expose
    var status: Int? = null,
    @SerializedName("msg")
    @Expose
    var msg: String? = null,
    @SerializedName("agents")
    @Expose
    var agents: List<MoneyAgent>? = null
)

data class MoneyAgent(
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("route_id")
    @Expose
    var route_id: String? = null,
    @SerializedName("agenttype")
    @Expose
    var agenttype: String? = null,
    @SerializedName("agennama")
    @Expose
    var agennama: String? = null,
    @SerializedName("phone")
    @Expose
    var phone: String? = null,
    @SerializedName("address")
    @Expose
    var address: String? = null,
    @SerializedName("lat")
    @Expose
    var lat: String? = null,
    @SerializedName("lng")
    @Expose
    var lng: String? = null,
    @SerializedName("distance")
    @Expose
    var distance: String? = null,
    @SerializedName("employee_id")
    @Expose
    var employee_id: String? = null,
)

data class DataAccuracy(
    @SerializedName("status")
    @Expose
    var status: Int? = null,
    @SerializedName("msg")
    @Expose
    var msg: String? = null,
    @SerializedName("accuracy")
    @Expose
    var accuracy: List<DataAccuracyConditions>? = null
) {
    data class DataAccuracyConditions(
        @SerializedName("id")
        @Expose
        var id: Int? = null,
        @SerializedName("_id")
        @Expose
        var _id: String? = null,
        @SerializedName("entry_date")
        @Expose
        var entry_date: String? = null,
        @SerializedName("remark")
        @Expose
        var remark: String? = null,
        @SerializedName("status")
        @Expose
        var status: Int? = null
    )
}

data class BreadCastNotification(val counts: Int = 0)
















