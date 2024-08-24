package com.davenonymous.bonsaitrees3.registry;

import com.davenonymous.bonsaitrees3.client.TreeModels;
import com.davenonymous.bonsaitrees3.setup.Registration;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {

	@SubscribeEvent
	static void onCommonSetup(FMLCommonSetupEvent event) {
	}


	@SubscribeEvent
	public static void onClientReloadListener(RegisterClientReloadListenersEvent event) {
		var listener = new ResourceManagerReloadListener() {

			@Override
			public void onResourceManagerReload(ResourceManager pResourceManager) {
				TreeModels.init();
			}
		};
		event.registerReloadListener(listener);
	}


}