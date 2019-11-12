package com.kulmerjo.drone.hugocopter

import com.kulmerjo.drone.hugocopter.helper.ResourcesHelper
import com.kulmerjo.drone.hugocopter.helper.impl.ResourcesHelperImpl
import org.koin.dsl.module

val appModule = module {
    single<ResourcesHelper> { ResourcesHelperImpl() }
//    single<ConnectionService> {
//        ConnectionServiceTcp(
//            get<ResourcesHelper>().getConfigValueAsString(ResourcesHelper.droneAddressPropertyName),
//            get<ResourcesHelper>().getConfigValueAsInt(ResourcesHelper.dronePortPropertyName)
//        )
//    }
}
