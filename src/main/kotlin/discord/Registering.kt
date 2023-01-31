package discord

import commands
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.application.ApplicationCommand
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import util.infoTime

private val log: Logger = LoggerFactory.getLogger("Registering")

val registeredCommands = mutableMapOf<ApplicationCommand, Command<out ApplicationCommandInteraction>>()

suspend fun registerCommandsGlobally(kord: Kord) {
    log.infoTime(
        "Registering all commands globally...",
        "Successfully registered all commands globally."
    ) {
        log.infoTime(
            "Clearing out any existing commands...",
            "Successfully cleared out any existing commands."
        ) {
            kord.createGlobalApplicationCommands { }
        }

        for (command in commands) {
            log.infoTime(
                "Registering '${command.name}' globally...",
                "Successfully registered '${command.name}' globally."
            ) {
                val discordCommand = command.makeGlobalCommand(kord)

                registeredCommands[discordCommand] = command
            }
        }
    }
}

suspend fun registerCommandsForGuild(kord: Kord, guildId: Long) {
    log.infoTime(
        "Registering all commands for guild $guildId...",
        "Successfully registered all commands for guild $guildId."
    ) {
        log.infoTime(
            "Clearing out any existing commands...",
            "Successfully cleared out any existing commands."
        ) {
            kord.createGuildApplicationCommands(Snowflake(guildId)) { }
        }

        for (command in commands) {
            log.infoTime(
                "Registering '${command.name}' for guild $guildId...",
                "Successfully registered '${command.name}' for guild $guildId."
            ) {
                val discordCommand = command.makeGuildCommand(kord, guildId)

                registeredCommands[discordCommand] = command
            }
        }
    }
}
