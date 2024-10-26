package com.anderzenn.valuabledropprices;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemSpawned;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.ColorUtil;

import java.awt.*;
import java.util.Objects;


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
	private ClientThread clientThread;

	@Inject
	private ValuableDropPricesConfig config;

	// VARBIT 5399 = Valuable Drop Notifications!
	private static final int VALUABLE_DROP_SETTING_VAR = 5399;

	@Provides
	ValuableDropPricesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ValuableDropPricesConfig.class);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			clientThread.invoke(() -> {
				client.setVarbitValue(client.getVarps(), VALUABLE_DROP_SETTING_VAR, 0); // 1 == enabled, 0 == disabled. I don't think this is working tho? It's not fucking working
			});
		}
	}

	@Subscribe
	public void onItemSpawned(ItemSpawned itemSpawned) {
		TileItem item = itemSpawned.getItem();


		// Check if item is owned by player and if the item has just spawned
		if (item.getOwnership() == 1) {
			if (item.getDespawnTime() >= 250) {
				int itemId = item.getId();
				int quantity = item.getQuantity();

				handleValuableDrop(itemId, quantity);
			}
		}

	}

	private void handleValuableDrop(int itemId, int quantity) {
		// Fetch GE and HA value
		ItemPriceInfo priceInfo = fetchItemPrices(itemId);

		Color messageColour = config.valuableDropColour();

		int totalHAValue = priceInfo.getHAValue() * quantity;
		int totalGEValue = priceInfo.getGEValue() * quantity;

		// If value is greater than 0, then add item value and gp. If not then make it say "no value"
		String geValueString = totalGEValue > 0 ? totalGEValue + " gp" : "no value";
		String haValueString = totalHAValue > 0 ? totalHAValue + " gp" : "no value";

		// Check if threshold and preference for valuable item
		int valuableDropThreshold = config.valuableDropThreshold();
		boolean isValuable = false;

		if (config.valuableConsideration() == ValuableDropConsiderationType.HIGH_ALCH) {
			isValuable = totalHAValue >= valuableDropThreshold;
		} else if (config.valuableConsideration() == ValuableDropConsiderationType.GRAND_EXCHANGE) {
			isValuable = totalGEValue >= valuableDropThreshold;
		} else if (config.valuableConsideration() == ValuableDropConsiderationType.BOTH) {
			isValuable = (totalGEValue >= valuableDropThreshold || totalHAValue >= valuableDropThreshold);
		}

		if (isValuable || geValueString.equals("no value") && haValueString.equals("no value")) {
			// Format message based on user configuration
			String message = ColorUtil.prependColorTag(formatDropMessage(itemManager.getItemComposition(itemId).getName(), haValueString, geValueString), messageColour);

			// Send message in chat
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", message, null);
		}
	}

	private ItemPriceInfo fetchItemPrices(int itemId) {
		ItemPriceInfo priceInfo = new ItemPriceInfo();

		// Use ItemManager to fetch GE value
		int geValue = itemManager.getItemPrice(itemId);
		priceInfo.setGEValue(geValue);

		// Use ItemComposition to fetch HA value
		ItemComposition itemComposition = client.getItemDefinition(itemId);
		int haValue = itemComposition.getHaPrice();
		priceInfo.setHAValue(haValue);

		return priceInfo;
	}

	private String formatDropMessage(String itemName, String haValue, String geValue) {
		ValuableDropPriceDisplayType displayType = config.displayPrices();

		/*
			Figure out a way to limit displaytype or considerationtype based on the other so the player doesn't
			end up having a message like "Valuable Drop: Dragon Born (GE: No Value)" because they have the
			displaytype set to GE but considerationtype is set to HA.
			a.k.a make it more obvious why something not valuable showed up as a valuable drop due to displaytype and considerationtype not being a match.
		 */
		if (!Objects.equals(haValue, "no value") && !Objects.equals(geValue, "no value")) {
			switch (displayType) {
				case GRAND_EXCHANGE:
					return String.format("Valuable Drop: %s (GE: %s)", itemName, geValue);
				case HIGH_ALCH:
					return String.format("Valuable Drop: %s (HA: %s)", itemName, haValue);
				case BOTH:
					return String.format("Valuable Drop: %s (GE: %s, HA: %s)", itemName, geValue, haValue);

				default:
					return "Valuable Drop: " + itemName;
			}
		} else {
			return "Untradeable Drop: " + itemName;
		}
	}

	// Store GE and HA Value
	private class ItemPriceInfo {
		private int haValue;
		private int geValue;

		public int getHAValue() {
			return haValue;
		}

		public void setHAValue(int haValue) {
			this.haValue = haValue;
		}

		public int getGEValue() {
			return geValue;
		}

		public void setGEValue(int geValue) {
			this.geValue = geValue;
		}
	}

}
