package magistu.siegemachines.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import magistu.siegemachines.SiegeMachines;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public abstract class Crosshair extends HudElement
{
    protected static final ResourceLocation CROSSHAIR_TEXTURES = new ResourceLocation(SiegeMachines.ID, "textures/gui/crosshairs.png");

    public Crosshair(int width, int height)
    {
        super(width, height);
    }

    public final void render(MatrixStack matrixstack, float partialticks) {}

    public abstract void render(MatrixStack matrixstack, float ticks, Minecraft mc, PlayerEntity player);
}