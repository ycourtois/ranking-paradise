package com.ycourtois.rankingparadise.di.component

import com.ycourtois.rankingparadise.di.module.DynamoDBModule
import com.ycourtois.rankingparadise.di.module.ServiceModule
import com.ycourtois.rankingparadise.health.DatabaseTableHealthCheck
import com.ycourtois.rankingparadise.resource.PlayerResource
import com.ycourtois.rankingparadise.resource.TournamentResource
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ServiceModule::class, DynamoDBModule::class])
interface RankingParadiseComponent {
    fun playerResource(): PlayerResource
    fun tournamentResource(): TournamentResource
    fun databaseHealthCheck(): DatabaseTableHealthCheck
}