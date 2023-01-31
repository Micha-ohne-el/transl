package discord

import dev.kord.core.event.interaction.AutoCompleteInteractionCreateEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import util.infoTime

private val log: Logger = LoggerFactory.getLogger("Completions")

suspend fun completeSuggestions(event: AutoCompleteInteractionCreateEvent) {
    val discordCommand = registeredCommands.keys.find {it.id == event.interaction.command.data.id.value}

    val command = registeredCommands[discordCommand] ?: return

    val optionName = event.interaction.command.options.entries.single {it.value.focused}.key

    val param = command.params.single {it.name == optionName}

    log.infoTime(
        "Suggesting completions for command '${command.name}' and param '${param.name}' for query '${event.interaction.focusedOption.value}'...",
        "Done."
    ) {
        param.suggest(event.interaction)
    }
}
