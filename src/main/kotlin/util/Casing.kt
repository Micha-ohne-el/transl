package util

fun String.toSnakeCase(): String = Regex("[A-Z]").replace(this) { match ->
    "_" + match.value.lowercase()
}
