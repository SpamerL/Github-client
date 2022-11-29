package com.kryak.data.converters // ktlint-disable filename

import com.kryak.model.RepoModel
import com.kryak.network.model.repos.UserReposListItem

fun UserReposListItem.asExternalModel() = RepoModel(
    id = id,
    name = name,
    language = language,
    star = stargazersCount,
    url = htmlUrl,
    owner = owner.login
)
