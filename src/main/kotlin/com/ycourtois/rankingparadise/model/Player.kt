package com.ycourtois.rankingparadise.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*

@DynamoDbBean
data class Player(
    @get:DynamoDbPartitionKey
    var nickname: String = "",
    @get:DynamoDbSecondaryPartitionKey(indexNames = ["score_index"])
    @get:DynamoDbAttribute(value = "tournament_id")
    @JsonProperty("tournament_id")
    var tournamentId: Int = 1
) {
    @get:DynamoDbSecondarySortKey(indexNames = ["score_index"])
    var score: Long = 0

    @get:DynamoDbIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var ranking: Int? = null

    constructor(nickname: String, score: Long) : this(nickname, 1) {
        this.score = score
    }
}