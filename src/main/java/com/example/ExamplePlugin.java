package com.example;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
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

@Slf4j
@PluginDescriptor(
	name = "ZMI Tracker",
	description = "Tracks total and individual runes crafted at the Ourania Altar.",
	tags = {"zmi"}
)
public class ExamplePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ItemManager itemManager;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private ExampleConfig config;

	private ExamplePanel panel;
	private NavigationButton navButton;
	private final Map<Integer, Integer> runeCounts = new HashMap<>();

	@Override
	protected void startUp() throws Exception
	{
		panel = new ExamplePanel(itemManager);
		navButton = NavigationButton.builder()
				.tooltip("ZMI Tracker")
				.icon(ImageUtil.loadImageResource(getClass(),"/zmi_icon.png"))
				.priority(5)
				.panel(panel)
				.build();
		clientToolbar.addNavigation(navButton);
		log.info("Example started!");
	}

	@Override

	protected void shutDown() throws Exception
	{
		clientToolbar.removeNavigation(navButton);
		log.info("Example stopped!");
	}

	@Subscribe
	public void onChatMessage(ChatMessage event) {
		if (event.getType() == ChatMessageType.GAMEMESSAGE) {
			String message = event.getMessage();

			// Check for regular essence crafting message
			if (message.contains("You bind the temple's power into")) {
				processRuneCraftingMessage(message);
			}
			// Check for Daeyalt essence crafting message
			else if (message.contains("You bind the temple's power into runes and gain some Runecraft experience.")) {
				processRuneCraftingMessage(message);
			}
		}
	}

	private void processRuneCraftingMessage(String message) {
		String[] parts = message.split(" ");
		int count = Integer.parseInt(parts[parts.length - 2]);
		int runeId = getRuneIdFromName(parts[parts.length - 1]);

		if (runeId != -1) {
			runeCounts.put(runeId, runeCounts.getOrDefault(runeId, 0) + count);
			panel.updateRuneCount(runeId, runeCounts.get(runeId));

			int total = runeCounts.values().stream().mapToInt(Integer::intValue).sum();
			panel.updateTotalRunes(total);
		}
	}

	private int getRuneIdFromName(String runeName) {
		switch (runeName.toLowerCase()) {
			case "air": return ItemID.AIR_RUNE;
			case "water": return ItemID.WATER_RUNE;
			case "earth": return ItemID.EARTH_RUNE;
			case "fire": return ItemID.FIRE_RUNE;
			case "mind": return ItemID.MIND_RUNE;
			case "chaos": return ItemID.CHAOS_RUNE;
			case "death": return ItemID.DEATH_RUNE;
			case "blood": return ItemID.BLOOD_RUNE;
			case "cosmic": return ItemID.COSMIC_RUNE;
			case "nature": return ItemID.NATURE_RUNE;
			case "law": return ItemID.LAW_RUNE;
			case "body": return ItemID.BODY_RUNE;
			case "soul": return ItemID.SOUL_RUNE;
			case "astral": return ItemID.ASTRAL_RUNE;
			default: return -1;
		}
	}

	//public void onGameStateChanged(GameStateChanged gameStateChanged)
	//{
	//	if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
	//	{
	//		client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
	//	}
	//}

	@Provides
	ExampleConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ExampleConfig.class);
	}
}
