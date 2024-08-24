package com.davenonymous.bonsaitrees3.datagen;

import com.davenonymous.bonsaitrees3.BonsaiTrees3;
import com.davenonymous.bonsaitrees3.datagen.client.DatagenBlockStates;
import com.davenonymous.bonsaitrees3.datagen.client.DatagenItemModels;
import com.davenonymous.bonsaitrees3.datagen.client.DatagenTranslations;
import com.davenonymous.bonsaitrees3.datagen.server.DatagenBlockTags;
import com.davenonymous.bonsaitrees3.datagen.server.DatagenRecipes;
import com.davenonymous.bonsaitrees3.datagen.server.DatagenSaplings;
import com.davenonymous.bonsaitrees3.datagen.server.DatagenSoils;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BonsaiTrees3.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
	private static void generateServerData(GatherDataEvent event) {
		var lookupProvider = event.getLookupProvider();
		var generator = event.getGenerator();
		var blockTagsProvider = new DatagenBlockTags(generator.getPackOutput(), lookupProvider, BonsaiTrees3.MODID, event.getExistingFileHelper());
		generator.addProvider(true, blockTagsProvider);
		generator.addProvider(true, new DatagenRecipes(generator.getPackOutput()));
		//generator.addProvider(true, new DatagenLootTables(generator.getPackOutput(), ));

		generator.addProvider(true, new DatagenSoils(generator));
		generator.addProvider(true, new DatagenSaplings(generator));
	}

	private static void generateClientData(GatherDataEvent event) {
		var generator = event.getGenerator();
		generator.addProvider(true, new DatagenBlockStates(generator, event.getExistingFileHelper()));
		generator.addProvider(true, new DatagenItemModels(generator, event.getExistingFileHelper()));
		generator.addProvider(true, new DatagenTranslations(generator, "en_us"));
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		var lookupProvider = event.getLookupProvider();
		var generator = event.getGenerator();
		if(event.includeServer()) {
			generateServerData(event);
		}

		if(event.includeClient()) {
			generateClientData(event);
		}
	}
}