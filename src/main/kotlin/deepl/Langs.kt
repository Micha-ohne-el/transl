package deepl

enum class SourceLang(
    val code: String
) {
    Bulgarian("BG"),
    Czech("CS"),
    Danish("DA"),
    German("DE"),
    Greek("EL"),
    English("EN"),
    Spanish("ES"),
    Estonian("ET"),
    Finnish("FI"),
    French("FR"),
    Hungarian("HU"),
    Indonesian("ID"),
    Italian("IT"),
    Japanese("JA"),
    Lithuanian("LT"),
    Latvian("LV"),
    Dutch("NL"),
    Polish("PL"),
    Portuguese("PT"),
    Romanian("RO"),
    Russian("RU"),
    Slovak("SK"),
    Slovenian("SL"),
    Swedish("SV"),
    Turkish("TR"),
    Chinese("ZH");
}

enum class TargetLang(
    val code: String,
    val hasFormality: Boolean = false
) {
    Bulgarian("BG"),
    Czech("CS"),
    Danish("DA"),
    German("DE", true),
    Greek("EL"),
    BritishEnglish("EN-GB"),
    AmericanEnglish("EN-US"),
    Spanish("ES", true),
    Estonian("ET"),
    Finnish("FI"),
    French("FR", true),
    Hungarian("HU"),
    Indonesian("ID"),
    Italian("IT", true),
    Japanese("JA"),
    Lithuanian("LT"),
    Latvian("LV"),
    Dutch("NL", true),
    Polish("PL", true),
    BrazilianPortuguese("PT-BR", true),
    Portuguese("PT-PT", true),
    Romanian("RO"),
    Russian("RU", true),
    Slovak("SK"),
    Slovenian("SL"),
    Swedish("SV"),
    Turkish("TR"),
    Chinese("ZH");

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
            Chinese -> SourceLang.Chinese
        }
    }
}
