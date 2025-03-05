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
					if (Object.keys(values.record.properties).length) {
						return `${values.timestamp} ${values.level} ${values.category}: ${values.message} ${inspect(values.record.properties, { depth: 1 })}`;
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
	intents: [],
	logger: {
		level: LogLevel.Debug,
		instance: new LogtapeSapphireLoggerBridge(LogLevel.Debug),
	},
});

await client.login(environment.ui.discord.botToken);

logger.info("Bot took {duration}ms until ready", { duration: Date.now() - startTime });
