package com.anderzenn.valuabledropprices;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("ValuableDropPrices")
public interface ValuableDropPricesConfig extends Config
{
	@ConfigItem(
		keyName = "displayPrices",
		name = "Display Price Type",
		description = "Choose if you want to display both GE and HA prices, or one of them.",
		position = 1
	)

	default ValuableDropPriceDisplayType displayPrices() {
		return ValuableDropPriceDisplayType.HIGH_ALCH; // Default to HA
	}

	@ConfigItem(
			keyName = "debugMode",
			name = "Debug Mode",
			description = "For debugging the plugin. WILL print messages in chat!",
			position = 99
	)

	default boolean debugMode() {
		return false;
	}
}
