package discord

import dev.kord.common.entity.Permissions
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.interaction.UserCommandInteraction

abstract class UserCommand : Command<UserCommandInteraction>() {
    override suspend fun makeGlobalCommand(kord: Kord) = kord.createGlobalUserCommand(
        name
    ) {
        defaultMemberPermissions = Permissions(permissions)

        localizeName(this@UserCommand.name)
    }

    override suspend fun makeGuildCommand(kord: Kord, guildId: Long) = kord.createGuildUserCommand(
        Snowflake(guildId),
        name
    ) {
        defaultMemberPermissions = Permissions(permissions)

        localizeName(this@UserCommand.name)
    }
}
