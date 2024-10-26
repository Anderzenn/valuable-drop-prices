package com.anderzenn.valuabledropprices;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ValuableDropPricesPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(ValuableDropPricesPlugin.class);
		RuneLite.main(args);
	}
}