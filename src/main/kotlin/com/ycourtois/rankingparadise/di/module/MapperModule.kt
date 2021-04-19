package com.ycourtois.rankingparadise.di.module

import com.ycourtois.rankingparadise.mapper.PlayerMapper
import dagger.Module
import dagger.Provides
import org.mapstruct.factory.Mappers
import javax.inject.Singleton

@Module
class MapperModule {
    @Provides
    @Singleton
    fun providePlayerMapper(): PlayerMapper = Mappers.getMapper(PlayerMapper::class.java)
}