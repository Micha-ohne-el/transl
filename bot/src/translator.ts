import * as Deepl from "deepl-node";
import { getLogger } from "@logtape/logtape";
import type { DeeplTranslatorConfig, TranslatorConfig } from "../../Config";

const logger = getLogger(["transl", "translator"]);

export function getTranslator(config: TranslatorConfig): Translator {
	if ("deepl" in config && config.deepl) {
		return new DeeplTranslator(config.deepl);
	}
	throw new Error("Translator config is invalid!");
}

export interface Translator {
	translate(options: Translate): Promise<string>;
}

export class DeeplTranslator implements Translator {
	constructor(config: DeeplTranslatorConfig) {
		this.deeplTranslator = new Deepl.Translator(config.authToken);
	}

	async translate({ sourceLang, targetLang, sourceText, context }: Translate): Promise<string> {
		const log = logger.with({ id: this.index++, sourceLang, targetLang, sourceText, context });

		log.debug("Attempting translation");

		try {
			const result = await this.deeplTranslator.translateText(sourceText, sourceLang ?? null, targetLang ?? "en-US", {
				formality: "prefer_less",
				preserveFormatting: true,
				context,
			});

			log.info("Translation succeeded", { result });

			return result.text;
		} catch (error) {
			if (error instanceof Deepl.TooManyRequestsError) {
				log.error("Too many requests against the DeepL API!", { error });
			} else if (error instanceof Deepl.QuotaExceededError) {
				log.error("Translation quota exceeded!", { error });
			} else if (error instanceof Deepl.DeepLError) {
				log.error("An error occurred during translation", { error });
			}

			throw error;
		}
	}

	private readonly deeplTranslator: Deepl.Translator;

	private index = 1;
}

interface Translate {
	sourceLang?: Deepl.SourceLanguageCode;
	targetLang: Deepl.TargetLanguageCode;
	sourceText: string;
	context?: string;
}
