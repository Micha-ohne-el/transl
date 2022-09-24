package discord

import dev.kord.common.entity.Permissions
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.interaction.MessageCommandInteraction

abstract class MessageCommand : Command<MessageCommandInteraction>() {
    override suspend fun makeGlobalCommand(kord: Kord) = kord.createGlobalMessageCommand(
        name
    ) {
        defaultMemberPermissions = Permissions(permissions)

        localizeName(this@MessageCommand.name)
    }

    override suspend fun makeGuildCommand(kord: Kord, guildId: Long) = kord.createGuildMessageCommand(
        Snowflake(guildId),
        name
    ) {
        defaultMemberPermissions = Permissions(permissions)

        localizeName(this@MessageCommand.name)
    }
}
