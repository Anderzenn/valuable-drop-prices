package com.anderzenn.valuabledropprices;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.*;

@ConfigGroup("ValuableDropPrices")
public interface ValuableDropPricesConfig extends Config
{
	@ConfigItem(
		keyName = "valuableDropColour",
		name = "Drop Colour",
		description = "Colour for valuable drops.",
		position = 1
	)
	default Color valuableDropColour()
	{
		return Color.decode("#FF3232");
	}

	@ConfigItem(
		keyName = "valuableDropThreshold",
		name = "Valuable Drop Threshold",
		description = "Set the value at which a drop is considered valuable",
		position = 2
	)

	default int valuableDropThreshold()
	{
		return 10000; // Default threshold is 10,000
	}

	@ConfigItem(
		keyName = "displayPrices",
		name = "Display Price Type",
		description = "Choose if you want to display both GE and HA prices, or one of them.",
		position = 3
	)

	default ValuableDropPriceDisplayType displayPrices() {
		return ValuableDropPriceDisplayType.HIGH_ALCH; // Default to HA
	}

	@ConfigItem(
		keyName = "valuableConsideration",
		name = "Primary Value",
		description = "Whether to decide if an item is valuable based on GE or HA prices.\n" +
				"Grand Exchange will use only the GE Value\n" +
				"High Alch will use only the HA Value\n" +
				"Both will use whichever one is higher",
		position = 4
	)

	default ValuableDropConsiderationType valuableConsideration() {
		return ValuableDropConsiderationType.HIGH_ALCH; // Default to HA
	}
}
