import type { SourceLanguageCode, TargetLanguageCode } from "deepl-node";
import { Cache } from "./cache";
import { Translator } from "./translator";

class TranslationRepo {
	constructor(
		private translator: Translator,
		private cache: Cache,
	) {}

	async get({ sourceLang, targetLang, sourceText, sourceTextContext, cacheTimeToLiveSeconds }: Get): Promise<string> {
		const cached = await this.cache.get({ sourceLang, targetLang, sourceText });

		if (cached) return cached;

		const targetText = await this.translator.translate({ sourceText, sourceLang, targetLang, context: sourceTextContext });

		await this.cache.set({ sourceLang, targetLang, sourceText, targetText, timeToLiveSeconds: cacheTimeToLiveSeconds });

		return targetText;
	}
}

export const translationRepo = new TranslationRepo(new Translator(), new Cache());

interface Get {
	sourceLang: SourceLanguageCode;
	targetLang: TargetLanguageCode;
	sourceText: string;
	sourceTextContext?: string;
	cacheTimeToLiveSeconds?: number | null;
}
