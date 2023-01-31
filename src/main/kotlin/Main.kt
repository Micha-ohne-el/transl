import dev.kord.core.Kord
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.guild.GuildCreateEvent
import dev.kord.core.on
import discord.commands.GetGuildLang
import discord.commands.SetGuildLang
import discord.commands.Translate
import discord.commands.TranslateForMe
import discord.completeSuggestions
import discord.executeCommand
import discord.registerCommandsForGuild
import discord.registerCommandsGlobally
import io.ktor.http.quote
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val log: Logger = LoggerFactory.getLogger("Main")

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

    kord.on<GuildCreateEvent> {
        log.info("TransL was added to a guild: ${guild.name.quote()} (${guild.memberCount} members)")
    }

    kord.on<ReadyEvent> {
        log.info("TransL is part of ${guildIds.size} guilds!")
    }

    kord.login()
}

val commands = listOf(
    Translate,
    TranslateForMe,
    GetGuildLang,
    SetGuildLang
)
