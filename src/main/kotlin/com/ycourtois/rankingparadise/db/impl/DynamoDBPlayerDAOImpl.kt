package com.ycourtois.rankingparadise.db.impl

import com.ycourtois.rankingparadise.configuration.RankingParadiseConfig
import com.ycourtois.rankingparadise.db.PlayerDAO
import com.ycourtois.rankingparadise.enum.DynamoDBQueryOrder
import com.ycourtois.rankingparadise.exception.PlayerAlreadyExistsException
import com.ycourtois.rankingparadise.model.Player
import software.amazon.awssdk.enhanced.dynamodb.*
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.Page
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException
import javax.inject.Inject


class DynamoDBPlayerDAOImpl @Inject constructor(
    dynamoDbEnhancedClient: DynamoDbEnhancedClient,
    private val dynamodbConfiguration: RankingParadiseConfig.DynamoDBConfig
) : PlayerDAO {

    private val tableName = dynamodbConfiguration.table
    private var playerTable: DynamoDbTable<Player> =
        dynamoDbEnhancedClient.table(tableName, TableSchema.fromBean(Player::class.java))

    override fun listPlayersOrderByScore(tournamentId: Int, orderBy: DynamoDBQueryOrder?): List<Player> {
        val playersByScore: DynamoDbIndex<Player> = playerTable.index(dynamodbConfiguration.gsi)

        val queryRequest = QueryEnhancedRequest.builder()
            .scanIndexForward(orderBy?.scanIndexForward ?: false)
            .limit(50) // hard limit to avoid too large response payload
            .queryConditional(
                QueryConditional.keyEqualTo(
                    Key.builder()
                        .partitionValue(tournamentId)
                        .build()
                )
            )
            .build()

        val iterator: Iterator<Page<Player>> = playersByScore.query(queryRequest).iterator()
        val players: MutableList<Player> = mutableListOf()
        iterator.forEach { page ->
            players.addAll(page.items())
        }
        return players
    }

    override fun listPlayersOrderByScore(tournamentId: Int): List<Player> {
        return listPlayersOrderByScore(tournamentId, null)
    }

    override fun getPlayer(nickname: String): Player {
        val key = Key.builder().partitionValue(nickname).build()
        return playerTable.getItem(key)
            ?: throw ResourceNotFoundException.builder()
                .message("Player with nickname $nickname does not exist")
                .build()
    }

    override fun updatePlayer(player: Player): Player {
        // Check if player exists.
        // By default, dynamodb update operation will insert item if it does not exist
        getPlayer(player.nickname)
        return playerTable.updateItem(player)
    }

    override fun createPlayer(player: Player) {
        // Check if player does not exist.
        // By default, dynamodb put operation will update item if it does exist
        try {
            getPlayer(player.nickname)
            throw PlayerAlreadyExistsException(player.nickname)
        } catch (e: ResourceNotFoundException) {
            playerTable.putItem(player)
        }
    }

    override fun deletePlayer(nickname: String) {
        val key = Key.builder()
            .partitionValue(nickname)
            .build()
        val deleteRequest = DeleteItemEnhancedRequest.builder()
            .key(key)
            .build()
        playerTable.deleteItem(deleteRequest)
    }
}