import { getLogger } from "@logtape/logtape";
import type { TargetLanguageCode } from "deepl-node";
import type { Snowflake } from "discord.js";
import type { SettingsStorageConfig, SqliteSettingsStorageConfig } from "../../Config";
import { Database } from "bun:sqlite";
import { environment } from "./environment";

const logger = getLogger(["transl", "settings-storage"]);

export const settingsStorage = getSettingsStorage(environment.settingsStorage);

export function getSettingsStorage(config: SettingsStorageConfig): SettingsStorage {
	if ("sqlite" in config && config.sqlite) {
		return new SqliteSettingsStorage(config.sqlite);
	}

	throw new Error("Settings Storage config is invalid!");
}

export interface SettingsStorage {
	getGuildLang(guildId: Snowflake): Promise<TargetLanguageCode | undefined>;
	setGuildLang(guildId: Snowflake, lang: TargetLanguageCode | undefined): Promise<void>;
}

export class SqliteSettingsStorage implements SettingsStorage {
	constructor(config: SqliteSettingsStorageConfig) {
		this.db = new Database(config.path, { strict: true, create: true });

		this.db.run("CREATE TABLE IF NOT EXISTS guildLang (guildId INTEGER PRIMARY KEY, lang CHARACTER(5))");
	}

	async getGuildLang(guildId: Snowflake): Promise<TargetLanguageCode | undefined> {
		logger.debug("Getting guild lang for guild {guildId}", { guildId });

		const query = this.db.query<{ lang: TargetLanguageCode }, { guildId: Snowflake }>(
			"SELECT lang FROM guildLang WHERE guildId = $guildId",
		);
		return query.get({ guildId })?.lang ?? undefined;
	}

	async setGuildLang(guildId: Snowflake, lang: TargetLanguageCode): Promise<void> {
		logger.debug("Setting guild lang for guild {guildId} to {lang}", { guildId, lang });

		const query = this.db.query<void, { guildId: Snowflake; lang: TargetLanguageCode }>(
			"INSERT OR REPLACE INTO guildLang (guildId, lang) VALUES ($guildId, $lang)",
		);

		query.run({ guildId, lang });
	}

	private db: Database;
}
