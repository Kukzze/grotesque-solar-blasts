package com.kukzze.grotesquesolar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

@Singleton
public class GrotesqueSolarOverlay extends Overlay
{
	private static final Stroke RETICLE_STROKE = new BasicStroke(1.5f);

	private final Client client;
	private final GrotesqueSolarPlugin plugin;
	private final GrotesqueSolarConfig config;

	@Inject
	public GrotesqueSolarOverlay(Client client, GrotesqueSolarPlugin plugin, GrotesqueSolarConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
		setPriority(OverlayPriority.MED);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.showGroundReticle() || plugin.getActiveBlasts().isEmpty())
		{
			return null;
		}

		int currentCycle = client.getGameCycle();

		for (SolarBlast blast : plugin.getActiveBlasts())
		{
			if (blast.getTargetPoint() == null || blast.isDespawned())
			{
				continue;
			}

			Polygon poly = Perspective.getCanvasTilePoly(client, blast.getTargetPoint());
			if (poly == null)
			{
				continue;
			}

			// Render ground reticle
			OverlayUtil.renderPolygon(graphics, poly, config.reticleColor(), config.reticleFillColor(), RETICLE_STROKE);

			// Render remaining tick countdown
			if (config.showCountdown())
			{
				int remainingTicks = blast.getRemainingTicks(currentCycle);
				if (remainingTicks > 0)
				{
					String text = String.valueOf(remainingTicks);
					Point textLoc = Perspective.getCanvasTextLocation(client, graphics, blast.getTargetPoint(), text, 0);
					if (textLoc != null)
					{
						OverlayUtil.renderTextLocation(graphics, textLoc, text, Color.WHITE);
					}
				}
			}
		}

		return null;
	}
}
