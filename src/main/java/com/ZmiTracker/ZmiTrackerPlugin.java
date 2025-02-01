package com.ZmiTracker;

import com.google.inject.Provides;
import net.runelite.api.*;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@PluginDescriptor(
	name = "ZMI Tracker",
	description = "Tracks total and individual runes crafted at the Ourania Altar.",
		tags = {"zmi", "ourania", "altar", "runecrafting", "tracker"}
)
public class ZmiTrackerPlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private ItemManager itemManager;

	@Inject
	private ZmiTrackerConfig config;

	private ZmiTrackerPanel panel;
	private NavigationButton navButton;
	private final Map<Integer, Integer> runeCounts = new HashMap<>();
	private Item[] previousInventory;

	@Override
	protected void startUp() {
		panel = new ZmiTrackerPanel(itemManager);
		navButton = NavigationButton.builder()
				.tooltip("ZMI Tracker")
				.icon(ImageUtil.loadImageResource(getClass(), "/zmi_icon.png"))
				.priority(5)
				.panel(panel)
				.build();
		clientToolbar.addNavigation(navButton);

		// Initialize previous inventory
		if (client.getItemContainer(InventoryID.INVENTORY) != null) {
			previousInventory = client.getItemContainer(InventoryID.INVENTORY).getItems();
		} else {
			previousInventory = new Item[0];
		}
	}

	@Override
	protected void shutDown() {
		clientToolbar.removeNavigation(navButton);
	}


	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event) {
		if (event.getContainerId() == InventoryID.INVENTORY.getId()) {
			Item[] currentInventory = event.getItemContainer().getItems();

			// Compare previous and current inventory to detect changes
			for (Item currentItem : currentInventory) {
				if (currentItem.getId() != -1 && isRune(currentItem.getId())) {
					int runeId = currentItem.getId();
					int previousCount = countItemInInventory(previousInventory, runeId);
					int currentCount = currentItem.getQuantity();

					if (currentCount > previousCount) {
						int difference = currentCount - previousCount;
						runeCounts.put(runeId, runeCounts.getOrDefault(runeId, 0) + difference);

						// Update the panel
						panel.updateRuneCount(runeId, runeCounts.get(runeId));

						// Update total runes
						int total = runeCounts.values().stream().mapToInt(Integer::intValue).sum();
						panel.updateTotalRunes(total);
					}
				}
			}

			// Update previous inventory
			previousInventory = currentInventory;
		}
	}

	private boolean isRune(int itemId) {
		// List of all rune IDs
		int[] runeIds = {
				ItemID.AIR_RUNE, ItemID.WATER_RUNE, ItemID.EARTH_RUNE, ItemID.FIRE_RUNE,
				ItemID.MIND_RUNE, ItemID.CHAOS_RUNE, ItemID.DEATH_RUNE, ItemID.BLOOD_RUNE,
				ItemID.COSMIC_RUNE, ItemID.NATURE_RUNE, ItemID.LAW_RUNE, ItemID.BODY_RUNE,
				ItemID.SOUL_RUNE, ItemID.ASTRAL_RUNE, ItemID.MIST_RUNE, ItemID.MUD_RUNE,
				ItemID.DUST_RUNE, ItemID.LAVA_RUNE, ItemID.STEAM_RUNE, ItemID.SMOKE_RUNE
		};

		for (int runeId : runeIds) {
			if (itemId == runeId) {
				return true;
			}
		}
		return false;
	}

	private int countItemInInventory(Item[] inventory, int itemId) {
		int count = 0;
		for (Item item : inventory) {
			if (item.getId() == itemId) {
				count += item.getQuantity();
			}
		}
		return count;
	}


	//public void onGameStateChanged(GameStateChanged gameStateChanged)
	//{
	//	if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
	//	{
	//		client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
	//	}
	//}

	@Provides
	ZmiTrackerConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ZmiTrackerConfig.class);
	}
}
