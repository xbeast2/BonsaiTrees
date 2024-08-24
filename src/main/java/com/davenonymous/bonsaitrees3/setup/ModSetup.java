package com.davenonymous.bonsaitrees3.setup;

import com.davenonymous.bonsaitrees3.network.Networking;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModSetup {
	public static void init(FMLCommonSetupEvent event) {
		Networking.registerMessages();
	}

	public static void buildContents(BuildCreativeModeTabContentsEvent event) {
		// Add to functional tab
		if(event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
			event.accept(Registration.BONSAI_POT);
		}
	}
}