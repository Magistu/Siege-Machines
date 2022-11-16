package magistu.siegemachines.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import magistu.siegemachines.SiegeMachines;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SiegeWorkbenchScreen extends ContainerScreen<SiegeWorkbenchContainer>
{
	private static final ResourceLocation DISPLAY_CASE_GUI = new ResourceLocation(SiegeMachines.ID,
			"textures/gui/siege_workbench.png");

	public SiegeWorkbenchScreen(SiegeWorkbenchContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
		super(screenContainer, inv, titleIn);
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}

    @Override
	protected void renderTooltip(@NotNull MatrixStack matrixStack, int x, int y)
    {
		if (this.minecraft.player.inventory.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem())
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

	@SuppressWarnings("deprecation")
	@Override
	protected void renderBg(@NotNull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		assert this.minecraft != null;
		this.minecraft.getTextureManager().bind(DISPLAY_CASE_GUI);
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
