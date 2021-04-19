package com.ycourtois.rankingparadise.mapper

import com.ycourtois.rankingparadise.model.Player
import com.ycourtois.rankingparadise.resource.dto.PlayerCreateRequest
import com.ycourtois.rankingparadise.resource.dto.PlayerUpdateRequest
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget

@Mapper
interface PlayerMapper {

    fun toPlayer(playerCreateRequest: PlayerCreateRequest): Player

    //fun toPlayer(nickname: String, playerUpdateRequest: PlayerUpdateRequest): Player

    fun updatePlayer(nickname: String, playerUpdateRequest: PlayerUpdateRequest, @MappingTarget player: Player): Player
}