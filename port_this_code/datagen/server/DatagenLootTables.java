package com.davenonymous.bonsaitrees3.datagen.server;

import com.davenonymous.libnonymous.base.BaseLootTableProvider;
import com.davenonymous.bonsaitrees3.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Set;

public class DatagenLootTables extends BaseLootTableProvider {

	public DatagenLootTables(PackOutput dataGeneratorIn, Set<ResourceLocation> pRequiredTables, List<SubProviderEntry> pSubProviders) {
		super(dataGeneratorIn, pRequiredTables, pSubProviders);
	}

	@Override
	protected void addTables() {
		this.addTable(Registration.BONSAI_POT.get(), dropSelf(Registration.BONSAI_POT.get()));
	}
}