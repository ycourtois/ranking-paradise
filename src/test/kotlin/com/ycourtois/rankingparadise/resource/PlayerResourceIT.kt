package com.ycourtois.rankingparadise.resource

import com.ycourtois.rankingparadise.db.PlayerDAO
import com.ycourtois.rankingparadise.db.helper.DynamoDBHelper
import com.ycourtois.rankingparadise.di.component.RankingParadiseComponentIT
import com.ycourtois.rankingparadise.model.Player
import com.ycourtois.rankingparadise.resource.dto.PlayerCreateRequest
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import javax.inject.Inject
import javax.ws.rs.client.Client
import javax.ws.rs.client.Entity
import javax.ws.rs.core.Response

@ExtendWith(DropwizardExtensionsSupport::class)
class PlayerResourceIT : AbstractResourceIT<Player>(Player::class) {

    // wait for application to start before creating DynamoDBHelper instance
//    override val dynamoDBHelper: DynamoDBHelper<Player> by lazy {
//        DynamoDBHelper(dynamoDBClient, Player::class.java)
//    }

    @Inject
    override lateinit var dynamoDBHelper: DynamoDBHelper<Player>

    @Inject
    lateinit var playerDAO: PlayerDAO

    private lateinit var baseEndpoint: String

    override val initialEntities: Collection<Player>
        get() =
            listOf(
                Player("Nick", 400L),
                Player("John", 100L),
                Player("Mac", 300L),
                Player("Bruce", 350L),
                Player("Mike", 50L)
            )

    @BeforeAll
    override fun before() {
        super.before()
        baseEndpoint = "http://localhost:${dropwizardAppExtension.localPort}${RankingParadiseAPI.Player.BASE}"
    }

    override fun inject(component: RankingParadiseComponentIT) {
        component.injectPlayerResourceIT(this)
    }

    @Test
    fun `Getting player information from nickname`() {
        // given
        val client: Client = dropwizardAppExtension.client()
        val nickname = "John"

        // when
        val response: Response = client.target(
            "$baseEndpoint/${nickname}"
        )
            .request()
            .get()
        val player = response.readEntity(Player::class.java)

        // then
        Assertions.assertThat(player.nickname).isEqualTo(nickname)
        Assertions.assertThat(player.score).isEqualTo(100L)
        Assertions.assertThat(response.status).isEqualTo(Response.Status.OK.statusCode)
    }

    @Test
    fun `Adding a new player`() {
        // given
        val client: Client = dropwizardAppExtension.client()
        val playerCreateRequest = PlayerCreateRequest("Romeo", 42L, 5)


        // when
        val response: Response = client.target(
            "$baseEndpoint"
        )
            .request()
            .post(Entity.json(playerCreateRequest))

        // then
        // checking if player has been actually created
        val player = playerDAO.getPlayer(playerCreateRequest.nickname)

        Assertions.assertThat(player.nickname).isEqualTo(playerCreateRequest.nickname)
        Assertions.assertThat(player.score).isEqualTo(playerCreateRequest.score)
        Assertions.assertThat(player.tournamentId).isEqualTo(playerCreateRequest.tournamentId)
        Assertions.assertThat(response.status).isEqualTo(Response.Status.CREATED.statusCode)
    }
}