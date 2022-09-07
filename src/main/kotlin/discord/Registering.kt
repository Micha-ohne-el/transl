package discord

import dev.kord.core.Kord
import dev.kord.core.entity.application.ApplicationCommand
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import dev.kord.core.event.interaction.ApplicationCommandInteractionCreateEvent
import discord.commands.Translate
import discord.commands.TranslateMessage

suspend fun registerCommandsGlobally(kord: Kord) {
    for (command in commands) {
        val discordCommand = command.makeGlobalCommand(kord)

        registeredCommands[discordCommand] = command
    }
}

suspend fun registerCommandsForGuild(kord: Kord, guildId: Long) {
    for (command in commands) {
        val discordCommand = command.makeGuildCommand(kord, guildId)

        registeredCommands[discordCommand] = command
    }
}

suspend fun executeCommand(event: ApplicationCommandInteractionCreateEvent) {
    val discordCommand = registeredCommands.keys.find { it.id == event.interaction.invokedCommandId }

    val command = registeredCommands[discordCommand]

    @Suppress("UNCHECKED_CAST") // This is supposed to throw when the cast is unsuccessful.
    command as Command<in ApplicationCommandInteraction>?

    command?.interaction = event.interaction
    command?.execute()
}


private val commands: List<Command<out ApplicationCommandInteraction>> = listOf(Translate, TranslateMessage)

private val registeredCommands = mutableMapOf<ApplicationCommand, Command<out ApplicationCommandInteraction>>()
