package com.example;

import com.dimmer.DimmerPlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class DimmerPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(DimmerPlugin.class);
		RuneLite.main(args);
	}
}