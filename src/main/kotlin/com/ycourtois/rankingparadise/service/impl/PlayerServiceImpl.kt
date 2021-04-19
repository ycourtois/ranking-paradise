package com.ycourtois.rankingparadise.service.impl

import com.ycourtois.rankingparadise.db.PlayerDAO
import com.ycourtois.rankingparadise.enum.DynamoDBQueryOrder
import com.ycourtois.rankingparadise.mapper.PlayerMapper
import com.ycourtois.rankingparadise.model.Player
import com.ycourtois.rankingparadise.resource.dto.PlayerCreateRequest
import com.ycourtois.rankingparadise.resource.dto.PlayerUpdateRequest
import com.ycourtois.rankingparadise.service.PlayerService
import javax.inject.Inject

class PlayerServiceImpl @Inject constructor(
    private val playerDAO: PlayerDAO,
    private val mapper: PlayerMapper
) : PlayerService {

    override fun addPlayer(playerCreateRequest: PlayerCreateRequest): Player {
        val player = mapper.toPlayer(playerCreateRequest)
        playerDAO.createPlayer(player)
        return computeRanking(player)
    }

    override fun updatePlayer(nickname: String, playerUpdateRequest: PlayerUpdateRequest): Player {
        val currentPlayer = playerDAO.getPlayer(nickname)
        val playerToUpdate = mapper.updatePlayer(nickname, playerUpdateRequest, currentPlayer)
        val playerUpdated = playerDAO.updatePlayer(playerToUpdate)
        return computeRanking(playerUpdated)
    }

    override fun getPlayerInformation(nickname: String): Player {
        val player = playerDAO.getPlayer(nickname)
        return computeRanking(player)
    }

    override fun listPlayersOrderByScore(tournamentId: Int, orderBy: DynamoDBQueryOrder?): List<Player> {
        val listPlayersOrderByScore = playerDAO.listPlayersOrderByScore(tournamentId, orderBy)
        return listPlayersOrderByScore
            .mapIndexed { index, player ->
                player.ranking =
                    if (orderBy == DynamoDBQueryOrder.DESC) (index + 1) else (listPlayersOrderByScore.size - index)
                player
            }
    }

    override fun deletePlayers(tournamentId: Int) {
        playerDAO.listPlayersOrderByScore(tournamentId)
            .forEach { player ->
                playerDAO.deletePlayer(player.nickname)
            }
    }


    /* Cannot find a proper and efficient way to compute ranking when querying database.
       This solution is clearly not viable with a large amount of items.  */
    private fun computeRanking(player: Player): Player {
        val listPlayersByScore = listPlayersOrderByScore(player.tournamentId, DynamoDBQueryOrder.DESC)
        val ranking = listPlayersByScore.indexOf(player)
        player.ranking = ranking + 1
        return player
    }
}