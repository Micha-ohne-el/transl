fun <T> Iterable<T>.search(query: String, transform: (T) -> String = {it.toString()}): Iterable<Match<T>> {
    return map {
        Match(it, transform(it).indexOf(query, ignoreCase = true))
    }.filter {
        it.quality != -1
    }.sortedByDescending {
        it.quality
    }
}

fun <T> Array<T>.search(query: String, transform: (T) -> String = {it.toString()}) = asIterable().search(query, transform)

data class Match<T>(
    val value: T,
    val quality: Int
)
