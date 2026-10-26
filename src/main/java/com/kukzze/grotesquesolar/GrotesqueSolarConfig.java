package com.kukzze.grotesquesolar;

import java.awt.Color;
import net.runelite.client.config.Alpha;
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
		name = "Ground Reticle",
		description = "Target ground tile indicators",
		position = 1
	)
	String reticleSection = "reticleSection";

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
		keyName = "showGroundReticle",
		name = "Show Ground Reticle",
		description = "Draws a sunfire reticle on tiles targeted by falling rocks.",
		position = 0,
		section = reticleSection
	)
	default boolean showGroundReticle()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showCountdown",
		name = "Show Impact Timer",
		description = "Displays the remaining ticks until the beam strikes the ground.",
		position = 1,
		section = reticleSection
	)
	default boolean showCountdown()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
		keyName = "reticleColor",
		name = "Reticle Color",
		description = "Color of the ground targeting circle.",
		position = 2,
		section = reticleSection
	)
	default Color reticleColor()
	{
		return new Color(255, 140, 0, 150);
	}

	@Alpha
	@ConfigItem(
		keyName = "reticleFillColor",
		name = "Reticle Fill Color",
		description = "Fill color inside the ground target indicator.",
		position = 3,
		section = reticleSection
	)
	default Color reticleFillColor()
	{
		return new Color(255, 170, 0, 35);
	}
}
