package com.ycourtois.rankingparadise.di.module

import com.ycourtois.rankingparadise.db.PlayerDAO
import com.ycourtois.rankingparadise.mapper.PlayerMapper
import com.ycourtois.rankingparadise.service.PlayerService
import com.ycourtois.rankingparadise.service.impl.PlayerServiceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DaoModule::class, MapperModule::class])
class ServiceModule {

    @Provides
    @Singleton
    fun providePlayerService(playerDAO: PlayerDAO, mapper: PlayerMapper): PlayerService = PlayerServiceImpl(playerDAO, mapper)
}