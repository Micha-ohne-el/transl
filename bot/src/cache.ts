import type { SourceLanguageCode, TargetLanguageCode } from "deepl-node";
import { createClient } from "redis";
import { type Entity, EntityId, Repository, Schema } from "redis-om";
import { environment } from "./environment";

export class Cache {
	async get(sourceLang: SourceLanguageCode, targetLang: TargetLanguageCode, sourceText: string): Promise<string | undefined> {
		const repo = await this.translationRepo;

		const index = this.index++;
		console.debug(`[cache operation #${index}]`, "Searching cache:", { sourceLang, targetLang, sourceText });

		try {
			const result = await repo
				.search()
				.where("sourceLang")
				.equals(sourceLang)
				.and("targetLang")
				.equals(targetLang)
				.and("sourceText")
				.equals(sourceText)
				.return.first();

			if (result) {
				console.debug(`[cache operation #${index}]`, "Cache hit:", { targetText: result.targetText });
			} else {
				console.debug(`[cache operation #${index}]`, "Cache miss.");
			}

			return result?.targetText;
		} catch (e) {
			console.error(`[cache operation #${index}]`, "An error occurred:", e);
			throw e;
		}
	}

	async set(
		sourceLang: SourceLanguageCode,
		targetLang: TargetLanguageCode,
		sourceText: string,
		targetText: string,
	): Promise<void> {
		const repo = await this.translationRepo;

		const index = this.index++;
		console.debug(`[cache operation #${index}]`, "Saving to cache:", { sourceLang, targetLang, sourceText, targetText });

		try {
			const entity = await repo.save({
				sourceLang,
				targetLang,
				sourceText,
				targetText,
			});

			const id = entity[EntityId];

			if (id) {
				await repo.expire(id, 10); // TODO: obviously 10 seconds is stupid.
			} else {
				console.warn(`[cache operation #${index}]`, "Could not set expiry on cache entry because ID is mysteriously missing!");
			}

			console.log(`[cache operation #${index}]`, "Saved successfully.");
		} catch (e) {
			console.error(`[cache operation #${index}]`, "An error occurred:", e);
		}
	}

	private connection = (async () => {
		const client = createClient({ url: environment.cache.redis.url });

		client.on("error", error => console.error("Redis client error:", error));
		await client.connect();

		return client;
	})();

	private translationSchema = new Schema<Translation>("translation", {
		sourceLang: { type: "string" },
		targetLang: { type: "string" },
		sourceText: { type: "string" },
		targetText: { type: "string" },
	});

	private translationRepo = (async () => {
		const repo = new Repository(this.translationSchema, await this.connection);

		await repo.createIndex();

		return repo;
	})();

	private index = 1;
}

interface Translation extends Entity {
	sourceLang: string;
	targetLang: string;
	sourceText: string;
	targetText: string;
}
