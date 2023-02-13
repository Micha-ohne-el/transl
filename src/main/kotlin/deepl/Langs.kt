package deepl

import search
import util.toTitleSpaceCase

enum class SourceLang(
    val code: String
) {
    Bulgarian("BG"),
    Chinese("ZH"),
    Czech("CS"),
    Danish("DA"),
    Dutch("NL"),
    English("EN"),
    Estonian("ET"),
    Finnish("FI"),
    French("FR"),
    German("DE"),
    Greek("EL"),
    Hungarian("HU"),
    Indonesian("ID"),
    Italian("IT"),
    Japanese("JA"),
    Korean("KO"),
    Latvian("LV"),
    Lithuanian("LT"),
    Norwegian("NB"),
    Polish("PL"),
    Portuguese("PT"),
    Romanian("RO"),
    Russian("RU"),
    Slovak("SK"),
    Slovenian("SL"),
    Spanish("ES"),
    Swedish("SV"),
    Turkish("TR"),
    Ukrainian("UK");

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
    val code: String
) {
    AmericanEnglish("EN-US"),
    BrazilianPortuguese("PT-BR"),
    BritishEnglish("EN-GB"),
    Bulgarian("BG"),
    Chinese("ZH"),
    Czech("CS"),
    Danish("DA"),
    Dutch("NL"),
    Estonian("ET"),
    Finnish("FI"),
    French("FR"),
    German("DE"),
    Greek("EL"),
    Hungarian("HU"),
    Indonesian("ID"),
    Italian("IT"),
    Japanese("JA"),
    Korean("KO"),
    Latvian("LV"),
    Lithuanian("LT"),
    Norwegian("NB"),
    Polish("PL"),
    Portuguese("PT-PT"),
    Romanian("RO"),
    Russian("RU"),
    Slovak("SK"),
    Slovenian("SL"),
    Spanish("ES"),
    Swedish("SV"),
    Turkish("TR"),
    Ukrainian("UK");

    override fun toString() = name.toTitleSpaceCase()

    fun toSourceLang(): SourceLang {
        return when (this) {
            AmericanEnglish -> SourceLang.English
            BrazilianPortuguese -> SourceLang.Portuguese
            BritishEnglish -> SourceLang.English
            Bulgarian -> SourceLang.Bulgarian
            Chinese -> SourceLang.Chinese
            Czech -> SourceLang.Czech
            Danish -> SourceLang.Danish
            Dutch -> SourceLang.Dutch
            Estonian -> SourceLang.Estonian
            Finnish -> SourceLang.Finnish
            French -> SourceLang.French
            German -> SourceLang.German
            Greek -> SourceLang.Greek
            Hungarian -> SourceLang.Hungarian
            Indonesian -> SourceLang.Indonesian
            Italian -> SourceLang.Italian
            Japanese -> SourceLang.Japanese
            Korean -> SourceLang.Korean
            Latvian -> SourceLang.Latvian
            Lithuanian -> SourceLang.Lithuanian
            Norwegian -> SourceLang.Norwegian
            Polish -> SourceLang.Polish
            Portuguese -> SourceLang.Portuguese
            Romanian -> SourceLang.Romanian
            Russian -> SourceLang.Russian
            Slovak -> SourceLang.Slovak
            Slovenian -> SourceLang.Slovenian
            Spanish -> SourceLang.Spanish
            Swedish -> SourceLang.Swedish
            Turkish -> SourceLang.Turkish
            Ukrainian -> SourceLang.Ukrainian
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
