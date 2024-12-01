package com.anderzenn.valuabledropprices;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

import java.awt.*;

@ConfigGroup("ValuableDropPrices")
public interface ValuableDropPricesConfig extends Config
{
	@ConfigSection(
			name = "Message Settings",
			description = "General settings for the message",
			position = 0
	)
	String msgSettings = "message settings";

	@ConfigSection(
			name = "Colour Settings",
			description = "Colour settings for the message",
			position = 1
	)
	String colSettings = "colour settings";

	@ConfigItem(
		keyName = "displayPrices",
		name = "Display Price Type",
		section = msgSettings,
		description = "Choose if you want to display both GE and HA prices, or one of them.",
		position = 0
	)
	default ValuableDropPriceDisplayType displayPrices() {
		return ValuableDropPriceDisplayType.HIGH_ALCH; // Default to HA
	}

	@ConfigItem(
			keyName = "formatPrices",
			name = "Format Prices",
			section = msgSettings,
			description = "Disable this if you want precise numbers, i.e. 1.000.000 instead of 1M",
			position = 1
	)
	default boolean formatPrices() { return true; }

	@ConfigItem(
			keyName = "mainColour",
			name = "Main Colour",
			section = colSettings,
			description = "Main colour for the message, default is #EF1020",
			position = 0
	)
	default Color mainColour() { return Color.decode("#EF1020"); }

	@ConfigItem(
			keyName = "itemColour",
			name = "Item Colour",
			section = colSettings,
			description = "Colour of the items name in the message. Default is black.",
			position = 1
	)
	default Color itemColour() { return Color.black; }

	@ConfigItem(
			keyName = "useMainColour_item",
			name = "Use main colour on item?",
			section = colSettings,
			description = "Use main colour as the colour for items.",
			position = 2
	)
	default boolean useMainColour_item() { return true; }

	@ConfigItem(
			keyName = "valueColour",
			name = "Value Colour",
			section = colSettings,
			description = "Colour for the value section of an item. Default is blue.",
			position = 3
	)
	default Color valueColour() { return Color.blue; }

	@ConfigItem(
			keyName = "useMainColour_value",
			name = "Use main colour on value?",
			section = colSettings,
			description = "Use main colour as the colour for value.",
			position = 4
	)
	default boolean useMainColour_value() { return true; }
















	// Debug config items - moved down for better visual seperation :)
	@ConfigSection(
			name = "Debugging Settings",
			description = "Settings for debugging and development purposes",
			position = 9999
	)
	String debugging = "debugging";

	@ConfigItem(
			keyName = "debugMode",
			name = "Debug Mode",
			section = debugging,
			description = "For debugging/developing the plugin. Need to be enabled for debug messages.",
			position = 0
	)
	default boolean debugMode() {
		return false;
	}

	// ------ //

	@ConfigItem(
			keyName = "prntItemName",
			name = "Print item name",
			section = debugging,
			description = "Prints item name in chat.",
			position = 1
	)
	default boolean prntItemName() {
		return false;
	}

	@ConfigItem(
			keyName = "prntItemId",
			name = "Print item id",
			section = debugging,
			description = "Prints item id in chat.",
			position = 2
	)
	default boolean prntItemId() {
		return false;
	}

	@ConfigItem(
			keyName = "prntItemQuantity",
			name = "Print item quantity string",
			section = debugging,
			description = "Prints the item quantity string in chat.",
			position = 3
	)
	default boolean prntItemQuantity() {
		return false;
	}

	@ConfigItem(
			keyName = "prntItemQuantityInt",
			name = "Print item quantity int",
			section = debugging,
			description = "Prints the item quantity int in chat.",
			position = 4
	)
	default boolean prntItemQuantityInt() {
		return false;
	}

	@ConfigItem(
			keyName = "prntItemValue",
			name = "Print item value",
			section = debugging,
			description = "Prints raw item value in chat.",
			position = 5
	)
	default boolean prntItemValue() {
		return false;
	}

	@ConfigItem(
			keyName = "debugValues",
			name = "Debug Values",
			section = debugging,
			description = "Sets bones (local) value to be high, used for testing messages since practically everything drops bones.",
			position = 6
	)
	default boolean debugValues() {
		return false;
	}
}
