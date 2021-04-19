package com.ycourtois.rankingparadise.service

import com.ycourtois.rankingparadise.db.PlayerDAO
import com.ycourtois.rankingparadise.model.Player
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import javax.inject.Inject

class PlayerServiceTest : AbstractServiceTest() {

    lateinit var service: PlayerService

    @Inject
    lateinit var playerDAO: PlayerDAO

    @BeforeAll
    fun setup() {
        component.inject(this)
        service = component.playerService()
    }

    @Test
    fun `Getting player information from nickname`() {
        // given
        val nickname = "John"
        val score = 800L
        val playerReturned = Player(nickname, score)
        Mockito.doReturn(playerReturned).`when`(playerDAO).getPlayer(nickname)

        // when
        val player = service.getPlayerInformation(nickname)

        // then
        Assertions.assertThat(player.nickname).isEqualTo(nickname)
        Assertions.assertThat(player.score).isEqualTo(score)
    }
}