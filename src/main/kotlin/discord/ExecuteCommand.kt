package discord

import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import dev.kord.core.event.interaction.ApplicationCommandInteractionCreateEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import util.infoTime

private val log: Logger = LoggerFactory.getLogger("Executing")

suspend fun executeCommand(event: ApplicationCommandInteractionCreateEvent) {
    val discordCommand = registeredCommands.keys.find {it.id == event.interaction.invokedCommandId}

    val command = registeredCommands[discordCommand] ?: return

    try {
        @Suppress("UNCHECKED_CAST") // This is supposed to throw when the cast is unsuccessful.
        command as Command<in ApplicationCommandInteraction>

        log.infoTime(
            "Executing command '${command.name}'...",
            "Successfully executed command '${command.name}'."
        ) {
            command.interaction = event.interaction
            command.execute()
        }
    } catch (error: Throwable) {
        log.error("An error occurred while executing command '${command.name}'!")
        log.error(error.stackTraceToString())
    }
}
