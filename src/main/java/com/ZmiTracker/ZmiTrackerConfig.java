package com.ZmiTracker;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("example")
public interface ZmiTrackerConfig extends Config
{
	@ConfigItem(
		keyName = "showTotalRunes",
		name = "Show total runes?",
		description = "Shows total runes made."
	)
	default boolean showTotalRunes() {
		return true;
	}
	@ConfigItem(
			keyName = "showRuneTypes",
			name = "Show all rune types?",
			description = "Shows individual runes made."
	)
	default boolean showRuneTypes() {
		return true;
	}
	default String greeting()
	{
		return "Placeholder";
	}
}
