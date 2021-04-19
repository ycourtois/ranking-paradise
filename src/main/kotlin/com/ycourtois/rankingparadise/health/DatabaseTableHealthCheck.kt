package com.ycourtois.rankingparadise.health

import com.codahale.metrics.health.HealthCheck
import com.ycourtois.rankingparadise.configuration.RankingParadiseConfig
import com.ycourtois.rankingparadise.db.helper.DynamoDBHelper
import com.ycourtois.rankingparadise.model.Player
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException
import javax.inject.Inject

class DatabaseTableHealthCheck @Inject constructor(
    private val dynamodbHelper: DynamoDBHelper<Player>,
    private val dynamodbConfiguration: RankingParadiseConfig.DynamoDBConfig
) : HealthCheck() {
    override fun check(): Result {
        try {
            val describeTable = dynamodbHelper.describeTable(dynamodbConfiguration.table)
            describeTable.tableName()
        } catch (e: ResourceNotFoundException) {
            return Result.unhealthy(e)
        }
        return Result.healthy("Application is healthy. Player table is available.")
    }
}