import type { Config } from "../../Config.ts";
import * as config from "../../config.json" with { type: "json" };

export const environment = (config as Config).bot;
