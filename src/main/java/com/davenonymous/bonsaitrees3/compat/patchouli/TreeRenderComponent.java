package com.davenonymous.bonsaitrees3.compat.patchouli;

import com.davenonymous.bonsaitrees3.client.TreeModelRenderer;
import com.davenonymous.bonsaitrees3.client.TreeModels;
import com.davenonymous.bonsaitrees3.registry.sapling.SaplingRecipeHelper;
import com.davenonymous.bonsaitrees3.setup.Registration;
import com.davenonymous.libnonymous.render.MultiBlockBlockAndTintGetter;
import com.davenonymous.libnonymous.render.MultiBlockBlockColors;
import com.davenonymous.libnonymous.render.MultiblockBakedModel;
import com.davenonymous.libnonymous.serialization.MultiblockBlockModel;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.data.ModelData;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.lwjgl.opengl.GL11;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import java.util.function.UnaryOperator;

public class TreeRenderComponent implements ICustomComponent {
	private transient int x, y;
	private transient ItemStack sapling;
	private transient MultiblockBakedModel bakedModel;
	private transient MultiblockBlockModel treeModel;

	public IVariable item;

	public float scale = 100.0f;

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		this.x = componentX;
		this.y = componentY;
	}

	@Override
	public void render(GuiGraphics guiGraphics, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
		if(bakedModel == null) {
			return;
		}

		var mc = Minecraft.getInstance();
		guiGraphics.pose().pushPose();
		guiGraphics.pose().translate(x+50, y+50, 100.0f);
		guiGraphics.pose().scale(scale, scale, scale);

		float scale = (float) treeModel.getScaleRatio(true);
		guiGraphics.pose().scale(scale, scale, scale);

		guiGraphics.pose().mulPose(new Quaternionf(new AxisAngle4f((float)Math.toRadians(-25.0f + 180.0f), 1, 0, 0)));

		int rotationDurationTicks = 15 * 20;
		long worldTicks = mc.level.getGameTime();
		int smallWorldTicks = (int)(worldTicks % rotationDurationTicks);
		double progressTicks = smallWorldTicks + pticks;
		double percent = progressTicks / rotationDurationTicks;
		guiGraphics.pose().mulPose(new Quaternionf(new AxisAngle4f((float)Math.toRadians(percent * 360), 0, 1, 0)));

		guiGraphics.pose().translate((treeModel.width + 1) / -2.0f, (treeModel.height + 1) / -2.0f, (treeModel.depth + 1) / -2.0f);

		MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
		var buffer = bufferSource.getBuffer(RenderType.cutout());

		GL11.glFrontFace(GL11.GL_CW);




		MultiBlockBlockAndTintGetter fakeLevel = new MultiBlockBlockAndTintGetter(treeModel, mc.level, BlockPos.ZERO);
		var mr = new TreeModelRenderer(new MultiBlockBlockColors(treeModel));

		if(mc.options.graphicsMode().get().getId() >= GraphicsStatus.FANCY.getId()) {
			mr.tesselateWithAO(fakeLevel, bakedModel, Blocks.ACACIA_LEAVES.defaultBlockState(), BlockPos.ZERO, guiGraphics.pose(), buffer, true, mc.level.random, 0, 15, ModelData.EMPTY, RenderType.cutout());
		} else {
			mr.tesselateWithoutAO(fakeLevel, bakedModel, Blocks.ACACIA_LEAVES.defaultBlockState(), BlockPos.ZERO, guiGraphics.pose(), buffer, true, mc.level.random, 0, 192, ModelData.EMPTY, RenderType.cutout());
		}

		bufferSource.endBatch();
		GL11.glFrontFace(GL11.GL_CCW);
		guiGraphics.pose().popPose();
	}

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
		sapling = lookup.apply(item).as(ItemStack.class);

		if(sapling == null || sapling.isEmpty()) {
			return;
		}

		var mc = Minecraft.getInstance();
		var saplingInfo = Registration.RECIPE_HELPER_SAPLING.get().getSaplingInfoForItem(mc.level, sapling);
		treeModel = TreeModels.get(saplingInfo.getId());
		if(treeModel == null) {
			return;
		}

		bakedModel = MultiblockBakedModel.of(treeModel);
	}
}