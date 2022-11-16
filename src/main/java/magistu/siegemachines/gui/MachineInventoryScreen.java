package magistu.siegemachines.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import magistu.siegemachines.SiegeMachines;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class MachineInventoryScreen extends ContainerScreen<MachineContainer>
{
	private final ResourceLocation displaceCaseGui;

	public MachineInventoryScreen(MachineContainer container, PlayerInventory inv, ITextComponent titleIn)
    {
		super(container, inv, titleIn);

		this.leftPos = 0;
		this.topPos = 0;
		this.imageWidth = 176;
		this.imageHeight = 166;
		switch (container.getContainerSize()) 
		{
			case 18:
				this.displaceCaseGui = new ResourceLocation(SiegeMachines.ID, "textures/gui/machine_inventory_2.png");
				break;
			case 27:
				this.displaceCaseGui = new ResourceLocation(SiegeMachines.ID, "textures/gui/machine_inventory_3.png");
				break; 
			default:
				this.displaceCaseGui = new ResourceLocation(SiegeMachines.ID, "textures/gui/machine_inventory_1.png");
				break;
		}
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

	@SuppressWarnings("deprecation")
	@Override
	protected void renderBg(@NotNull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		assert this.minecraft != null;
		this.minecraft.textureManager.bind(this.displaceCaseGui);
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
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}
}
