package com.anderzenn.valuabledropprices;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.ColorUtil;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@PluginDescriptor(
		name = "Valuable Drop Prices Plugin",
		description = "Plugin for adding HA prices to the valuable drops chat notification, as well as modifying colours."
)
public class ValuableDropPricesPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ItemManager itemManager;

	@Inject
	private ValuableDropPricesConfig config;

	int dropThreshold;
	int dropThresholdId = 5400;

	@Provides
	ValuableDropPricesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ValuableDropPricesConfig.class);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged) {
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN) {

			// Check what the clients valuable drop threshold is set to in settings and set variable to new value.
			dropThreshold = client.getVarbitValue(dropThresholdId);
        }
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event) {

		// Check if varbit matching the valuable drop threshold setting has changed, if so set variable to new value.
		// Also sets the value if the server never sent the actual value on login.
		if (event.getVarbitId() == dropThresholdId) {
			dropThreshold = client.getVarbitValue(dropThresholdId);
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage) {
		MessageNode messageNode = chatMessage.getMessageNode();
		if (messageNode.getType() != ChatMessageType.GAMEMESSAGE) {
			return;
		}

		// Pattern regEx from Mafhams plugins: https://github.com/Mafham/mafham-plugins/
		String valuableDropPatternString = "Valuable drop: ((?:\\d+ x )?(.*?)) \\((\\d{1,3}(?:,\\d{3})*|\\d+) coin(?:s?)\\)";
		Pattern valuableDropPattern = Pattern.compile(valuableDropPatternString);
		Matcher valuableDropMatcher = valuableDropPattern.matcher(messageNode.getValue());

		if (valuableDropMatcher.find()) {
			// Extract item name and quantity
			String quantityString = valuableDropMatcher.group(1);
			String itemName = valuableDropMatcher.group(2);
			int quantity = quantityString != null & quantityString.contains(" x ") ? Integer.parseInt(quantityString.split(" x ")[0]) : 1;

			// Get item ID and fetch values
			int itemId = itemManager.search(itemName).get(0).getId(); // Basic itemID search. Maybe there's a better way?
			int geValue = itemManager.getItemPrice(itemId);
			int haValue = client.getItemDefinition(itemId).getHaPrice();

			// Check if item is "coins" or "coin". If it is set value to 1 to avoid insanely high nonsense numbers.
			if (itemName.equalsIgnoreCase("coins") || itemName.equalsIgnoreCase("coin")) {
				geValue = 1;
				haValue = 1;
			}

			// Check if displayType is HA and if haValue is zero or if it's below the drop threshold. If it is, then don't print a message.
			// And do the same for GE.
			if (config.displayPrices() == ValuableDropPriceDisplayType.HIGH_ALCH && haValue == 0 || config.displayPrices() == ValuableDropPriceDisplayType.HIGH_ALCH && haValue < dropThreshold) {
				messageNode.setValue(null);
				return;
			} else if (config.displayPrices() == ValuableDropPriceDisplayType.GRAND_EXCHANGE && geValue == 0 || config.displayPrices() == ValuableDropPriceDisplayType.GRAND_EXCHANGE && geValue < dropThreshold) {
				messageNode.setValue(null);
				return;
			}

			// Debugging
			if (config.debugMode()) {
				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Item Name: " + itemName, null);
				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Item Quantity String: " + quantityString, null);
				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Item Quantity Int: " + quantity, null);
				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Item Value: " + valuableDropMatcher.group(3), null);

				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Item ID: " + itemId, null);
			}

			// Calculate total values
			int totalGEValue = geValue * quantity;
			int totalHAValue = haValue * quantity;

			// Format message based on user configuration
			String modifiedMessage = formatDropMessage(itemName, quantityString, totalHAValue, totalGEValue);

			// Append string onto default message
			messageNode.setValue(ColorUtil.prependColorTag(modifiedMessage, Color.decode("#EF1020"))); // Find out how to fetch the colour from settings or the original message.
		}

	}

	private String formatDropMessage(String itemName, String quantityString, int haValue, int geValue) {
		ValuableDropPriceDisplayType displayType = config.displayPrices();
		String haValueString = haValue > 0 ? haValue + " gp" : "";
		String geValueString = geValue > 0 ? geValue + " gp" : "";
		String valueString;

		// set valuestring based on the users settings.
		switch (displayType) {
			case GRAND_EXCHANGE:
				valueString = itemName.equalsIgnoreCase("coins") || itemName.equalsIgnoreCase("coin") ? String.format("(%s)", geValueString) : String.format("(GE: %s)", geValueString);
				break;
			case HIGH_ALCH:
				valueString = itemName.equalsIgnoreCase("coins") || itemName.equalsIgnoreCase("coin") ? String.format("(%s)", haValueString) : String.format("(HA: %s)", haValueString);
				break;
			case BOTH:
				valueString = itemName.equalsIgnoreCase("coins") || itemName.equalsIgnoreCase("coin") ? String.format("(%s)", geValueString) : String.format("(GE: %s, HA: %s)", geValueString, haValueString);
				break;
			default:
				valueString = "";
				break;
		}

		// Build full message
		return String.format("Valuable drop: %s %s", (quantityString != null ? quantityString : itemName), valueString);
	}

}
