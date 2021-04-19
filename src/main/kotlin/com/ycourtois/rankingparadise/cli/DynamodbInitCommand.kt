package com.ycourtois.rankingparadise.cli

import com.ycourtois.rankingparadise.configuration.RankingParadiseConfig
import com.ycourtois.rankingparadise.db.helper.DynamoDBHelper
import com.ycourtois.rankingparadise.model.Player
import io.dropwizard.Application
import org.slf4j.LoggerFactory

class DynamodbInitCommand<T : RankingParadiseConfig>(
    application: Application<T>
) : DynamodbCommand<T>(application, "init-db", "Initialize database table") {

    companion object {
        @JvmStatic
        val logger = LoggerFactory.getLogger(javaClass.enclosingClass)
    }

    override fun performCommand(
        dynamoDbHelper: DynamoDBHelper<Player>,
        dynamoDBConfig: RankingParadiseConfig.DynamoDBConfig
    ) {
        dynamoDbHelper.createTable(dynamoDBConfig.table, dynamoDBConfig.gsi)
        addPlayers(dynamoDBConfig.table, dynamoDbHelper)
    }

    private fun addPlayers(tableName: String, dynamoDbHelper: DynamoDBHelper<Player>) {
        logger.info("Initializing table with some players ...")
        dynamoDbHelper.addItem(tableName, Player("Nick", 400L))
        dynamoDbHelper.addItem(tableName, Player("John", 100L))
        dynamoDbHelper.addItem(tableName, Player("Mac", 300L))
        dynamoDbHelper.addItem(tableName, Player("Bruce", 350L))
        dynamoDbHelper.addItem(tableName, Player("Mike", 50L))
    }
}