package com.ycourtois.rankingparadise.db.helper

import com.ycourtois.rankingparadise.configuration.RankingParadiseConfig
import org.slf4j.LoggerFactory
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.core.waiters.WaiterResponse
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.enhanced.dynamodb.model.CreateTableEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.EnhancedGlobalSecondaryIndex
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.*
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter
import java.net.URI


class DynamoDBHelper<T>(
    private val dynamoDbClient: DynamoDbClient,
    private val tableClass: Class<T>
) {

    companion object {
        @JvmStatic
        val logger = LoggerFactory.getLogger(javaClass.enclosingClass)

        fun createDynamoDbClient(dynamodbConfiguration: RankingParadiseConfig.DynamoDBConfig): DynamoDbClient {
            val builder = DynamoDbClient.builder()
                .region(Region.of(dynamodbConfiguration.aws.region))
                .credentialsProvider(
                    ProfileCredentialsProvider.builder()
                        .profileName(dynamodbConfiguration.aws.profile)
                        .build()
                )
            if (!dynamodbConfiguration.aws.endpoint.isNullOrBlank()) {
                builder.endpointOverride(URI.create(dynamodbConfiguration.aws.endpoint))
            }
            return builder.build()
        }
    }

    private val dynamoDbEnhancedClient: DynamoDbEnhancedClient = DynamoDbEnhancedClient.builder()
        .dynamoDbClient(dynamoDbClient)
        .build()

    fun addItem(tableName: String, item: T) {
        val enhancedTable = getEnhancedTable(tableName)
        enhancedTable.putItem(item)
    }

    fun describeTable(tableName: String): TableDescription {
        val request = DescribeTableRequest.builder()
            .tableName(tableName)
            .build()
        return dynamoDbClient.describeTable(request).table()
    }

    private fun getEnhancedTable(tableName: String): DynamoDbTable<T> {
        return dynamoDbEnhancedClient.table(tableName, TableSchema.fromClass(tableClass))
    }

    fun deleteTable(tableName: String) {
        val request = DeleteTableRequest.builder()
            .tableName(tableName)
            .build()
        val dbWaiter: DynamoDbWaiter = dynamoDbClient.waiter()
        dynamoDbClient.deleteTable(request)
        waitForRequest(tableName, dbWaiter::waitUntilTableNotExists)
        logger.info("$tableName table has been deleted.")
    }

    fun createTable(tableName: String, gsi: String) {
        logger.info("Creating dynamodb table $tableName ...")

        val globalSecondaryIndex = EnhancedGlobalSecondaryIndex.builder()
            .indexName(gsi)
            .projection(
                Projection.builder().projectionType(ProjectionType.ALL).build()
            )
            .build()
        val request: CreateTableEnhancedRequest = CreateTableEnhancedRequest.builder()
            .globalSecondaryIndices(globalSecondaryIndex)
            .build()

        val dbWaiter: DynamoDbWaiter = dynamoDbClient.waiter()
        try {
            val dynamoDbTable: DynamoDbTable<T> = getEnhancedTable(tableName)
            dynamoDbTable.createTable(request)
            waitForRequest(tableName, dbWaiter::waitUntilTableExists)
            logger.info("$tableName table has been created.")
        } catch (dde: DynamoDbException) {
            when (dde) {
                is ResourceInUseException, is TableAlreadyExistsException -> {
                    logger.warn(dde.message)
                }
                else -> throw dde
            }
        }
    }

    private fun waitForRequest(
        tableName: String,
        waiterFunction: (request: DescribeTableRequest) -> WaiterResponse<DescribeTableResponse>
    ) {
        val tableRequest = DescribeTableRequest.builder()
            .tableName(tableName)
            .build()

        // Wait for request to be fully handled
        val waiterResponse = waiterFunction(tableRequest)
        waiterResponse.matched().response()
            .ifPresent { r ->
                logger.info(r.toString())
            }
    }
}

