package discord

import dev.kord.common.entity.Permissions
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.interaction.ChatInputCommandInteraction

abstract class ChatInputCommand : Command<ChatInputCommandInteraction>() {
    override suspend fun makeGlobalCommand(kord: Kord) = kord.createGlobalChatInputCommand(
        name,
        description
    ) {
        options = params.map {it.option}.toMutableList()

        defaultMemberPermissions = Permissions(permissions)
    }

    override suspend fun makeGuildCommand(kord: Kord, guildId: Long) = kord.createGuildChatInputCommand(
        Snowflake(guildId),
        name,
        description
    ) {
        options = params.map {it.option}.toMutableList()

        defaultMemberPermissions = Permissions(permissions)
    }
}
