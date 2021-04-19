package com.ycourtois.rankingparadise.resource

import cloud.localstack.docker.LocalstackDockerExtension
import cloud.localstack.docker.annotation.IEnvironmentVariableProvider
import cloud.localstack.docker.annotation.LocalstackDockerProperties
import com.ycourtois.rankingparadise.RankingParadiseApp
import com.ycourtois.rankingparadise.configuration.RankingParadiseConfig
import com.ycourtois.rankingparadise.db.helper.DynamoDBHelper
import com.ycourtois.rankingparadise.di.component.DaggerRankingParadiseComponentIT
import com.ycourtois.rankingparadise.di.component.RankingParadiseComponentIT
import com.ycourtois.rankingparadise.di.module.ConfigurationModule
import com.ycourtois.rankingparadise.di.module.DynamoDBModule
import io.dropwizard.testing.junit5.DropwizardAppExtension
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(LocalstackDockerExtension::class)
@LocalstackDockerProperties(
    services = ["dynamodb"], imageTag = "0.12.9.1",
    environmentVariableProvider = AbstractResourceIT.EnvironmentVariablesProvider::class
)
abstract class AbstractResourceIT<T : Any>(private val entityClass: KClass<T>) {

    companion object {
        @JvmStatic
        val logger = LoggerFactory.getLogger(javaClass.enclosingClass)

        @JvmStatic
        protected val dropwizardAppExtension: DropwizardAppExtension<RankingParadiseConfig> = DropwizardAppExtension(
            RankingParadiseApp::class.java, "dw-config-test.yml"
        )
    }

    protected abstract fun inject(component: RankingParadiseComponentIT)
    protected abstract val dynamoDBHelper: DynamoDBHelper<T>
    protected abstract val initialEntities: Collection<T>

    @BeforeAll
    open fun before() {
        val dynamoDBConfig = dropwizardAppExtension.configuration.dynamoDBConfig
        val component = DaggerRankingParadiseComponentIT.builder()
            .dynamoDBModule(DynamoDBModule())
            .configurationModule(ConfigurationModule(dropwizardAppExtension.configuration))
            .build()

        inject(component)

        dynamoDBHelper.createTable(dynamoDBConfig.table, dynamoDBConfig.gsi)
        addEntities(dynamoDBConfig.table)
    }

//    protected val dynamoDBClient: DynamoDbClient by lazy {
//        DynamoDBHelper.createDynamoDbClient(dropwizardAppExtension.configuration.dynamoDBConfig)
//    }

    private fun addEntities(tableName: String) {
        logger.info("Initializing table with entities ...")
        initialEntities.forEach { entity -> dynamoDBHelper.addItem(tableName, entity) }
    }

    class EnvironmentVariablesProvider : IEnvironmentVariableProvider {
        override fun getEnvironmentVariables(): MutableMap<String, String> {
            return mutableMapOf("DEFAULT_REGION" to "eu-west-1")
        }
    }
}