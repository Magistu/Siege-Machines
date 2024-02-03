package ru.magistu.siegemachines.client.gui.workbench;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import ru.magistu.siegemachines.SiegeMachines;

@OnlyIn(Dist.CLIENT)
public class SiegeWorkbenchScreen extends AbstractContainerScreen<SiegeWorkbenchContainer>
{
	private static final ResourceLocation DISPLAY_CASE_GUI = new ResourceLocation(SiegeMachines.ID, "textures/gui/siege_workbench.png");

	public SiegeWorkbenchScreen(SiegeWorkbenchContainer screenContainer, Inventory inv, Component titleIn)
    {
		super(screenContainer, inv, titleIn);
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}

    @Override
	protected void renderTooltip(@NotNull PoseStack matrixStack, int x, int y)
    {
		if (this.minecraft.player.getInventory().getSelected().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem())
		{
    		this.renderTooltip(matrixStack, this.hoveredSlot.getItem(), x, y);
		}
	}

	@Override
	protected void init()
	{
		super.init();
		this.titleLabelX = 29;
	}

	@Override
	protected void renderBg(@NotNull PoseStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, DISPLAY_CASE_GUI);
    	int i = this.leftPos;
    	int j = (this.height - this.imageHeight) / 2;
    	this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
	}

	@Override
	public boolean keyPressed(int key, int b, int c)
	{
		assert this.minecraft != null;
		if (key == 256)
		{
			assert this.minecraft.player != null;
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}
}
