package ru.magistu.siegemachines.gui.machine;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import ru.magistu.siegemachines.SiegeMachines;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class MachineInventoryScreen extends AbstractContainerScreen<MachineContainer>
{
	private static final ResourceLocation DISPLAY_CASE_GUI = new ResourceLocation(SiegeMachines.ID,
			"textures/gui/machine_inventory.png");

	public MachineInventoryScreen(MachineContainer screenContainer, Inventory inv, Component titleIn)
    {
		super(screenContainer, inv, titleIn);

		this.leftPos = 0;
		this.topPos = 0;
		this.imageWidth = 176;
		this.imageHeight = 166;
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
	protected void renderBg(@NotNull PoseStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      	RenderSystem.setShaderTexture(0, DISPLAY_CASE_GUI);
		int x = (this.width - this.imageWidth) / 2;
		int y = (this.height - this.imageHeight) / 2;
		this.blit(matrixStack, x, y, 0, 0, this.imageWidth, this.imageHeight);
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
