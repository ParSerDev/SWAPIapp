package com.parserdev.swapiapp.data.storage.local

interface LocalStorage {

    fun saveAll()

    fun get(name: String)

}