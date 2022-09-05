import io.github.cdimascio.dotenv.dotenv

object Vault {
    val discordBotToken = dotenv.get("TRANSL_DISCORD_BOT_TOKEN") ?: throw EnvError("TRANSL_DISCORD_BOT_TOKEN")

    val deeplAuthKey = dotenv.get("TRANSL_DEEPL_AUTH_KEY") ?: throw EnvError("TRANSL_DEEPL_AUTH_KEY")

    val testGuildId = dotenv.get("TRANSL_TEST_GUILD").toLongOrNull()
}

class EnvError(name: String) : Error("Variable '$name' could not be loaded.")


private val dotenv = dotenv()
