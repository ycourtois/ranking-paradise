package com.ycourtois.rankingparadise.resource

interface RankingParadiseAPI {

    object Player {
        const val BASE = "/players"
        const val GET = "/{nickname}"
        const val UPDATE = "/{nickname}"
    }

    object Tournament {
        const val BASE = "/tournament"
        const val LIST_PLAYERS = "/{id}/players"
        const val DELETE = "/{id}"
    }
}