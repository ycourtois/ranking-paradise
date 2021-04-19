package com.ycourtois.rankingparadise.service

import com.ycourtois.rankingparadise.enum.DynamoDBQueryOrder
import com.ycourtois.rankingparadise.model.Player
import com.ycourtois.rankingparadise.resource.dto.PlayerCreateRequest
import com.ycourtois.rankingparadise.resource.dto.PlayerUpdateRequest

interface PlayerService {

    /**
     * Add a player to tournament
     * @param playerCreateRequest player creation request
     * @return created [Player]
     */
    fun addPlayer(playerCreateRequest: PlayerCreateRequest): Player

    /**
     * Update player score
     * @param nickname player nickname to update
     * @param playerUpdateRequest: new player attributes to update
     * @return updated [Player]
     */
    fun updatePlayer(nickname: String, playerUpdateRequest: PlayerUpdateRequest): Player

    /**
     * Find a player by nickname and return additional information
     * @param nickname player nickname
     * @return [Player]
     */
    fun getPlayerInformation(nickname: String): Player

    /**
     * List players sorted by score (descending by default)
     * @param tournamentId tournament id
     * @param orderBy asc or desc to sort players by score
     * @return list of [Player]
     */
    fun listPlayersOrderByScore(tournamentId: Int, orderBy: DynamoDBQueryOrder?): List<Player>

    /**
     * Delete all players for a given tournament
     */
    fun deletePlayers(tournamentId: Int)
}