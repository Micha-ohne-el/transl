import type { TargetLanguageCode } from "deepl-node";
import { Locale } from "discord.js";

export const targetLangs: Record<Locale, TargetLanguageCode> = {
	[Locale.Bulgarian]: "bg",
	[Locale.ChineseCN]: "zh",
	[Locale.ChineseTW]: "zh",
	[Locale.Croatian]: "en-GB", // fallback because croatian is not supported by DeepL.
	[Locale.Czech]: "cs",
	[Locale.Danish]: "da",
	[Locale.Dutch]: "nl",
	[Locale.EnglishGB]: "en-GB",
	[Locale.EnglishUS]: "en-US",
	[Locale.Finnish]: "fi",
	[Locale.French]: "fr",
	[Locale.German]: "de",
	[Locale.Greek]: "el",
	[Locale.Hindi]: "en-GB", // fallback beause hindi is not supported by DeepL.
	[Locale.Hungarian]: "hu",
	[Locale.Indonesian]: "id",
	[Locale.Italian]: "it",
	[Locale.Japanese]: "ja",
	[Locale.Korean]: "ko",
	[Locale.Lithuanian]: "lt",
	[Locale.Norwegian]: "nb",
	[Locale.Polish]: "pl",
	[Locale.PortugueseBR]: "pt-BR",
	[Locale.Romanian]: "ro",
	[Locale.Russian]: "ru",
	[Locale.SpanishES]: "es",
	[Locale.SpanishLATAM]: "es",
	[Locale.Swedish]: "sv",
	[Locale.Thai]: "en-GB", // fallback because thai is not supported by DeepL.
	[Locale.Turkish]: "tr",
	[Locale.Ukrainian]: "uk",
	[Locale.Vietnamese]: "en-GB", // fallback because vietnamese is not supported by DeepL.
};
