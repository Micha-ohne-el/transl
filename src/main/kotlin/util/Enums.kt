package util

inline fun <reified T : Enum<T>> enumValueOfOrNull(identifier: String): T? {
    return try {
        enumValueOf<T>(identifier)
    } catch (e: IllegalArgumentException) {
        null
    }
}
