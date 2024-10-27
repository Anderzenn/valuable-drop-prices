package com.anderzenn.valuabledropprices;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.*;

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

	/* Temporarily disabled.
	@ConfigItem(
		keyName = "valuableConsideration",
		name = "Primary Value",
		description = "Whether to decide if an item is valuable based on GE or HA prices.\n" +
				"Grand Exchange will use only the GE Value\n" +
				"High Alch will use only the HA Value\n" +
				"Both will use whichever one is higher",
		position = 2
	)

	default ValuableDropConsiderationType valuableConsideration() {
		return ValuableDropConsiderationType.HIGH_ALCH; // Default to HA
	} */

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
