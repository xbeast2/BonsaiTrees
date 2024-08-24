package com.davenonymous.bonsaitrees3.client;

import com.davenonymous.libnonymous.render.TintedBakedQuad;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.BitSet;
import java.util.List;

public class TreeModelRenderer extends ModelBlockRenderer {

	public TreeModelRenderer(BlockColors pBlockColors) {
		super(pBlockColors);
	}

	@Override
	public void renderModelFaceAO(BlockAndTintGetter pLevel, BlockState pState, BlockPos pPos, PoseStack pPoseStack, VertexConsumer pConsumer, List<BakedQuad> pQuads, float[] pShape, BitSet pShapeFlags, ModelBlockRenderer.AmbientOcclusionFace pAoFace, int pPackedOverlay) {
		float[] shape = new float[12];
		BitSet shapeFlags = new BitSet(3);

		for(BakedQuad bakedquad : pQuads) {
			var state = pState;
			var pos = pPos;
			if(bakedquad instanceof TintedBakedQuad q) {
				state = q.state;
				pos = q.pos;
			}

			this.calculateShape(pLevel, state, pos, bakedquad.getVertices(), bakedquad.getDirection(), pShape, pShapeFlags);
			pAoFace.calculate(pLevel, state, pos, bakedquad.getDirection(), shape, shapeFlags, bakedquad.isShade());

			var b1 = pAoFace.brightness[0] + 0.2f;
			var b2 = pAoFace.brightness[1] + 0.2f;
			var b3 = pAoFace.brightness[2] + 0.2f;
			var b4 = pAoFace.brightness[3] + 0.2f;
			this.putQuadData(pLevel, state, pos, pConsumer, pPoseStack.last(), bakedquad, b1, b2, b3, b4, pAoFace.lightmap[0], pAoFace.lightmap[1], pAoFace.lightmap[2], pAoFace.lightmap[3], pPackedOverlay);
		}
	}

	@Override
	public void renderModelFaceFlat(BlockAndTintGetter pLevel, BlockState pState, BlockPos pPos, int pPackedLight, int pPackedOverlay, boolean pRepackLight, PoseStack pPoseStack, VertexConsumer pConsumer, List<BakedQuad> pQuads, BitSet pShapeFlags) {
		float[] shape = new float[12];
		BitSet shapeFlags = new BitSet(3);

		for(BakedQuad bakedquad : pQuads) {
			var state = pState;
			var pos = pPos;
			if(bakedquad instanceof TintedBakedQuad q) {
				state = q.state;
				pos = q.pos;
			}

			if (pRepackLight) {
				this.calculateShape(pLevel, state, pos, bakedquad.getVertices(), bakedquad.getDirection(), shape, shapeFlags);
				BlockPos blockpos = shapeFlags.get(0) ? pos.relative(bakedquad.getDirection()) : pos;
				pPackedLight = LevelRenderer.getLightColor(pLevel, state, blockpos);
			}

			float f = pLevel.getShade(bakedquad.getDirection(), bakedquad.isShade());
			this.putQuadData(pLevel, state, pos, pConsumer, pPoseStack.last(), bakedquad, f, f, f, f, pPackedLight, pPackedLight, pPackedLight, pPackedLight, pPackedOverlay);
		}

	}
}