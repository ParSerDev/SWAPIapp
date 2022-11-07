package com.parserdev.swapiapp.data.dto

import com.google.gson.annotations.SerializedName

data class PageDto<T>(
    @field:SerializedName("next")
    val next: String?,
    @field:SerializedName("previous")
    val previous: String?,
    @field:SerializedName("count")
    val count: Int,
    @field:SerializedName("results")
    val results: List<T>
)