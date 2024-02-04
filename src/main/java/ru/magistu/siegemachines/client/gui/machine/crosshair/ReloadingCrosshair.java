package ru.magistu.siegemachines.client.gui.machine.crosshair;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import ru.magistu.siegemachines.entity.machine.Machine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ReloadingCrosshair extends Crosshair
{
    //public static ResourceLocation TYPE = new ResourceLocation(SiegeMachines.ID, "siege_machine");
    public int x;
    public int y;

    public ReloadingCrosshair()
    {
        super(9, 9);
        this.x = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2;
        this.y = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2;
    }

    public void render(PoseStack matrixstack, float ticks, Minecraft mc, Player player)
    {
        RenderSystem.assertOnRenderThread();

        if (!player.isPassenger())
        {
            return;
        }
        Entity entity = player.getVehicle();

        if (entity instanceof Machine)
        {
            Machine machine = (Machine) entity;

            int width = 11;
            int height = 11;
            int imagex = 0;
            int imagey = 0;
            int originx = (mc.getWindow().getGuiScaledWidth() - width) / 2;
            int originy = (mc.getWindow().getGuiScaledHeight() - height) / 2;
            int animationsize = 23;
            if (machine.useticks > 0)
            {
                imagey = height;
            }
            else if (machine.delayticks > 0)
            {
                int number = (int) (((double) animationsize) * ((double) (machine.type.specs.delaytime.get() - machine.delayticks) / (double) machine.type.specs.delaytime.get()));
                imagex = width;
                imagey = height * number;
            }

            RenderSystem.applyModelViewMatrix();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
      	    RenderSystem.setShaderTexture(0, CROSSHAIR_TEXTURES);

            mc.gui.blit(matrixstack, originx, originy, imagex, imagey, width, height);

            RenderSystem.backupProjectionMatrix();
        }
    }
}