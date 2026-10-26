package com.kukzze.grotesquesolar;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("grotesquesolar")
public interface GrotesqueSolarConfig extends Config
{
	@ConfigSection(
		name = "Beam Settings",
		description = "Visual light beam customization",
		position = 0
	)
	String beamSection = "beamSection";

	@ConfigSection(
		name = "Discord Notifications",
		description = "Send kill notifications to a Discord channel",
		position = 1,
		closedByDefault = true
	)
	String discordSection = "discordSection";

	@ConfigItem(
		keyName = "hideOriginalRock",
		name = "Hide Falling Rocks",
		description = "Despawns Dusk's falling rock debris so only the solar beam is visible.",
		position = 0,
		section = beamSection
	)
	default boolean hideOriginalRock()
	{
		return true;
	}

	@ConfigItem(
		keyName = "beamStyle",
		name = "Beam Style",
		description = "Choose which Sol Heredit / Colosseum beam animation to use.",
		position = 1,
		section = beamSection
	)
	default SolarStyle beamStyle()
	{
		return SolarStyle.SUNFIRE_LIGHTNING;
	}

	@ConfigItem(
		keyName = "beamColor",
		name = "Beam Tint",
		description = "Color tint applied to the descending light beam.",
		position = 2,
		section = beamSection
	)
	default Color beamColor()
	{
		return new Color(255, 185, 35);
	}

	@ConfigItem(
		keyName = "discordWebhook",
		name = "Webhook URL",
		description = "Discord channel webhook URL.",
		position = 0,
		section = discordSection
	)
	default String discordWebhook()
	{
		return "";
	}

	@ConfigItem(
		keyName = "notifyOnKill",
		name = "Notify on Kill",
		description = "Posts an embed whenever Grotesque Guardians is defeated.",
		position = 1,
		section = discordSection
	)
	default boolean notifyOnKill()
	{
		return false;
	}

	@ConfigItem(
		keyName = "sendPersonalBest",
		name = "Notify on PB",
		description = "Highlights new personal best kill times in Discord.",
		position = 2,
		section = discordSection
	)
	default boolean sendPersonalBest()
	{
		return true;
	}
}
