import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.event.interaction.GlobalChatInputCommandInteractionCreateEvent
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import discord.Command
import discord.commands.Translate

suspend fun main() {
    val kord = Kord(Vault.discordBotToken)

    val testGuildId = Vault.testGuildId

    if (testGuildId == null) {
        kord.registerCommandsGlobally()
    } else {
        kord.registerCommandsForGuild(testGuildId)
    }

    kord.login()
}


private val commands: List<Command> = listOf(Translate)

private suspend fun Kord.registerCommandsGlobally() {
    for (command in commands) {
        val discordCommand = createGlobalChatInputCommand(
            command.name,
            command.description
        ) {
            options = command.params.map {it.option}.toMutableList()
        }

        on<GlobalChatInputCommandInteractionCreateEvent> {
            if (interaction.invokedCommandId == discordCommand.id) {
                command.interaction = interaction
                command.execute()
            }
        }
    }
}

private suspend fun Kord.registerCommandsForGuild(guildId: Long) {
    for (command in commands) {
        val discordCommand = createGuildChatInputCommand(
            Snowflake(guildId),
            command.name,
            command.description
        ) {
            options = command.params.map {it.option}.toMutableList()
        }

        on<GuildChatInputCommandInteractionCreateEvent> {
            if (interaction.invokedCommandId == discordCommand.id) {
                command.interaction = interaction
                command.execute()
            }
        }
    }
}
