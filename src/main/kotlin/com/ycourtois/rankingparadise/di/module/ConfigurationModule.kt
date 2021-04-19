package com.ycourtois.rankingparadise.di.module

import com.ycourtois.rankingparadise.configuration.RankingParadiseConfig
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ConfigurationModule(val configuration: RankingParadiseConfig) {
    @Provides
    @Singleton
    fun provideDynamoDBConfiguration(): RankingParadiseConfig.DynamoDBConfig = configuration.dynamoDBConfig
}