package com.ycourtois.rankingparadise.resource

import com.ycourtois.rankingparadise.db.helper.DynamoDBHelper
import com.ycourtois.rankingparadise.di.component.RankingParadiseComponentIT
import com.ycourtois.rankingparadise.model.Player
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import javax.inject.Inject
import javax.ws.rs.client.Client
import javax.ws.rs.core.Response

@ExtendWith(DropwizardExtensionsSupport::class)
class TournamentResourceIT : AbstractResourceIT<Player>(Player::class) {

    @Inject
    override lateinit var dynamoDBHelper: DynamoDBHelper<Player>

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
        baseEndpoint = "http://localhost:${dropwizardAppExtension.localPort}${RankingParadiseAPI.Tournament.BASE}"
    }

    override fun inject(component: RankingParadiseComponentIT) {
        component.injectTournamentResourceIT(this)
    }

    @Test
    fun `Listing players belonging to a tournament`() {
        // given
        val client: Client = dropwizardAppExtension.client()
        val tournamentId = "1"

        // when
        val response: Response = client.target(
            "$baseEndpoint${RankingParadiseAPI.Tournament.LIST_PLAYERS}".replace("{id}", tournamentId)
        )
            .request()
            .get()
        val players = response.readEntity(List::class.java)

        // then
        Assertions.assertThat(players).hasSize(initialEntities.size)
        Assertions.assertThat(response.status).isEqualTo(Response.Status.OK.statusCode)
    }
}