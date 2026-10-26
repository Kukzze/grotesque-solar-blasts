package com.kukzze.grotesquesolar;

import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SolarBlastTest
{
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
