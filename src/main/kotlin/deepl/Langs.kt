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
    val code: String
) {
    Bulgarian("BG"),
    Czech("CS"),
    Danish("DA"),
    German("DE"),
    Greek("EL"),
    BritishEnglish("EN-GB"),
    AmericanEnglish("EN-US"),
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
    BrazilianPortuguese("PT-BR"),
    Portuguese("PT-PT"),
    Romanian("RO"),
    Russian("RU"),
    Slovak("SK"),
    Slovenian("SL"),
    Swedish("SV"),
    Turkish("TR"),
    Chinese("ZH");
}
