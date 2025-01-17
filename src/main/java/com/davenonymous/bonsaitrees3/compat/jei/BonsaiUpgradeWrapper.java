package com.davenonymous.bonsaitrees3.compat.jei;

import com.davenonymous.libnonymous.base.BaseLanguageProvider;
import com.davenonymous.libnonymous.helper.Translatable;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Style;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.List;

public class BonsaiUpgradeWrapper {
	List<ItemStack> upgradeItems;
	List<TagKey<Item>> upgradeItemTags;

	Translatable upgradeDescription;

	public BonsaiUpgradeWrapper(Translatable upgradeDescription, ItemStack... upgradeItem) {
		this.upgradeItems = Arrays.stream(upgradeItem).toList();
		this.upgradeDescription = upgradeDescription;
	}

	public BonsaiUpgradeWrapper(Translatable upgradeDescription, List<ItemStack> upgradeItems) {
		this.upgradeItems = upgradeItems;
		this.upgradeDescription = upgradeDescription;
	}

	public BonsaiUpgradeWrapper(Translatable upgradeDescription, TagKey<Item>... upgradeTags) {
		this.upgradeItemTags = Arrays.stream(upgradeTags).toList();
		this.upgradeDescription = upgradeDescription;
	}

	public void setRecipe(IRecipeLayoutBuilder builder, IFocusGroup focuses) {
		var upgradeSlot = builder.addSlot(RecipeIngredientRole.CATALYST, 1, 1);
		if(upgradeItemTags != null) {
			for(var item : upgradeItemTags) {
				upgradeSlot.addIngredients(Ingredient.of(item));
			}
			return;
		}

		upgradeSlot.addItemStacks(upgradeItems);
    }

	public void draw(IRecipeSlotsView view, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		var mc = Minecraft.getInstance();
        var font = mc.font;
		var text = I18n.get(BaseLanguageProvider.getTranslatableLanguageKey(upgradeDescription));
		var split = font.getSplitter().splitLines(text, 120, Style.EMPTY);

		var yOffset = 0;
		if(split.size() == 1) {
			yOffset = 5;
		} else if(split.size() == 2) {
			yOffset = 1;
		} else if(split.size() == 3) {
			yOffset = -3;
		}

		for(var line : split) {
			guiGraphics.drawString(mc.font, line.getString(), 24, yOffset, 0xFFFFFF);
			yOffset += 9;
		}
	}
}