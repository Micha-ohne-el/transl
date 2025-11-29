export type Config = {
	bot: {
		ui: UserInterfaceConfig;
		translator: TranslatorConfig;
		cache: CacheConfig;
		settingsStorage: SettingsStorageConfig;
		logging: LoggingConfig;
	};
};

export type UserInterfaceConfig = { discord: DiscordUserInterfaceConfig };
export type DiscordUserInterfaceConfig = {
	botToken: string;
	clientId: string;
	adminIds: string[];
	testGuildIds?: string[];
};

export type TranslatorConfig = { deepl: DeeplTranslatorConfig };
export type DeeplTranslatorConfig = {
	authToken: string;
};

export type CacheConfig = { redis: RedisCacheConfig } | null;
export type RedisCacheConfig = {
	url: string;
};

export type SettingsStorageConfig = { sqlite: SqliteSettingsStorageConfig };
export type SqliteSettingsStorageConfig = {
	path: string;
};

export type LoggingConfig = { openTelemetry: OpenTelemetryLoggingConfig } | null;
export type OpenTelemetryLoggingConfig = {
	url: string;
	headers?: Record<string, string>;
};
