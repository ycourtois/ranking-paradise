package com.ycourtois.rankingparadise.di.component

import com.ycourtois.rankingparadise.di.module.DaoModuleTest
import com.ycourtois.rankingparadise.di.module.MapperModule
import com.ycourtois.rankingparadise.service.PlayerService
import com.ycourtois.rankingparadise.service.PlayerServiceTest
import com.ycourtois.rankingparadise.service.impl.PlayerServiceImpl
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DaoModuleTest::class, MapperModule::class])
interface ServiceComponentTest {
    fun playerService(): PlayerServiceImpl

    // tested resources
    fun inject(test: PlayerServiceTest)
}