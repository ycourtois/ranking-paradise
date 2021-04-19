package com.ycourtois.rankingparadise.di.module

import com.ycourtois.rankingparadise.configuration.RankingParadiseConfig
import com.ycourtois.rankingparadise.db.PlayerDAO
import com.ycourtois.rankingparadise.db.impl.DynamoDBPlayerDAOImpl
import dagger.Module
import dagger.Provides
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import javax.inject.Singleton

@Module(includes = [DynamoDBModule::class, ConfigurationModule::class])
class DaoModule {

    @Provides
    @Singleton
    fun providePlayerDAO(
        dynamoDbEnhancedClient: DynamoDbEnhancedClient,
        dynamodbConfiguration: RankingParadiseConfig.DynamoDBConfig
    ): PlayerDAO =
        DynamoDBPlayerDAOImpl(dynamoDbEnhancedClient, dynamodbConfiguration)
}