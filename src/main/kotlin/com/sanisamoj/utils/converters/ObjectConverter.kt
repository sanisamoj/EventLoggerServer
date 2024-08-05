package com.sanisamoj.utils.converters

import com.google.gson.Gson
import kotlinx.serialization.json.Json
import java.io.InputStream

class ObjectConverter {
    val gson = Gson()

    inline fun <reified T> objectToString(obj: T): String {
        return gson.toJson(obj)
    }

    inline fun <reified T> stringToObject(objectInString: String): T {
        return gson.fromJson(objectInString, T::class.java)
    }

    inline fun <reified T> inputStreamToObject(inputStream: InputStream): T {
        val jsonString: String = inputStream.bufferedReader().use { it.readText() }
        return Json.decodeFromString<T>(jsonString)
    }

}