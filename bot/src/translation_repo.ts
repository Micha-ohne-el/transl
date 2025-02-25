import type { SourceLanguageCode, TargetLanguageCode } from "deepl-node";
import { type Cache, getCache } from "./cache";
import { getTranslator, type Translator } from "./translator";
import { environment } from "./environment";

class TranslationRepo {
	constructor(
		private readonly translator: Translator,
		private readonly cache: Cache,
	) {}

	async get({ sourceLang, targetLang, sourceText, sourceTextContext, cacheTimeToLiveSeconds }: Get): Promise<string> {
		const cached = await this.cache.get({ sourceLang, targetLang, sourceText });

		if (cached) return cached;

		const targetText = await this.translator.translate({ sourceText, sourceLang, targetLang, context: sourceTextContext });

		await this.cache.set({ sourceLang, targetLang, sourceText, targetText, timeToLiveSeconds: cacheTimeToLiveSeconds });

		return targetText;
	}
}

export const translationRepo = new TranslationRepo(getTranslator(environment.translator), getCache(environment.cache));

interface Get {
	sourceLang?: SourceLanguageCode;
	targetLang: TargetLanguageCode;
	sourceText: string;
	sourceTextContext?: string;
	cacheTimeToLiveSeconds?: number | null;
}
