package com.ycourtois.rankingparadise.di.component

import com.ycourtois.rankingparadise.di.module.ServiceModuleTest
import com.ycourtois.rankingparadise.resource.PlayerResource
import com.ycourtois.rankingparadise.resource.PlayerResourceTest
import com.ycourtois.rankingparadise.resource.TournamentResource
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ServiceModuleTest::class])
interface ResourceComponentTest {
    fun playerResource(): PlayerResource
    // fun tournamentResource(): TournamentResource
    // tested resources
    fun inject(test: PlayerResourceTest)
    // fun inject(test: TournamentResourceTest)
}