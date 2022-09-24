package discord

import dev.kord.common.entity.Permissions
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import util.toSnakeCase

abstract class ChatInputCommand : Command<ChatInputCommandInteraction>() {
    abstract val description: String

    override suspend fun makeGlobalCommand(kord: Kord) = kord.createGlobalChatInputCommand(
        name.toSnakeCase(),
        description
    ) {
        options = params.map {it.option}.toMutableList()

        defaultMemberPermissions = Permissions(permissions)

        localizeName(this@ChatInputCommand.name) {it.toSnakeCase().sanitize()}
        localizeDescription(this@ChatInputCommand.description)
    }

    override suspend fun makeGuildCommand(kord: Kord, guildId: Long) = kord.createGuildChatInputCommand(
        Snowflake(guildId),
        name.toSnakeCase(),
        description
    ) {
        options = params.map {it.option}.toMutableList()

        defaultMemberPermissions = Permissions(permissions)

        localizeName(this@ChatInputCommand.name) {it.toSnakeCase().sanitize()}
        localizeDescription(this@ChatInputCommand.description)
    }
}
