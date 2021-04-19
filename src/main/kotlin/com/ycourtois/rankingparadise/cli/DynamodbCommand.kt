package com.ycourtois.rankingparadise.cli

import com.ycourtois.rankingparadise.configuration.RankingParadiseConfig
import com.ycourtois.rankingparadise.db.helper.DynamoDBHelper
import com.ycourtois.rankingparadise.model.Player
import io.dropwizard.Application
import io.dropwizard.cli.EnvironmentCommand
import io.dropwizard.setup.Environment
import net.sourceforge.argparse4j.inf.Namespace

abstract class DynamodbCommand<T : RankingParadiseConfig>(
    application: Application<T>,
    name: String,
    description: String
) :
    EnvironmentCommand<T>(application, name, description) {

    private var configurationClass: Class<T> = application.configurationClass

    override fun getConfigurationClass(): Class<T> {
        return configurationClass
    }

    protected abstract fun performCommand(dynamoDbHelper: DynamoDBHelper<Player>, dynamoDBConfig: RankingParadiseConfig.DynamoDBConfig)

    override fun run(environment: Environment?, namespace: Namespace?, configuration: T) {
        val dynamoDbClient = DynamoDBHelper.createDynamoDbClient(configuration.dynamoDBConfig)
        val dynamoDbHelper = DynamoDBHelper(dynamoDbClient, Player::class.java)
        performCommand(dynamoDbHelper, configuration.dynamoDBConfig)
    }
}