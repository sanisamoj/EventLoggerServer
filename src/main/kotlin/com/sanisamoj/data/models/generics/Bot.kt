package com.sanisamoj.data.models.generics

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Bot(
    @BsonId
    val id: ObjectId,
    val name: String,
    val description: String,
    val number: String,
    val profileImage: String
)
