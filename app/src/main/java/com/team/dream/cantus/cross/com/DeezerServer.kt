package com.team.dream.cantus.cross.com

object DeezerServer {

    fun makeDeezerService() = DeezerRetrofitBuilder.getDeezerRetrofit().create()
}