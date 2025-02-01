package com.example;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("example")
public interface ExampleConfig extends Config
{
	@ConfigItem(
		keyName = "showTotalRunes",
		name = "Show total runes?",
		description = "Placeholder"
	)
	default boolean showTotalRunes() {
		return true;
	}
	@ConfigItem(
			keyName = "showRuneTypes",
			name = "Show all rune types?",
			description = "Placeholder"
	)
	default boolean showRuneTypes() {
		return true;
	}
	default String greeting()
	{
		return "Placeholder";
	}
}
