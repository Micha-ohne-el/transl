package util

fun String.toSnakeCase() = splitWords(this).joinToSnakeCase()

fun String.toKebabCase() = splitWords(this).joinToKebabCase()

fun String.toCamelCase() = splitWords(this).joinToCamelCase()

fun String.toPascalCase() = splitWords(this).joinToPascalCase()

fun String.toSpaceCase() = splitWords(this).joinToSpaceCase()

fun String.toTitleSpaceCase() = splitWords(this).joinToTitleSpaceCase()


private fun splitWords(text: String): List<String> {
    return text.split(Regex("""\s+|_|-|(?<!^|\s|_|-)(?=[A-Z])""")).map(String::lowercase)
}

private fun List<String>.joinToSnakeCase() = joinToString("_")

private fun List<String>.joinToKebabCase() = joinToString("-")

private fun List<String>.joinToCamelCase() =
    get(0) + subList(1, size).joinToString(separator = "", transform = String::titlecase)

private fun List<String>.joinToPascalCase() = joinToString(separator = "", transform = String::titlecase)

private fun List<String>.joinToSpaceCase() = joinToString(separator = " ")

private fun List<String>.joinToTitleSpaceCase() = joinToString(separator = " ", transform = String::titlecase)

private fun String.titlecase() = replaceFirstChar { char -> char.uppercase() }
