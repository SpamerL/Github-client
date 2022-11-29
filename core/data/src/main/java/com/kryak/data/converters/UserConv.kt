package com.kryak.data.converters // ktlint-disable filename

import com.kryak.model.UserModel
import com.kryak.network.model.userSearch.User

fun User.asExternalModel() = UserModel(
    id = id,
    name = login,
    image = avatarUrl
)
