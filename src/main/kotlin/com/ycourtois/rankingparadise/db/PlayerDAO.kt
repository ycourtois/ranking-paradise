package com.ycourtois.rankingparadise.db

import com.ycourtois.rankingparadise.enum.DynamoDBQueryOrder
import com.ycourtois.rankingparadise.model.Player
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException

interface PlayerDAO {

    /**
     * List players sorted by score (descending by default)
     * @param tournamentId tournament id
     * @param orderBy asc or desc to sort players by score
     */
    fun listPlayersOrderByScore(tournamentId: Int, orderBy: DynamoDBQueryOrder?): List<Player>

    /**
     * List players sorted by score (descending by default)
     * @param tournamentId tournament id
     */
    fun listPlayersOrderByScore(tournamentId: Int): List<Player>

    /**
     * Find a player by nickname
     * @param nickname player nickname$
     * @throws [ResourceNotFoundException] if not found
     */
    fun getPlayer(nickname: String): Player

    /**
     * Update player information
     * @param player player to update
     * @throws [ResourceNotFoundException] if player does not exist
     */
    fun updatePlayer(player: Player): Player

    /**
     * Create a player
     * @param player player to insert
     */
    fun createPlayer(player: Player)

    /**
     * Delete player for a given nickname
     */
    fun deletePlayer(nickname: String)
}