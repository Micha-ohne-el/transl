import { getLogger } from "@logtape/logtape";
import { Logger, LogLevel } from "@sapphire/framework";

export class LogtapeSapphireLoggerBridge extends Logger {
	override write(level: LogLevel, ...values: unknown[]) {
		switch (level) {
			case LogLevel.Trace:
				this.logtapeLogger.debug(values.join(" "));
				break;
			case LogLevel.Debug:
				this.logtapeLogger.debug(values.join(" "));
				break;
			case LogLevel.Info:
				this.logtapeLogger.info(values.join(" "));
				break;
			case LogLevel.Warn:
				this.logtapeLogger.warn(values.join(" "));
				break;
			case LogLevel.Error:
				this.logtapeLogger.error(values.join(" "));
				break;
			case LogLevel.Fatal:
				this.logtapeLogger.fatal(values.join(" "));
				break;
		}
	}

	private readonly logtapeLogger = getLogger(["sapphire"]);
}
