package com.kulmerjo.drone.hugocopter

import com.kulmerjo.drone.hugocopter.connection.ConnectionService
import com.kulmerjo.drone.hugocopter.connection.impl.ConnectionServiceTcp
import com.kulmerjo.drone.hugocopter.helper.ResourcesHelper
import com.kulmerjo.drone.hugocopter.helper.impl.ResourcesHelperImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single<ResourcesHelper> { ResourcesHelperImpl() }
    single<ConnectionService> {
        ConnectionServiceTcp(
            get<ResourcesHelper>().getConfigValueAsString(
                androidContext(),
                R.raw.drone,
                ResourcesHelper.droneAddressPropertyName),
            get<ResourcesHelper>().getConfigValueAsInt(
                androidContext(),
                R.raw.drone,
                ResourcesHelper.dronePortPropertyName)
        )
    }
}
