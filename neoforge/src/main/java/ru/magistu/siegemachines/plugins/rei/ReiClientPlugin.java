package ru.magistu.siegemachines.plugins.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginCommon;
import ru.magistu.siegemachines.block.ModBlocks;

import static me.shedaniel.rei.plugin.common.BuiltinPlugin.CRAFTING;

@REIPluginCommon
public class ReiClientPlugin implements REIClientPlugin {

  @Override
  public void registerCategories(CategoryRegistry registry) {
    registry.addWorkstations(CRAFTING, EntryStacks.of(ModBlocks.SIEGE_WORKBENCH.get()));
  }

  @Override
  public void registerTransferHandlers(TransferHandlerRegistry registry) {
  //  registry.register(new CraftingStationTransferHandler(CraftingStationMenu.class,CRAFTING));
  }

  @Override
  public void registerScreens(ScreenRegistry registry) {
  //  registry.registerContainerClickArea(new Rectangle(88, 32, 28, 23), CraftingStationScreen.class, CRAFTING);
  }

}
