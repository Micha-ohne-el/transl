import io.github.cdimascio.dotenv.dotenv

object Vault {
    val discordBotToken = dotenv.get("TRANSL_DISCORD_BOT_TOKEN") ?: throw EnvError("TRANSL_DISCORD_BOT_TOKEN")

    val deeplAuthKey = dotenv.get("TRANSL_DEEPL_AUTH_KEY") ?: throw EnvError("TRANSL_DEEPL_AUTH_KEY")

    val testGuildId = dotenv.get("TRANSL_TEST_GUILD")?.toLongOrNull()

    val dbHost = dotenv.get("TRANSL_DB_HOST") ?: throw EnvError("TRANSL_DB_HOST")
    val dbName = dotenv.get("TRANSL_DB_NAME") ?: throw EnvError("TRANSL_DB_NAME")
    val dbUsername = dotenv.get("TRANSL_DB_USERNAME") ?: throw EnvError("TRANSL_DB_USERNAME")
    val dbPassword = dotenv.get("TRANSL_DB_PASSWORD") ?: throw EnvError("TRANSL_DB_PASSWORD")

    val translationCacheSize = dotenv.get("TRANSL_CACHE_SIZE")?.toIntOrNull() ?: 10000
}

class EnvError(name: String) : Error("Variable '$name' could not be loaded.")


private val dotenv = dotenv()
