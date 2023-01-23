package ru.magistu.siegemachines.gui.siegeworkbench;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import ru.magistu.siegemachines.SiegeMachines;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SiegeWorkbenchScreen extends AbstractContainerScreen<SiegeWorkbenchMenu> {
	private static final ResourceLocation TEXTURE =
			new ResourceLocation(SiegeMachines.ID, "textures/gui/siege_workbench.png");

	public SiegeWorkbenchScreen(SiegeWorkbenchMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;

		this.blit(pPoseStack, x, y, 0, 0, this.imageWidth, this.imageHeight);
	}

	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float delta) {
		renderBackground(stack);
		super.render(stack, mouseX, mouseY, delta);
		renderTooltip(stack, mouseX, mouseY);
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		assert this.minecraft != null;

		if (key == 256) {
			assert this.minecraft.player != null;
			this.minecraft.player.closeContainer();
			return true;
		}

		return super.keyPressed(key, b, c);
	}
}
