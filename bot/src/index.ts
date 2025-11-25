import { LogLevel, SapphireClient } from "@sapphire/framework";
import { configure as configureLogtape, getAnsiColorFormatter, getConsoleSink, getLogger } from "@logtape/logtape";
import { environment } from "./environment";
import { getOpenTelemetrySink } from "@logtape/otel";
import { LogtapeSapphireLoggerBridge } from "./logtape_sapphire_logger_bridge";
import { inspect } from "node:util";

const startTime = Date.now();

await configureLogtape({
	sinks: {
		console: getConsoleSink({
			formatter: getAnsiColorFormatter({
				format(values) {
					let props = values.record.properties;
					const rawMessage = typeof values.record.rawMessage === "string" ? values.record.rawMessage : "";

					if (typeof values.record.rawMessage === "string") {
						props = Object.fromEntries(Object.entries(props).filter(([key]) => !rawMessage.includes(`\{${key}\}`)));
					}

					if (Object.keys(props).length) {
						return `${values.timestamp} ${values.level} ${values.category}: ${values.message} ${inspect(props, {
							depth: null,
							colors: true,
							breakLength: Number.POSITIVE_INFINITY,
						})}`;
					}
					return `${values.timestamp} ${values.level} ${values.category}: ${values.message}`;
				},
			}),
		}),
		otel: environment.logging?.openTelemetry
			? getOpenTelemetrySink({
					diagnostics: true,
					serviceName: "transl",
					otlpExporterConfig: {
						concurrencyLimit: 1000,
						url: environment.logging.openTelemetry.url,
					},
				})
			: () => {},
	},
	loggers: [
		{
			category: ["logtape", "meta"],
			lowestLevel: "warning",
			sinks: ["console", "otel"],
		},
		{
			category: ["logtape", "meta", "otel"],
			lowestLevel: "warning",
			sinks: ["console"],
		},
		{
			category: ["transl"],
			lowestLevel: "debug",
			sinks: ["console", "otel"],
		},
		{
			category: ["sapphire"],
			lowestLevel: "debug",
			sinks: ["console", "otel"],
		},
	],
});

const logger = getLogger(["transl"]);

const client = new SapphireClient({
	shards: "auto", // this enables “internal sharding” (see here: https://discordjs.guide/legacy/sharding#how-does-sharding-work)
	intents: [],
	logger: {
		level: LogLevel.Debug,
		instance: new LogtapeSapphireLoggerBridge(LogLevel.Debug),
	},
});

client.addListener("ready", async () => {
	logger.info("Bot is ready!");
});

await client.login(environment.ui.discord.botToken);

logger.info("Bot took {duration}ms until login", { duration: Date.now() - startTime });
