package com.ycourtois.rankingparadise.resource.dto

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank


data class PlayerCreateRequest(
    @field:NotBlank
    val nickname: String,
    @field:Min(0)
    val score: Long = 0,
    @field:Min(1)
    @JsonProperty("tournament_id")
    val tournamentId: Int = 1
)