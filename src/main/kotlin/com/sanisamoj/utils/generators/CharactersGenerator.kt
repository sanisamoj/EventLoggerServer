package com.sanisamoj.utils.generators

import kotlin.random.Random

class CharactersGenerator {
    fun codeValidationGenerate(): Int {
        return Random.nextInt(100_000, 1_000_000)
    }

    fun randomCodeGenerate(length: Int): String {
        val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%&*?"
        val sb = StringBuilder(length)
        val random = Random.Default

        repeat(length) {
            val index = random.nextInt(characters.length)
            sb.append(characters[index])
        }

        return sb.toString()
    }

    fun generateWithNoSymbols(maxChat: Int = 36): String {
        val charPermited: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val characters: String = (1..maxChat).map{ charPermited.random() }.joinToString("")

        return characters
    }
}