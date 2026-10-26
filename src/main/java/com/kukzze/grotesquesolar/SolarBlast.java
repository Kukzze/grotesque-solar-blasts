package com.kukzze.grotesquesolar;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.RuneLiteObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;

@Getter
public class SolarBlast
{
	private final LocalPoint targetPoint;
	private final WorldPoint worldPoint;
	private final int startCycle;
	private final int endCycle;

	@Setter
	private RuneLiteObject runeLiteObject;
	private boolean despawned;

	public SolarBlast(LocalPoint targetPoint, WorldPoint worldPoint, int startCycle, int endCycle)
	{
		this.targetPoint = targetPoint;
		this.worldPoint = worldPoint;
		this.startCycle = startCycle;
		this.endCycle = endCycle;
		this.despawned = false;
	}

	public float getProgress(int currentCycle)
	{
		int total = endCycle - startCycle;
		if (total <= 0)
		{
			return 1.0f;
		}
		int elapsed = currentCycle - startCycle;
		return Math.min(1.0f, Math.max(0.0f, (float) elapsed / (float) total));
	}

	public boolean isExpired(int currentCycle)
	{
		return currentCycle >= endCycle + 15; // Linger slightly for impact effect
	}

	public void despawn()
	{
		if (despawned)
		{
			return;
		}
		despawned = true;
		if (runeLiteObject != null)
		{
			runeLiteObject.setActive(false);
			runeLiteObject = null;
		}
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SolarBlast that = (SolarBlast) o;
		return startCycle == that.startCycle &&
			endCycle == that.endCycle &&
			Objects.equals(worldPoint, that.worldPoint);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(worldPoint, startCycle, endCycle);
	}
}
