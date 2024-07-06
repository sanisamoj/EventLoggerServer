package com.sanisamoj.utils.converters

import com.google.gson.Gson

class ObjectConverter {

    val gson = Gson()

    // Função para converter objeto em string
    inline fun <reified T> objectToString(obj: T): String {
        return gson.toJson(obj)
    }

    // Função para converter string em objeto
    inline fun <reified T> stringToObject(objectInString: String): T {
        return gson.fromJson(objectInString, T::class.java)
    }

}