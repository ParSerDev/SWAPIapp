package com.parserdev.swapiapp.data.network.retrofit

import com.parserdev.swapiapp.data.network.NetworkInstance
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RetrofitInstance @Inject constructor() : NetworkInstance {

    override val api: LibraryService by lazy {
        Retrofit.Builder()
            .baseUrl("https://swapi.dev/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LibraryService::class.java)
    }

}