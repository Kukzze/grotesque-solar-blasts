package com.kukzze.grotesquesolar;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.gameval.AnimationID;

@Getter
@RequiredArgsConstructor
public enum SolarStyle
{
	SUNFIRE_LIGHTNING("Sunfire Lightning", AnimationID.VFX_COLOSSEUM_SUNFIRE_LIGHTNING_01_BEAM_01),
	CRYSTAL_BEAM("Crystal Beam", AnimationID.VFX_COLOSSEUM_CRYSTAL_ATTACK_01_BEAM_01),
	SOLAR_PILLAR("Solar Pillar", AnimationID.FX_BEAM_IDLE);

	private final String name;
	private final int animationId;

	@Override
	public String toString()
	{
		return name;
	}
}
