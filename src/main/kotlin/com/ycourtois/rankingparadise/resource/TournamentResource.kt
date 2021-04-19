package com.ycourtois.rankingparadise.resource

import com.ycourtois.rankingparadise.enum.DynamoDBQueryOrder
import com.ycourtois.rankingparadise.model.Player
import com.ycourtois.rankingparadise.service.PlayerService
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.validation.constraints.Min
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path(RankingParadiseAPI.Tournament.BASE)
@Produces(MediaType.APPLICATION_JSON)
class TournamentResource @Inject constructor(
    private val playerService: PlayerService
) {

    companion object {
        @JvmStatic
        val logger = LoggerFactory.getLogger(javaClass.enclosingClass)
    }

    /**
     * Return list of players (sorted by score) for a given tournament
     * @param id tournament
     * @param orderBy asc or desc to sort players by score
     */
    @GET
    @Path(RankingParadiseAPI.Tournament.LIST_PLAYERS)
    fun listPlayersByScore(
        @QueryParam("orderBy") orderBy: DynamoDBQueryOrder?,
        @Min(1) @PathParam("id") id: Int
    ): List<Player> {
        logger.debug("Listing players with tournament id = $id")
        return playerService.listPlayersOrderByScore(id, orderBy)
    }

    @DELETE
    @Path(RankingParadiseAPI.Tournament.DELETE)
    fun deletePlayers(@Min(1) @PathParam("id") id: Int): Response {
        logger.debug("Deleting players with tournament id = $id")
        playerService.deletePlayers(id)
        return Response.ok("Tournament ended").build()
    }
}