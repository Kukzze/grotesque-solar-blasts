package com.kukzze.grotesquesolar;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class GrotesqueSolarPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(GrotesqueSolarPlugin.class);
		RuneLite.main(args);
	}
}
