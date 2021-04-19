package com.ycourtois.rankingparadise.configuration

import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration

data class RankingParadiseConfig(
    @JsonProperty("dynamodb")
    val dynamoDBConfig: DynamoDBConfig
) : Configuration() {

    data class DynamoDBConfig(
        val table: String,
        val primaryKey: String,
        val gsi: String,
        val aws: AWS
    ) {
        data class AWS(
            val region: String,
            val profile: String,
            val endpoint: String?
        )
    }
}