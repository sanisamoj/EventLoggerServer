package com.sanisamoj.database.redis

import com.sanisamoj.data.models.enums.Errors
import com.sanisamoj.utils.converters.ObjectConverter
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import redis.clients.jedis.JedisPool
import redis.clients.jedis.exceptions.JedisConnectionException
import java.util.concurrent.TimeUnit

object Redis {
    lateinit var jedisPool: JedisPool

    fun initialize() {
        jedisPool = JedisPool("localhost", 6379)
        var isConnected = isConnected()
        if (!isConnected) {
            while (!isConnected) {
                isConnected = isConnected()
                //if (!isConnected) println("A new attempt will be made to reconnect to redis in 30s.")
                runBlocking { delay(TimeUnit.SECONDS.toMillis(30)) }
            }
        }

        println("You successfully connected to Redis!")
    }

    private fun isConnected(): Boolean {
        return try {
            jedisPool.resource.use { jedis ->
                //jedis.auth(password)
                jedis.ping()
            }
            true
        } catch (e: JedisConnectionException) {
            false
        }
    }

    fun set(identification: DataIdentificationRedis, value: String) {
        val key = identification.key
        val collection = identification.collection.name

        try {
            jedisPool.resource.use { jedis -> jedis.set("$key:${collection}", value) }

        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun setWithTimeToLive(identification: DataIdentificationRedis, value: String, time: Long) {
        val key = identification.key
        val collection = identification.collection.name

        try {
            jedisPool.resource.use { jedis -> jedis.setex("$key:${collection}", time, value) }

        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun get(identification: DataIdentificationRedis): String? {
        val key = identification.key
        val collection = identification.collection.name

        return try {
            jedisPool.resource.use { jedis -> jedis["$key:${collection}"] }
        } catch (e: JedisConnectionException) {
            throw Exception(Errors.RedisNotResponding.description)
        }
    }

    fun setObject(identification: DataIdentificationRedis, value: Any) {
        val key = identification.key
        val collection = identification.collection.name
        val valueInString = ObjectConverter().objectToString<Any>(value)

        try {
            jedisPool.resource.use { jedis -> jedis.set("$key:${collection}", valueInString) }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun setObjectWithTimeToLive(identification: DataIdentificationRedis, value: Any, time: Long) {
        val key = identification.key
        val collection = identification.collection.name
        val valueInString = ObjectConverter().objectToString<Any>(value)

        try {
            jedisPool.resource.use { jedis -> jedis.setex("$key:${collection}", time, valueInString) }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    inline fun <reified T> getObject(identification: DataIdentificationRedis): T? {
        val key = identification.key
        val collection = identification.collection.name

        return try {
            val valueInString = jedisPool.resource.use { jedis -> jedis["$key:${collection}"] } ?: return null
            val stringInObject = ObjectConverter().stringToObject<T>(valueInString)
            stringInObject

        } catch (e: JedisConnectionException) {
            throw Exception(Errors.RedisNotResponding.description)
        }
    }

    fun delete(identification: DataIdentificationRedis) {
        val key = identification.key
        val collection = identification.collection.name

        try {
            jedisPool.resource.use { jedis -> jedis.del("$key:${collection}") }
        } catch (e: Throwable) {
            println(e.message)
            throw Exception(Errors.RedisNotResponding.description)
        }

    }

    fun flushAll() {
        try {
            jedisPool.resource.use { jedis -> jedis.flushAll() }
            println("Todos os dados do Redis foram apagados.")

        } catch (e: Throwable) {
            println(e.message)
            throw Exception(Errors.RedisNotResponding.description)
        }
    }
}