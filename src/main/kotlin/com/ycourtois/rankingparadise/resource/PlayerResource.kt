package com.ycourtois.rankingparadise.resource

import com.ycourtois.rankingparadise.model.Player
import com.ycourtois.rankingparadise.resource.dto.PlayerCreateRequest
import com.ycourtois.rankingparadise.resource.dto.PlayerUpdateRequest
import com.ycourtois.rankingparadise.service.PlayerService
import org.slf4j.LoggerFactory
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException
import java.net.URI
import javax.inject.Inject
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path(RankingParadiseAPI.Player.BASE)
@Produces(MediaType.APPLICATION_JSON)
class PlayerResource @Inject constructor(
    private val playerService: PlayerService
) {

    companion object {
        @JvmStatic
        val logger = LoggerFactory.getLogger(javaClass.enclosingClass)
    }

    /**
     * Find a player by nickname and return additional information
     * @param nickname player nickname
     * @throws [ResourceNotFoundException] if not found
     */
    @GET
    @Path(RankingParadiseAPI.Player.GET)
    fun getPlayerInformation(@NotBlank @PathParam("nickname") nickname: String): Player {
        logger.debug("Getting player information for nickname $nickname")
        return playerService.getPlayerInformation(nickname)
    }

    /**
     * Add a player to tournament
     * @param playerCreateRequest player creation request
     */
    @POST
    fun addPlayer(@Valid playerCreateRequest: PlayerCreateRequest, @Context request: HttpServletRequest): Response {
        logger.debug("Adding player with payload [${playerCreateRequest}]")
        playerService.addPlayer(playerCreateRequest)
        return Response.created(
            URI(
                "${request.scheme}://${request.serverName}:${request.serverPort}/player/${playerCreateRequest.nickname}"
            )
        ).build()
    }

    /**
     * Update player information base on his nickname
     * @param nickname player nickname
     * @param playerUpdateRequest: new player attributes to update
     * @throws [ResourceNotFoundException] if not player does not exist
     */
    @PUT
    @Path(RankingParadiseAPI.Player.UPDATE)
    fun updatePlayer(
        @NotBlank @PathParam("nickname") nickname: String,
        @Valid playerUpdateRequest: PlayerUpdateRequest
    ): Player {
        logger.debug("Updating player $nickname with payload [${playerUpdateRequest}]")
        return playerService.updatePlayer(nickname, playerUpdateRequest)
    }
}