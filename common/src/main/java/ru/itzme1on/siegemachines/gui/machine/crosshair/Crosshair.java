package ru.itzme1on.siegemachines.gui.machine.crosshair;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import ru.itzme1on.siegemachines.SiegeMachinesCore;

@Environment(EnvType.CLIENT)
public abstract class Crosshair extends HudElement {
    protected static final Identifier CROSSHAIR_TEXTURES = new Identifier(SiegeMachinesCore.MOD_ID, "textures/gui/crosshair.png");

    public Crosshair(int width, int height) {
        super(width, height);
    }

    public final void render(MatrixStack matrixStack, float partialTicks) {}

    public abstract void render(MatrixStack matrixStack, float ticks, MinecraftClient mc, PlayerEntity player);
}
