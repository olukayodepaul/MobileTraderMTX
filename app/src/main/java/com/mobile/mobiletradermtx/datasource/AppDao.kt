package com.mobile.mobiletradermtx.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.mobiletradermtx.dto.*

@Dao
interface AppDao {

    @Query("SELECT * FROM customers")
    suspend fun getCustomers() : List<CustomersList>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCustomers(cust: List<CustomersList>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setBasket(cust: List<BasketLimitList>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setMobileAgent(cust: List<IsMoneyAgent>)

    @Query("SELECT * FROM osqty")
    suspend fun fetchBasketFromLocalRep() : List<BasketLimitList>

    @Query("delete from osqty")
    suspend fun deleteBasketFromLocalRep()

    @Query("delete from customers")
    suspend fun deleteFromCustomersLocalRep()

    @Query("delete from spinners")
    suspend fun deleteFromSpinnerLocalRep()

    @Query("delete from moneyagent")
    suspend fun deleteFromMobileAgent()

    @Query("UPDATE osqty SET inventory=:inventory, pricing=:pricing, orders=:order, entry_time=:entry_time, controlpricing=:controlpricing, controlinventory = :controlinventory, controlorder=:controlorder where  auto=:auto")
    suspend fun updateDailySales(inventory: Double, pricing: Int, order: Double, entry_time: String, controlpricing:Int, controlinventory:Int, controlorder:Int, auto:Int)

    @Query("SELECT count(auto) FROM osqty WHERE   controlpricing = '0' OR controlinventory = '0' OR controlorder = '0'")
    suspend fun validateSalesEntry() : Int

    @Query("UPDATE  osqty SET  controlpricing = '0', controlinventory = '0',  controlorder = '0' , pricing= '0.0', inventory = '0.0', orders = '0.0', entry_time = ''")
    suspend fun setBasketToInitState()

    @Query("SELECT * FROM osqty WHERE  (inventory!='0.0'  OR pricing !='0.0' OR orders !='0.0' ) ")
    suspend fun salesPosted():  List<BasketLimitList>

    @Query("UPDATE  osqty SET  controlpricing = '0', controlinventory = '0',  controlorder = '0' , pricing= '0.0', inventory = '0.0', orders = '0.0', entry_time = '',  order_sold = order_sold - orders  WHERE auto=:auto and seperator = '1'")
    suspend fun resetOrders(auto: Int)

    @Query("UPDATE  customers SET timeago = :timeago WHERE urno = :urno ")
    suspend fun setVisitTime(timeago:String, urno:Int )

    @Query("SELECT * FROM spinners")
    suspend fun fetchSpinnerFromLocalDb() : List<UserSpinnerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCustomer(cust: List<UserSpinnerEntity>)

    @Query("UPDATE  customers SET timeago = :timeago WHERE sort = :sort ")
    suspend fun setAttendantTime(timeago:String, sort:Int )

    @Query("UPDATE customers SET outletclassid=:outletclassid, outletlanguageid=:outletlanguageid, outlettypeid=:outlettypeid, outletname=:outletname, outletaddress=:outletaddress, contactname=:contactname, contactphone=:contactphone, latitude=:latitude, longitude=:longitude where urno=:urno and sort = 2")
    suspend fun updateIndividualCustomer(outletclassid:Int, outletlanguageid:Int, outlettypeid:Int, outletname:String, outletaddress:String, contactname:String, contactphone:String, latitude:Double, longitude:Double,urno:Int)

    @Query("UPDATE  osqty SET order_sold = :total WHERE  auto=:auto and seperator = '1'")
    suspend fun resetPostEntry(auto:Int, total:Double )

    @Query("SELECT * FROM moneyagent where employee_id = :route_id")
    suspend fun getAllMobileAgents(route_id:String) : List<IsMoneyAgent>

    @Query("SELECT * FROM  dataAccuracy")
    suspend fun getDataAccuracy() : List<EntityAccuracy>

    @Query("UPDATE dataAccuracy SET status = :status WHERE _id = :id")
    suspend fun updateDataAccuracyStatus(status:Int,id:String)

    @Query("SELECT count(status)  FROM  dataAccuracy WHERE status = 2 ")
    suspend fun getCountDataAccuracyStatus(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun isCurrentMessage(msg: List<EntityAccuracy>)

}

