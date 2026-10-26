package com.kukzze.grotesquesolar;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Animation;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.GraphicsObject;
import net.runelite.api.JagexColor;
import net.runelite.api.Model;
import net.runelite.api.ModelData;
import net.runelite.api.Projectile;
import net.runelite.api.RuneLiteObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GraphicsObjectCreated;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@Singleton
@PluginDescriptor(
	name = "Grotesque Solar Blasts",
	description = "Replaces Dusk's falling rocks at Grotesque Guardians with Sol Heredit's sunfire beams.",
	tags = {"grotesque", "guardians", "dusk", "sol", "colosseum", "transmog", "visual", "rocks"}
)
public class GrotesqueSolarPlugin extends Plugin
{
	private static final int DUSK_FALLING_ROCK_PROJECTILE = 1435;
	private static final int BEAM_MODEL_ID = 43330;

	private static final Set<Integer> ROCK_SHADOW_SPOTANIMS = ImmutableSet.of(
		1446, 1447, 1448, 1449,
		1889, 1890,
		1938, 1939,
		2111,
		2299,
		2380
	);

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private GrotesqueSolarOverlay overlay;

	@Inject
	private GrotesqueSolarConfig config;

	@Getter
	private final List<SolarBlast> activeBlasts = Collections.synchronizedList(new ArrayList<>());

	@Provides
	GrotesqueSolarConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(GrotesqueSolarConfig.class);
	}

	@Override
	protected void startUp()
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(overlay);
		clientThread.invoke(this::clearActiveBlasts);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() != GameState.LOGGED_IN)
		{
			clearActiveBlasts();
		}
	}

	@Subscribe
	public void onProjectileMoved(ProjectileMoved event)
	{
		Projectile projectile = event.getProjectile();
		if (projectile == null || projectile.getId() != DUSK_FALLING_ROCK_PROJECTILE)
		{
			return;
		}

		LocalPoint targetPoint = event.getPosition();
		if (targetPoint == null)
		{
			return;
		}

		WorldPoint worldPoint = WorldPoint.fromLocal(client, targetPoint);
		int startCycle = projectile.getStartCycle();
		int endCycle = projectile.getEndCycle();

		// Check if we're already tracking this strike
		synchronized (activeBlasts)
		{
			for (SolarBlast existing : activeBlasts)
			{
				if (existing.getWorldPoint().equals(worldPoint) && existing.getEndCycle() == endCycle)
				{
					return;
				}
			}
		}

		if (config.hideOriginalRock())
		{
			projectile.setEndCycle(client.getGameCycle());
		}

		SolarBlast blast = new SolarBlast(targetPoint, worldPoint, startCycle, endCycle);
		activeBlasts.add(blast);

		clientThread.invoke(() -> spawnSolarBeam(blast));
	}

	@Subscribe
	public void onGraphicsObjectCreated(GraphicsObjectCreated event)
	{
		if (!config.hideOriginalRock())
		{
			return;
		}

		GraphicsObject graphicsObject = event.getGraphicsObject();
		if (graphicsObject != null && ROCK_SHADOW_SPOTANIMS.contains(graphicsObject.getId()))
		{
			graphicsObject.setFinished(true);
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		int currentCycle = client.getGameCycle();

		synchronized (activeBlasts)
		{
			Iterator<SolarBlast> it = activeBlasts.iterator();
			while (it.hasNext())
			{
				SolarBlast blast = it.next();
				if (blast.isExpired(currentCycle))
				{
					blast.despawn();
					it.remove();
				}
			}
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!"grotesquesolar".equals(event.getGroup()))
		{
			return;
		}

		if ("beamStyle".equals(event.getKey()) || "beamColor".equals(event.getKey()))
		{
			clientThread.invoke(() ->
			{
				synchronized (activeBlasts)
				{
					for (SolarBlast blast : activeBlasts)
					{
						if (blast.getRuneLiteObject() != null && !blast.isDespawned())
						{
							updateBeamVisuals(blast.getRuneLiteObject());
						}
					}
				}
			});
		}
	}

	private void spawnSolarBeam(SolarBlast blast)
	{
		if (blast.isDespawned())
		{
			return;
		}

		LocalPoint target = blast.getTargetPoint();
		if (target == null)
		{
			return;
		}

		RuneLiteObject beam = client.createRuneLiteObject();
		updateBeamVisuals(beam);
		beam.setLocation(target, client.getPlane());
		beam.setActive(true);

		blast.setRuneLiteObject(beam);
	}

	private void updateBeamVisuals(RuneLiteObject beam)
	{
		Model model = createBeamModel(config.beamColor());
		if (model != null)
		{
			beam.setModel(model);
		}

		Animation anim = client.loadAnimation(config.beamStyle().getAnimationId());
		if (anim != null)
		{
			beam.setAnimation(anim);
			beam.setShouldLoop(true);
		}
	}

	private Model createBeamModel(Color color)
	{
		ModelData md = client.loadModelData(BEAM_MODEL_ID);
		if (md != null)
		{
			try
			{
				short hsl = JagexColor.rgbToHSL(color.getRGB(), 1.0d);
				int hue = JagexColor.unpackHue(hsl);
				int sat = JagexColor.unpackSaturation(hsl);
				int lum = JagexColor.unpackLuminance(hsl);
				int satDelta = sat > 2 ? 1 : 0;

				return md.cloneColors()
					.recolor((short) 26432, JagexColor.packHSL(hue, sat - satDelta, lum))
					.recolor((short) 26584, JagexColor.packHSL(hue, sat, Math.min(lum + 24, JagexColor.LUMINANCE_MAX)))
					.light(75 + ModelData.DEFAULT_AMBIENT, 1875 + ModelData.DEFAULT_CONTRAST,
						ModelData.DEFAULT_X, ModelData.DEFAULT_Y, ModelData.DEFAULT_Z);
			}
			catch (Exception e)
			{
				log.debug("Falling back to unshaded beam model", e);
			}
		}

		return client.loadModel(BEAM_MODEL_ID);
	}

	private void clearActiveBlasts()
	{
		synchronized (activeBlasts)
		{
			for (SolarBlast blast : activeBlasts)
			{
				blast.despawn();
			}
			activeBlasts.clear();
		}
	}
}
