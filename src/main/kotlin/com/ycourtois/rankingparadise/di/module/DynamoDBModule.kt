package com.ycourtois.rankingparadise.di.module

import com.ycourtois.rankingparadise.configuration.RankingParadiseConfig
import com.ycourtois.rankingparadise.db.helper.DynamoDBHelper
import com.ycourtois.rankingparadise.model.Player
import dagger.Module
import dagger.Provides
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import javax.inject.Singleton

@Module(includes = [ConfigurationModule::class])
class DynamoDBModule {

    @Provides
    @Singleton
    fun provideDynamoDbClient(dynamodbConfiguration: RankingParadiseConfig.DynamoDBConfig): DynamoDbClient {
        return DynamoDBHelper.createDynamoDbClient(dynamodbConfiguration)
    }

    @Provides
    @Singleton
    fun provideDynamoDbEnhancedClient(dynamoDbClient: DynamoDbClient): DynamoDbEnhancedClient {
        return DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideDynamoDbHelper(dynamoDbClient: DynamoDbClient): DynamoDBHelper<Player> {
        return DynamoDBHelper(dynamoDbClient, Player::class.java)
    }
}