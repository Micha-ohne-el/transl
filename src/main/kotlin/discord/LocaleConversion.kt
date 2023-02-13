package discord

import deepl.SourceLang
import deepl.TargetLang
import dev.kord.common.Locale

fun Locale.toSourceLang(): SourceLang {
    return when (this) {
        Locale.DANISH -> SourceLang.Danish
        Locale.GERMAN -> SourceLang.German
        Locale.ENGLISH_GREAT_BRITAIN -> SourceLang.English
        Locale.ENGLISH_UNITED_STATES -> SourceLang.English
        Locale.SPANISH_SPAIN -> SourceLang.Spanish
        Locale.FRENCH -> SourceLang.French
        Locale.CROATIAN -> SourceLang.English // source: https://howwidelyspoken.com/how-widely-spoken-english-croatia/
        Locale.ITALIAN -> SourceLang.Italian
        Locale.LITHUANIAN -> SourceLang.Lithuanian
        Locale.HUNGARIAN -> SourceLang.Hungarian
        Locale.DUTCH -> SourceLang.Dutch
        Locale.NORWEGIAN -> SourceLang.Norwegian
        Locale.POLISH -> SourceLang.Polish
        Locale.PORTUGUESE_BRAZIL -> SourceLang.Portuguese
        Locale.ROMANIAN -> SourceLang.Romanian
        Locale.FINNISH -> SourceLang.Finnish
        Locale.SWEDISH -> SourceLang.Swedish
        Locale.VIETNAMESE -> SourceLang.French // source: https://www.worldatlas.com/articles/languages-spoken-in-vietnam.html
        Locale.TURKISH -> SourceLang.Turkish
        Locale.CZECH -> SourceLang.Czech
        Locale.GREEK -> SourceLang.Greek
        Locale.BULGARIAN -> SourceLang.Bulgarian
        Locale.RUSSIAN -> SourceLang.Russian
        Locale.UKRAINIAN -> SourceLang.Ukrainian
        Locale.HINDI -> SourceLang.English // source: https://www.berlitz.com/blog/indian-languages-spoken-list
        Locale.THAI -> SourceLang.English // source: https://www.tourismthailand.org/Articles/plan-your-trip-language-and-culture
        Locale.CHINESE_CHINA -> SourceLang.Chinese
        Locale.JAPANESE -> SourceLang.Japanese
        Locale.CHINESE_TAIWAN -> SourceLang.Chinese // source: https://www.worldatlas.com/articles/what-languages-are-spoken-in-taiwan.html
        Locale.KOREAN -> SourceLang.Korean

        else -> SourceLang.English
    }
}

fun Locale.toTargetLang(): TargetLang {
    return when (this) {
        Locale.DANISH -> TargetLang.Danish
        Locale.GERMAN -> TargetLang.German
        Locale.ENGLISH_GREAT_BRITAIN -> TargetLang.BritishEnglish
        Locale.ENGLISH_UNITED_STATES -> TargetLang.AmericanEnglish
        Locale.SPANISH_SPAIN -> TargetLang.Spanish
        Locale.FRENCH -> TargetLang.French
        Locale.CROATIAN -> TargetLang.BritishEnglish // source: https://howwidelyspoken.com/how-widely-spoken-english-croatia/
        Locale.ITALIAN -> TargetLang.Italian
        Locale.LITHUANIAN -> TargetLang.Lithuanian
        Locale.HUNGARIAN -> TargetLang.Hungarian
        Locale.DUTCH -> TargetLang.Dutch
        Locale.NORWEGIAN -> TargetLang.Norwegian
        Locale.POLISH -> TargetLang.Polish
        Locale.PORTUGUESE_BRAZIL -> TargetLang.BrazilianPortuguese
        Locale.ROMANIAN -> TargetLang.Romanian
        Locale.FINNISH -> TargetLang.Finnish
        Locale.SWEDISH -> TargetLang.Swedish
        Locale.VIETNAMESE -> TargetLang.French // source: https://www.worldatlas.com/articles/languages-spoken-in-vietnam.html
        Locale.TURKISH -> TargetLang.Turkish
        Locale.CZECH -> TargetLang.Czech
        Locale.GREEK -> TargetLang.Greek
        Locale.BULGARIAN -> TargetLang.Bulgarian
        Locale.RUSSIAN -> TargetLang.Russian
        Locale.UKRAINIAN -> TargetLang.Ukrainian
        Locale.HINDI -> TargetLang.BritishEnglish // source: https://www.berlitz.com/blog/indian-languages-spoken-list
        Locale.THAI -> TargetLang.BritishEnglish // source: https://www.tourismthailand.org/Articles/plan-your-trip-language-and-culture
        Locale.CHINESE_CHINA -> TargetLang.Chinese
        Locale.JAPANESE -> TargetLang.Japanese
        Locale.CHINESE_TAIWAN -> TargetLang.Chinese // source: https://www.worldatlas.com/articles/what-languages-are-spoken-in-taiwan.html
        Locale.KOREAN -> TargetLang.Korean

        else -> TargetLang.BritishEnglish
    }
}
