package discord

import commands
import dev.kord.core.Kord
import dev.kord.core.entity.application.ApplicationCommand
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import dev.kord.core.event.interaction.ApplicationCommandInteractionCreateEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import util.infoTime

val log: Logger = LoggerFactory.getLogger("Registering")

suspend fun registerCommandsGlobally(kord: Kord) {
    log.infoTime(
        "Registering all commands globally...",
        "Successfully registered all commands globally."
    ) {
        for (command in commands) {
            log.infoTime(
                "Registering '${command.name}' globally...",
                "Successfully registered '${command.name}' globally."
            ) {
                val discordCommand = command.makeGlobalCommand(kord)

                registeredCommands[discordCommand] = command

                log.info("Finished registering '${command.name}' globally.")
            }
        }
    }
}

suspend fun registerCommandsForGuild(kord: Kord, guildId: Long) {
    log.infoTime(
        "Registering all commands for guild $guildId...",
        "Successfully registered all commands for guild $guildId."
    ) {
        for (command in commands) {
            log.infoTime(
                "Registering '${command.name}' for guild $guildId...",
                "Successfully registered '${command.name}' for guild $guildId."
            ) {
                val discordCommand = command.makeGuildCommand(kord, guildId)

                registeredCommands[discordCommand] = command

                log.info("Finished registering '${command.name}' for guild $guildId.")
            }
        }
    }
}

suspend fun executeCommand(event: ApplicationCommandInteractionCreateEvent) {
    val discordCommand = registeredCommands.keys.find { it.id == event.interaction.invokedCommandId }

    val command = registeredCommands[discordCommand]

    try {
        @Suppress("UNCHECKED_CAST") // This is supposed to throw when the cast is unsuccessful.
        command as Command<in ApplicationCommandInteraction>?

        log.infoTime(
            "Executing command '${command?.name}'...",
            "Successfully executed command '${command?.name}'."
        ) {
            command?.interaction = event.interaction
            command?.execute()
        }
    } catch (error: Throwable) {
        log.error("An error occurred while executing command '${command?.name}'!")
        log.error(error.stackTraceToString())
    }
}


private val registeredCommands = mutableMapOf<ApplicationCommand, Command<out ApplicationCommandInteraction>>()
