package com.ycourtois.rankingparadise.di.module

import com.ycourtois.rankingparadise.service.PlayerService
import dagger.Module
import dagger.Provides
import org.mockito.Mockito
import javax.inject.Singleton

@Module
class ServiceModuleTest {

    @Provides
    @Singleton
    fun providePlayerService(): PlayerService {
        return Mockito.mock(PlayerService::class.java)
    }
}