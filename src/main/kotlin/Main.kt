import dev.kord.core.Kord
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import dev.kord.core.on
import discord.Command
import discord.commands.GetGuildLang
import discord.commands.SetGuildLang
import discord.commands.Translate
import discord.commands.TranslateFrom
import discord.commands.TranslateMessage
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

val commands: List<Command<out ApplicationCommandInteraction>> = listOf(
    Translate,
    TranslateMessage,
    GetGuildLang,
    SetGuildLang,
    TranslateFrom
)
