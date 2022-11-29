package com.kryak.model

data class RepoModel(
    val id: Int,
    val name: String,
    val language: String?,
    val star: Int,
    val url: String,
    val owner: String
)

