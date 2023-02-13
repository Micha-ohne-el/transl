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
    Latvian("LV"),
    Lithuanian("LT"),
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
    Latvian("LV"),
    Lithuanian("LT"),
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
