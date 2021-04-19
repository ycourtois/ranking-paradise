package com.ycourtois.rankingparadise.di.module

import com.ycourtois.rankingparadise.db.PlayerDAO
import dagger.Module
import dagger.Provides
import org.mockito.Mockito
import javax.inject.Singleton

@Module
class DaoModuleTest {

    @Provides
    @Singleton
    fun providePlayerDAO(): PlayerDAO {
        return Mockito.mock(PlayerDAO::class.java)
    }

}