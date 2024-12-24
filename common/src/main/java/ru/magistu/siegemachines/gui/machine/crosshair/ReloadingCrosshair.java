package ru.magistu.siegemachines.gui.machine.crosshair;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import ru.magistu.siegemachines.entity.machine.Machine;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

public class ReloadingCrosshair extends Crosshair
{
    //public static ResourceLocation TYPE = new ResourceLocation(SiegeMachinesForge.ID, "siege_machine");
    public int x;
    public int y;

    public ReloadingCrosshair()
    {
        super(9, 9);
        this.x = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2;
        this.y = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2;
    }

    public void render(GuiGraphics matrixstack, DeltaTracker ticks)
    {
        LocalPlayer player = Minecraft.getInstance().player;
        if (!player.isPassenger())
        {
            return;
        }
        Entity entity = player.getVehicle();

        if (entity instanceof Machine machine)
        {

            int width = 11;
            int height = 11;
            int imagex = 0;
            int imagey = 0;
            int originx = (matrixstack.guiWidth() - width) / 2;
            int originy = (matrixstack.guiHeight() - height) / 2;
            int animationsize = 23;
            if (machine.getUseTicks() > 0)
            {
                imagey = height;
            }
            else if (machine.getDelayTicks() > 0)
            {
                int number = (int) (((double) animationsize) * ((double) (machine.type.specs.delaytime.get() - machine.getDelayTicks()) / (double) machine.type.specs.delaytime.get()));
                imagex = width;
                imagey = height * number;
            }

            RenderSystem.applyModelViewMatrix();
      	    RenderSystem.setShaderTexture(0, CROSSHAIR_TEXTURES);

            matrixstack.blit(CROSSHAIR_TEXTURES, originx, originy, imagex, imagey, width, height);

            RenderSystem.backupProjectionMatrix();
        }
    }
}