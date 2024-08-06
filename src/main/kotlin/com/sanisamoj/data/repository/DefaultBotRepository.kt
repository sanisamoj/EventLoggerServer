package com.sanisamoj.data.repository

import com.sanisamoj.config.GlobalContext
import com.sanisamoj.data.models.dataclass.*
import com.sanisamoj.data.models.interfaces.BotRepository
import com.sanisamoj.data.models.interfaces.BotsApiService
import com.sanisamoj.data.models.interfaces.DatabaseRepository
import com.sanisamoj.utils.analyzers.dotEnv
import org.bson.types.ObjectId

class DefaultBotRepository(
    private val databaseRepository: DatabaseRepository = GlobalContext.databaseRepository,
    private val apiService: BotsApiService
) : BotRepository {
    private val username: String = dotEnv("NY_USERNAME")
    private val password: String = dotEnv("NY_PASSWORD")
    private val adminPhone: String = dotEnv("SUPERADMIN_PHONE")
    private var token: String = ""

    override suspend fun createBot(): Bot {
        val botAlreadyExist: List<Bot> = databaseRepository.getAllBots()

        if (botAlreadyExist.isEmpty()) {
            val createBotRequest = CreateBotRequest(
                name = "EventLogger",
                description = GlobalContext.warningMessagesToChat.botDescription,
                profileImage = dotEnv("BOT_IMAGE_URL"),
                admins = listOf(adminPhone)
            )

            updateToken()
            val botResponse: BotResponse = apiService.createBot(createBotRequest, token)

            val bot = Bot(
                id = ObjectId(botResponse.id),
                name = botResponse.name,
                description = botResponse.description,
                number = botResponse.number,
                profileImageUrl = botResponse.profileImageUrl,
                groupsId = botResponse.groups.map { it.id }
            )

            databaseRepository.registerBot(bot)
            return bot
        }

        return botAlreadyExist[0]
    }

    override suspend fun sendMessage(message: String, phone: String) {
        val bot: Bot = getBot()
        val botSendMessageRequest = BotSendMessageRequest(phone, message)
        apiService.sendMessage(bot.id.toString(), botSendMessageRequest, token)
    }

    override suspend fun getBot(): Bot {
        return getAllBots()[0]
    }

    private suspend fun getAllBots(): List<Bot> {
        return databaseRepository.getAllBots()
    }

    private suspend fun updateToken() {
        val botServiceLoginRequest = BotServiceLoginRequest(username, password)
        val authenticationToken: String = apiService.login(botServiceLoginRequest).token
        token = "Bearer $authenticationToken"
    }
}