package com.mobile.mobiletradermtx.di


import com.mobile.mobiletradermtx.datasource.AppDao
import com.mobile.mobiletradermtx.datasource.RetrofitService
import com.mobile.mobiletradermtx.datasource.RetrofitServices
import com.mobile.mobiletradermtx.ui.attendant.repository.AttendantRepo
import com.mobile.mobiletradermtx.ui.attendant.repository.AttendantRepoImpl
import com.mobile.mobiletradermtx.ui.customers.repository.AddCustomerRep
import com.mobile.mobiletradermtx.ui.customers.repository.AddCustomerRepoImpl
import com.mobile.mobiletradermtx.ui.login.repository.LoginRepo
import com.mobile.mobiletradermtx.ui.login.repository.LoginRepoImpl
import com.mobile.mobiletradermtx.ui.messages.repository.MessageRepo
import com.mobile.mobiletradermtx.ui.messages.repository.MessageRepoImpl
import com.mobile.mobiletradermtx.ui.module.repository.ModulesRepo
import com.mobile.mobiletradermtx.ui.module.repository.ModulesRepoImpl
import com.mobile.mobiletradermtx.ui.order.repository.OrderRepo
import com.mobile.mobiletradermtx.ui.order.repository.OrderRepoImpl
import com.mobile.mobiletradermtx.ui.orderpurchase.repository.OrderPurchaseRepo
import com.mobile.mobiletradermtx.ui.orderpurchase.repository.OrderPurchaseRepoImpl
import com.mobile.mobiletradermtx.ui.sales.repository.SalesRepo
import com.mobile.mobiletradermtx.ui.sales.repository.SalesRepoImpl
import com.mobile.mobiletradermtx.ui.salesentry.repository.SalesEntryRepo
import com.mobile.mobiletradermtx.ui.salesentry.repository.SalesEntryRepoImpl
import com.mobile.mobiletradermtx.ui.salesrecord.repository.SalesRecordRepo
import com.mobile.mobiletradermtx.ui.salesrecord.repository.SalesRecordRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppRepository {

    @Singleton
    @Provides
    fun provideMessageRepository(
        retrofitClient: RetrofitService,
        appdoa: AppDao
    ): MessageRepo {
        return MessageRepoImpl(
            retrofitClient, appdoa
        )
    }

    @Singleton
    @Provides
    fun provideLoginRepository(
        retrofitClient: RetrofitService,
        appdoa: AppDao
    ): LoginRepo {
        return LoginRepoImpl(
            retrofitClient, appdoa
        )
    }

    @Singleton
    @Provides
    fun provideModulesRepository(
        retrofitClient: RetrofitService,
        appdoa: AppDao
    ): ModulesRepo {
        return ModulesRepoImpl(
            retrofitClient, appdoa
        )
    }

    @Singleton
    @Provides
    fun provideSalesRepository(
        retrofitClient: RetrofitService,
        appdoa: AppDao,
        retrofitServices: RetrofitServices
    ): SalesRepo {
        return SalesRepoImpl(
            retrofitClient, appdoa, retrofitServices
        )
    }

    @Singleton
    @Provides
    fun provideSalesEntryRepository(
        retrofitClient: RetrofitService,
        appdoa: AppDao
    ): SalesEntryRepo {
        return SalesEntryRepoImpl(
            retrofitClient, appdoa
        )
    }

    @Singleton
    @Provides
    fun provideSalesRecordRepository(
        retrofitClient: RetrofitService,
        appdoa: AppDao,
        retrofitServices: RetrofitServices
    ): SalesRecordRepo {
        return SalesRecordRepoImpl(
            retrofitClient, appdoa, retrofitServices
        )
    }

    @Singleton
    @Provides
    fun provideAttendantRepository(
        retrofitClient: RetrofitService,
        appdoa: AppDao,
        retrofitService: RetrofitServices
    ): AttendantRepo {
        return AttendantRepoImpl(
            retrofitClient, appdoa, retrofitService
        )
    }

    @Singleton
    @Provides
    fun provideAddCustomersRepository(
        retrofitClient: RetrofitService,
        appdoa: AppDao,
        retrofitServices: RetrofitServices
    ): AddCustomerRep {
        return AddCustomerRepoImpl(
            retrofitClient, appdoa, retrofitServices
        )
    }

    @Singleton
    @Provides
    fun provideOrderPurchaseRepository(
        retrofitClient: RetrofitService,
        appdoa: AppDao,
        networkHelper: NetworkHelper
    ): OrderPurchaseRepo {
        return OrderPurchaseRepoImpl(
            retrofitClient, appdoa, networkHelper
        )
    }

    @Singleton
    @Provides
    fun provideReOrderRepository(
        retrofitClient: RetrofitServices,
        appdoa: AppDao,
        retrofitService: RetrofitService
    ): OrderRepo {
        return OrderRepoImpl(
            retrofitClient, appdoa, retrofitService
        )
    }

}