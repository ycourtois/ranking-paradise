package com.ycourtois.rankingparadise.resource.dto

import javax.validation.constraints.Min
import javax.validation.constraints.NotNull


data class PlayerUpdateRequest(
    @field:NotNull
    @field:Min(0)
    val score: Long?
)