package com.ycourtois.rankingparadise.enum

import com.fasterxml.jackson.annotation.JsonValue

enum class DynamoDBQueryOrder(
    @get:JsonValue
    private val orderBy: String,
    val scanIndexForward: Boolean
) {

    ASC("asc", true),
    DESC("desc", false);
}