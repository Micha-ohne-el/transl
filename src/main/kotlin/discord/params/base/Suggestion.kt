package discord.params.base

data class Suggestion<T>(
    val name: String,
    val value: T
)
