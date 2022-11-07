package com.parserdev.swapiapp.data.network

import com.parserdev.swapiapp.data.network.retrofit.LibraryService

interface NetworkInstance {
    val api: LibraryService
}