package com.ycourtois.rankingparadise.di.component

import com.ycourtois.rankingparadise.di.module.DaoModule
import com.ycourtois.rankingparadise.di.module.DynamoDBModule
import com.ycourtois.rankingparadise.resource.PlayerResourceIT
import com.ycourtois.rankingparadise.resource.TournamentResourceIT
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DynamoDBModule::class, DaoModule::class])
interface RankingParadiseComponentIT {
    fun injectPlayerResourceIT(test: PlayerResourceIT)
    fun injectTournamentResourceIT(test: TournamentResourceIT)
}