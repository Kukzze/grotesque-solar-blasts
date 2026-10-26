package com.kukzze.grotesquesolar;

import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SolarBlastTest
{
	@Test
	void testRemainingTicksCalculation()
	{
		LocalPoint localPoint = new LocalPoint(100, 200);
		WorldPoint worldPoint = new WorldPoint(10, 20, 0);
		SolarBlast blast = new SolarBlast(localPoint, worldPoint, 100, 190);

		// At start cycle 100, remaining cycles = 90 -> 90 / 30 = 3 ticks
		assertEquals(3, blast.getRemainingTicks(100));

		// At cycle 140, remaining cycles = 50 -> 50 / 30 = 2 ticks (ceiling)
		assertEquals(2, blast.getRemainingTicks(140));

		// At or past end cycle 190, remaining = 0
		assertEquals(0, blast.getRemainingTicks(190));
		assertEquals(0, blast.getRemainingTicks(200));
	}

	@Test
	void testProgressCalculation()
	{
		LocalPoint localPoint = new LocalPoint(100, 200);
		WorldPoint worldPoint = new WorldPoint(10, 20, 0);
		SolarBlast blast = new SolarBlast(localPoint, worldPoint, 100, 200);

		assertEquals(0.0f, blast.getProgress(100), 0.001f);
		assertEquals(0.5f, blast.getProgress(150), 0.001f);
		assertEquals(1.0f, blast.getProgress(200), 0.001f);
		assertEquals(1.0f, blast.getProgress(250), 0.001f);
	}

	@Test
	void testExpiration()
	{
		LocalPoint localPoint = new LocalPoint(100, 200);
		WorldPoint worldPoint = new WorldPoint(10, 20, 0);
		SolarBlast blast = new SolarBlast(localPoint, worldPoint, 100, 200);

		assertFalse(blast.isExpired(200));
		assertFalse(blast.isExpired(214));
		assertTrue(blast.isExpired(215));
	}
}
