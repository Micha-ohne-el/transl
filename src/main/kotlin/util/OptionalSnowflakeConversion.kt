package util

import dev.kord.common.entity.optional.OptionalSnowflake

fun OptionalSnowflake.toLong() = value?.value?.toLong() ?: 0

fun OptionalSnowflake.toLongOrNull() = value?.value?.toLong()
