package com.parserdev.swapiapp.data.storage.remote.retrofit

import com.parserdev.swapiapp.data.storage.remote.retrofit.api.SWAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitSWInstance {

    val api: SWAPI by lazy {
        Retrofit.Builder()
            .baseUrl("https://swapi.dev")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SWAPI::class.java)
    }

}