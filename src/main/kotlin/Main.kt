import dev.kord.core.Kord
import dev.kord.core.on
import discord.executeCommand
import discord.registerCommandsForGuild
import discord.registerCommandsGlobally

suspend fun main() {
    val kord = Kord(Vault.discordBotToken)

    val testGuildId = Vault.testGuildId

    if (testGuildId == null) {
        registerCommandsGlobally(kord)
    } else {
        registerCommandsForGuild(kord, testGuildId)
    }

    kord.on(kord, ::executeCommand)

    kord.login()
}
