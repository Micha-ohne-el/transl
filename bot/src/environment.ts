import { raise } from "./utils/raise";
import { bot } from "../../config.json";

export const environment = {
	botToken: bot.discord.botToken ?? raise("No bot token specified in config.json!"),
	clientId: bot.discord.clientId ?? raise("No client ID specified in config.json!"),
	testGuildId: bot.discord.testGuildId,
	adminIds: bot.discord.adminIds ?? raise("No admin IDs specified in config.json!"),
	deeplAuthToken: bot.deepl.authToken ?? raise("No DeepL auth token specified in config.json!"),
};
