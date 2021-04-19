package com.ycourtois.rankingparadise.exception

class PlayerAlreadyExistsException(nickname: String) : Exception("Player $nickname already exist")