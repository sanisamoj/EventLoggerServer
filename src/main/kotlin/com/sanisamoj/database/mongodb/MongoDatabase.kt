package com.sanisamoj.database.mongodb

import com.mongodb.MongoException
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.github.cdimascio.dotenv.Dotenv
import org.bson.BsonInt64
import org.bson.Document

object MongoDatabase {
    private val dotenv : Dotenv = Dotenv.configure().ignoreIfMissing().load()
    private var database: MongoDatabase? = null
    private lateinit var client : MongoClient

    private val connectionString: String = dotenv["SERVER_URL"]
    private val nameDatabase : String = dotenv["NAME_DATABASE"]

    private suspend fun init() {
        client = MongoClient.create(connectionString)
        val db: MongoDatabase = client.getDatabase(nameDatabase)

        try {
            val command = Document("ping", BsonInt64(1))
            db.runCommand(command)
            println("You successfully connected to MongoDB!")
            database = db
        } catch (me: MongoException) {
            System.err.println(me)
        }
    }

    suspend fun initialize() { if (database == null) init() }
    
    suspend fun getDatabase(): MongoDatabase {
        if (database == null) init()
        return database!!
    }
}