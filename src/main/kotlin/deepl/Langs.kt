package deepl

import search
import util.toTitleSpaceCase

enum class SourceLang(
    val code: String,
    val flag: String
) {
    Bulgarian("BG", "\uD83C\uDDE7\uD83C\uDDEC"),
    Chinese("ZH", "\uD83C\uDDED\uD83C\uDDF0"),
    Czech("CS", "\uD83C\uDDE8\uD83C\uDDFF"),
    Danish("DA", "\uD83C\uDDE9\uD83C\uDDF0"),
    Dutch("NL", "\uD83C\uDDF3\uD83C\uDDF1"),
    English("EN", "\uD83C\uDDEC\uD83C\uDDE7"),
    Estonian("ET", "\uD83C\uDDEA\uD83C\uDDEA"),
    Finnish("FI", "\uD83C\uDDEB\uD83C\uDDEE"),
    French("FR", "\uD83C\uDDEB\uD83C\uDDF7"),
    German("DE", "\uD83C\uDDE9\uD83C\uDDEA"),
    Greek("EL", "\uD83C\uDDEC\uD83C\uDDF7"),
    Hungarian("HU", "\uD83C\uDDED\uD83C\uDDFA"),
    Indonesian("ID", "\uD83C\uDDEE\uD83C\uDDE9"),
    Italian("IT", "\uD83C\uDDEE\uD83C\uDDF9"),
    Japanese("JA", "\uD83C\uDDEF\uD83C\uDDF5"),
    Latvian("LV", "\uD83C\uDDF1\uD83C\uDDFB"),
    Lithuanian("LT", "\uD83C\uDDF1\uD83C\uDDF9"),
    Polish("PL", "\uD83C\uDDF5\uD83C\uDDF1"),
    Portuguese("PT", "\uD83C\uDDF5\uD83C\uDDF9"),
    Romanian("RO", "\uD83C\uDDF7\uD83C\uDDF4"),
    Russian("RU", "\uD83C\uDDF7\uD83C\uDDFA"),
    Slovak("SK", "\uD83C\uDDF8\uD83C\uDDF0"),
    Slovenian("SL", "\uD83C\uDDF8\uD83C\uDDEE"),
    Spanish("ES", "\uD83C\uDDEA\uD83C\uDDF8"),
    Swedish("SV", "\uD83C\uDDF8\uD83C\uDDEA"),
    Turkish("TR", "\uD83C\uDDF9\uD83C\uDDF7"),
    Ukrainian("UK", "\uD83C\uDDFA\uD83C\uDDE6");

    override fun toString() = name.toTitleSpaceCase()

    companion object {
        fun find(query: String): Iterable<SourceLang> {
            val resultsByName = values().asIterable().search(query) {it.name}
            val resultsByCode = values().asIterable().search(query) {it.code}

            return (resultsByName + resultsByCode).sortedBy {it.quality}.map {it.value}.distinct()
        }
    }
}

enum class TargetLang(
    val code: String,
    val flag: String,
    val hasFormality: Boolean = false
) {
    AmericanEnglish("EN-US", "\uD83C\uDDFA\uD83C\uDDF8"),
    BrazilianPortuguese("PT-BR", "\uD83C\uDDE7\uD83C\uDDF7", true),
    BritishEnglish("EN-GB", "\uD83C\uDDEC\uD83C\uDDE7"),
    Bulgarian("BG", "\uD83C\uDDE7\uD83C\uDDEC"),
    Chinese("ZH", "\uD83C\uDDED\uD83C\uDDF0"),
    Czech("CS", "\uD83C\uDDE8\uD83C\uDDFF"),
    Danish("DA", "\uD83C\uDDE9\uD83C\uDDF0"),
    Dutch("NL", "\uD83C\uDDF3\uD83C\uDDF1", true),
    Estonian("ET", "\uD83C\uDDEA\uD83C\uDDEA"),
    Finnish("FI", "\uD83C\uDDEB\uD83C\uDDEE"),
    French("FR", "\uD83C\uDDEB\uD83C\uDDF7", true),
    German("DE", "\uD83C\uDDE9\uD83C\uDDEA", true),
    Greek("EL", "\uD83C\uDDEC\uD83C\uDDF7"),
    Hungarian("HU", "\uD83C\uDDED\uD83C\uDDFA"),
    Indonesian("ID", "\uD83C\uDDEE\uD83C\uDDE9"),
    Italian("IT", "\uD83C\uDDEE\uD83C\uDDF9", true),
    Japanese("JA", "\uD83C\uDDEF\uD83C\uDDF5"),
    Latvian("LV", "\uD83C\uDDF1\uD83C\uDDFB"),
    Lithuanian("LT", "\uD83C\uDDF1\uD83C\uDDF9"),
    Polish("PL", "\uD83C\uDDF5\uD83C\uDDF1", true),
    Portuguese("PT-PT", "\uD83C\uDDF5\uD83C\uDDF9", true),
    Romanian("RO", "\uD83C\uDDF7\uD83C\uDDF4"),
    Russian("RU", "\uD83C\uDDF7\uD83C\uDDFA", true),
    Slovak("SK", "\uD83C\uDDF8\uD83C\uDDF0"),
    Slovenian("SL", "\uD83C\uDDF8\uD83C\uDDEE"),
    Spanish("ES", "\uD83C\uDDEA\uD83C\uDDF8", true),
    Swedish("SV", "\uD83C\uDDF8\uD83C\uDDEA"),
    Turkish("TR", "\uD83C\uDDF9\uD83C\uDDF7"),
    Ukrainian("UK", "\uD83C\uDDFA\uD83C\uDDE6");

    override fun toString() = name.toTitleSpaceCase()

    fun toSourceLang(): SourceLang {
        return when (this) {
            Bulgarian -> SourceLang.Bulgarian
            Czech -> SourceLang.Czech
            Danish -> SourceLang.Danish
            German -> SourceLang.German
            Greek -> SourceLang.Greek
            BritishEnglish -> SourceLang.English
            AmericanEnglish -> SourceLang.English
            Spanish -> SourceLang.Spanish
            Estonian -> SourceLang.Estonian
            Finnish -> SourceLang.Finnish
            French -> SourceLang.French
            Hungarian -> SourceLang.Hungarian
            Indonesian -> SourceLang.Indonesian
            Italian -> SourceLang.Italian
            Japanese -> SourceLang.Japanese
            Lithuanian -> SourceLang.Lithuanian
            Latvian -> SourceLang.Latvian
            Dutch -> SourceLang.Dutch
            Polish -> SourceLang.Polish
            BrazilianPortuguese -> SourceLang.Portuguese
            Portuguese -> SourceLang.Portuguese
            Romanian -> SourceLang.Romanian
            Russian -> SourceLang.Russian
            Slovak -> SourceLang.Slovak
            Slovenian -> SourceLang.Slovenian
            Swedish -> SourceLang.Swedish
            Turkish -> SourceLang.Turkish
            Ukrainian -> SourceLang.Ukrainian
            Chinese -> SourceLang.Chinese
        }
    }

    companion object {
        fun find(query: String): Iterable<TargetLang> {
            val resultsByName = values().asIterable().search(query) {it.name}
            val resultsByCode = values().asIterable().search(query) {it.code}

            return (resultsByName + resultsByCode).sortedBy {it.quality}.map {it.value}.distinct()
        }
    }
}
