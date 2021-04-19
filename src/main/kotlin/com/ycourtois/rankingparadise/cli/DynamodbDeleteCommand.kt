package com.ycourtois.rankingparadise.cli

import com.ycourtois.rankingparadise.configuration.RankingParadiseConfig
import com.ycourtois.rankingparadise.db.helper.DynamoDBHelper
import com.ycourtois.rankingparadise.model.Player
import io.dropwizard.Application

class DynamodbDeleteCommand<T : RankingParadiseConfig>(
    application: Application<T>,
) : DynamodbCommand<T>(application, "delete-db", "Delete database table") {

    override fun performCommand(
        dynamoDbHelper: DynamoDBHelper<Player>,
        dynamoDBConfig: RankingParadiseConfig.DynamoDBConfig
    ) {
        dynamoDbHelper.deleteTable(dynamoDBConfig.table)
    }
}