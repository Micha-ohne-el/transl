import dev.kord.core.Kord
import dev.kord.core.on
import discord.commands.GetGuildLang
import discord.commands.SetGuildLang
import discord.commands.Translate
import discord.commands.TranslateFrom
import discord.commands.TranslateFromTo
import discord.commands.TranslateMessage
import discord.commands.TranslateTo
import discord.completeSuggestions
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

    kord.on(kord, ::completeSuggestions)

    kord.login()
}

val commands = listOf(
    Translate,
    TranslateMessage,
    GetGuildLang,
    SetGuildLang,
    TranslateFrom,
    TranslateTo,
    TranslateFromTo
)
