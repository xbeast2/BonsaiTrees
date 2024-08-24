package com.davenonymous.bonsaitrees3.datagen.server;

import com.davenonymous.bonsaitrees3.setup.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DatagenBlockTags extends BlockTagsProvider {
	public DatagenBlockTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
		super(packOutput, lookupProvider, modId, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider pProvider) {
		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(Registration.BONSAI_POT.get());
		tag(BlockTags.NEEDS_STONE_TOOL).add(Registration.BONSAI_POT.get());
	}
	@Override
	public String getName() {
		return "Bonsai Trees BlockTags";
	}
}