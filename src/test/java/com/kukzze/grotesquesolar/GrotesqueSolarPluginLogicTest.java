package com.kukzze.grotesquesolar;

import java.lang.reflect.Field;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.GraphicsObject;
import net.runelite.api.Projectile;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GraphicsObjectCreated;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.ui.overlay.OverlayManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrotesqueSolarPluginLogicTest
{
	@Mock
	private Client client;

	@Mock
	private ClientThread clientThread;

	@Mock
	private OverlayManager overlayManager;

	@Mock
	private GrotesqueSolarOverlay overlay;

	@Mock
	private GrotesqueSolarConfig config;

	private GrotesqueSolarPlugin plugin;

	@BeforeEach
	void setUp() throws Exception
	{
		plugin = new GrotesqueSolarPlugin();

		setField(plugin, "client", client);
		setField(plugin, "clientThread", clientThread);
		setField(plugin, "overlayManager", overlayManager);
		setField(plugin, "overlay", overlay);
		setField(plugin, "config", config);

		lenient().when(config.hideOriginalRock()).thenReturn(true);
		lenient().when(config.beamStyle()).thenReturn(SolarStyle.SUNFIRE_LIGHTNING);
	}

	@Test
	void testIgnoresNonDuskProjectiles()
	{
		Projectile projectile = mock(Projectile.class);
		when(projectile.getId()).thenReturn(9999);

		ProjectileMoved event = new ProjectileMoved();
		event.setProjectile(projectile);
		event.setPosition(new LocalPoint(100, 200));

		plugin.onProjectileMoved(event);

		assertTrue(plugin.getActiveBlasts().isEmpty());
		verify(projectile, never()).setEndCycle(anyInt());
	}

	@Test
	void testInterceptsDuskFallingRock()
	{
		int currentCycle = 500;
		int endCycle = 590;
		when(client.getGameCycle()).thenReturn(currentCycle);

		Projectile projectile = mock(Projectile.class);
		when(projectile.getId()).thenReturn(1435);
		when(projectile.getStartCycle()).thenReturn(currentCycle);
		when(projectile.getEndCycle()).thenReturn(endCycle);

		LocalPoint localPoint = new LocalPoint(1024, 2048);
		WorldPoint worldPoint = new WorldPoint(10, 20, 0);

		// Mock WorldPoint.fromLocal(client, localPoint) through client top-level worldview
		when(client.getBaseX()).thenReturn(0);
		when(client.getBaseY()).thenReturn(0);
		when(client.getPlane()).thenReturn(0);

		ProjectileMoved event = new ProjectileMoved();
		event.setProjectile(projectile);
		event.setPosition(localPoint);

		plugin.onProjectileMoved(event);

		assertEquals(1, plugin.getActiveBlasts().size());
		SolarBlast blast = plugin.getActiveBlasts().get(0);
		assertEquals(localPoint, blast.getTargetPoint());

		// Verify rock was hidden
		verify(projectile).setEndCycle(currentCycle);

		// Verify beam spawn was scheduled on client thread
		verify(clientThread).invoke(any(Runnable.class));
	}

	@Test
	void testDespawnsRockShadowGraphics()
	{
		GraphicsObject shadow = mock(GraphicsObject.class);
		when(shadow.getId()).thenReturn(1446); // gargboss_debris_shadow_90

		GraphicsObjectCreated event = new GraphicsObjectCreated(shadow);
		plugin.onGraphicsObjectCreated(event);

		verify(shadow).setFinished(true);

		GraphicsObject otherGraphic = mock(GraphicsObject.class);
		when(otherGraphic.getId()).thenReturn(9999);
		plugin.onGraphicsObjectCreated(new GraphicsObjectCreated(otherGraphic));

		verify(otherGraphic, never()).setFinished(true);
	}

	@Test
	void testCleansUpExpiredBlastsOnGameTick()
	{
		LocalPoint localPoint = new LocalPoint(100, 200);
		WorldPoint worldPoint = new WorldPoint(10, 20, 0);
		SolarBlast blast = new SolarBlast(localPoint, worldPoint, 100, 200);
		plugin.getActiveBlasts().add(blast);

		// Cycle before expiration
		when(client.getGameCycle()).thenReturn(210);
		plugin.onGameTick(new GameTick());
		assertEquals(1, plugin.getActiveBlasts().size());

		// Cycle after expiration (endCycle 200 + 15 = 215)
		when(client.getGameCycle()).thenReturn(220);
		plugin.onGameTick(new GameTick());
		assertTrue(plugin.getActiveBlasts().isEmpty());
	}

	@Test
	void testClearsActiveBlastsOnWorldHop()
	{
		LocalPoint localPoint = new LocalPoint(100, 200);
		WorldPoint worldPoint = new WorldPoint(10, 20, 0);
		plugin.getActiveBlasts().add(new SolarBlast(localPoint, worldPoint, 100, 200));

		GameStateChanged event = new GameStateChanged();
		event.setGameState(GameState.HOPPING);
		plugin.onGameStateChanged(event);

		assertTrue(plugin.getActiveBlasts().isEmpty());
	}

	private static void setField(Object target, String fieldName, Object value) throws Exception
	{
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}
}
