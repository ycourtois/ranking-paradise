package com.ycourtois.rankingparadise.resource

import com.ycourtois.rankingparadise.model.Player
import com.ycourtois.rankingparadise.service.PlayerService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import javax.inject.Inject

class PlayerResourceTest : AbstractResourceTest() {

    lateinit var resource: PlayerResource

    @Inject
    lateinit var playerService: PlayerService

    @BeforeAll
    fun setup() {
        component.inject(this)
        resource = component.playerResource()
    }

    @Test
    fun `Getting player information from nickname`() {
        // given
        val nickname = "John"
        val score = 800L
        val playerReturned = Player(nickname, score)
        Mockito.doReturn(playerReturned).`when`(playerService).getPlayerInformation(nickname)

        // when
        val player = resource.getPlayerInformation(nickname)

        // then
        Assertions.assertThat(player.nickname).isEqualTo(nickname)
        Assertions.assertThat(player.score).isEqualTo(score)
    }
}