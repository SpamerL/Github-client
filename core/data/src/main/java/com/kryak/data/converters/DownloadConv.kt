package com.kryak.data.converters // ktlint-disable filename

import com.kryak.database.model.Download
import com.kryak.model.DownloadModel

fun Download.asExternalModel() = DownloadModel(
    id = id,
    repositoryName = repositoryName,
    userName = owner
)
