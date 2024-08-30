package com.davenonymous.bonsaitrees3.registry.soil;

import com.davenonymous.bonsaitrees3.BonsaiTrees3;
import com.davenonymous.libnonymous.helper.BlockStateSerializationHelper;
import com.davenonymous.libnonymous.helper.FluidStateSerializationHelper;
import com.davenonymous.libnonymous.serialization.JsonHelpers;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

import javax.annotation.Nullable;


public class SoilSerializer implements RecipeSerializer<SoilInfo> {

	@Override
	public SoilInfo fromJson(ResourceLocation recipeId, JsonObject json) {
		final Ingredient soil = JsonHelpers.getIngredientFromArrayOrSingle(json.get("soil"));
		if(soil.isEmpty()) {
			BonsaiTrees3.LOGGER.info("Skipping recipe '{}', contains unknown soil ingredient.", recipeId);
			return null;
		}

		float tickModifier = 1.0f;
		if(json.has("tickModifier")) {
			tickModifier = json.get("tickModifier").getAsFloat();
		}

		JsonObject display = json.getAsJsonObject("display");
		SoilInfo result;
		if(display.has("fluid")) {
			FluidState state = FluidStateSerializationHelper.deserializeFluidState(display);
			result = new SoilInfo(recipeId, soil, state, tickModifier);
		} else if (display.has("texture")) {
			ResourceLocation texture = new ResourceLocation(display.get("texture").getAsString());
			result = new SoilInfo(recipeId, soil, texture, tickModifier);
		} else {
			BlockState state = BlockStateSerializationHelper.deserializeBlockState(display);
			result = new SoilInfo(recipeId, soil, state, tickModifier);
		}


		if(json.has("compatibleSoilTags")) {
			JsonArray tagsJson = json.getAsJsonArray("compatibleSoilTags");
			for(JsonElement element : tagsJson) {
				if(!element.isJsonPrimitive()) {
					continue;
				}

				String tag = element.getAsString();
				if(tag == null) {
					continue;
				}

				result.addTag(tag);
			}
		}

		return result;
	}

	@Nullable
	@Override
	public SoilInfo fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
		final SoilInfo.SoilType soilType = buffer.readEnum(SoilInfo.SoilType.class);
		final Ingredient ingredient = Ingredient.fromNetwork(buffer);
		final float tickModifier = buffer.readFloat();

		SoilInfo result;
		switch(soilType) {
			case FLUID -> result = new SoilInfo(recipeId, ingredient, FluidStateSerializationHelper.deserializeFluidState(buffer), tickModifier);
			case TEXTURE -> result = new SoilInfo(recipeId, ingredient, buffer.readResourceLocation(), tickModifier);
			default -> result = new SoilInfo(recipeId, ingredient, BlockStateSerializationHelper.deserializeBlockState(buffer), tickModifier);
		}

		final int tagCount = buffer.readInt();
		for(int i = 0; i < tagCount; i++) {
			result.addTag(buffer.readUtf());
		}

		return result;
	}

	@Override
	public void toNetwork(FriendlyByteBuf buffer, SoilInfo soil) {
		buffer.writeEnum(soil.soilType);
		soil.ingredient.toNetwork(buffer);
		buffer.writeFloat(soil.tickModifier);
		switch(soil.soilType) {
			case FLUID -> FluidStateSerializationHelper.serializeFluidState(buffer, soil.fluidState);
			case TEXTURE -> buffer.writeResourceLocation(soil.soilTexture);
			default -> BlockStateSerializationHelper.serializeBlockState(buffer, soil.blockState);
		}

		buffer.writeInt(soil.tags.size());
		for(String tag : soil.tags) {
			buffer.writeUtf(tag);
		}
	}
}