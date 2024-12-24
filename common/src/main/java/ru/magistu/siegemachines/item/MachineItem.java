package ru.magistu.siegemachines.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import ru.magistu.siegemachines.SiegeMachines;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import ru.magistu.siegemachines.client.KeyBindings;
import ru.magistu.siegemachines.client.renderer.MachineItemGeoRenderer;
import ru.magistu.siegemachines.entity.machine.Machine;
import ru.magistu.siegemachines.entity.machine.MachineType;
import ru.magistu.siegemachines.entity.projectile.ProjectileBuilder;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class MachineItem<T extends Machine> extends Item implements GeoItem {
    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    private final Supplier<EntityType<T>> entitytype;
    private final Supplier<MachineType> machinetype;

    public MachineItem(Properties prop, Supplier<EntityType<T>> entitytype, Supplier<MachineType> machinetype)
    {
        super(prop.stacksTo(1));
        this.entitytype = entitytype;
        this.machinetype = machinetype;
    }

    public MachineItemGeoRenderer<T> getRenderer()
    {
        return null;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable(SiegeMachines.ID + ".usage", KeyBindings.getUseKey(this.machinetype.get()).getTranslatedKeyMessage()).withStyle(ChatFormatting.BLUE));

        ProjectileBuilder<?>[] ammo = this.machinetype.get().ammo;
        if (ammo.length > 0)
        {
            tooltip.add(Component.translatable(SiegeMachines.ID + ".ammo").withStyle(ChatFormatting.BLUE));
            for (ProjectileBuilder<?> builder : ammo)
            {
                if (this.machinetype.get().usesgunpowder)
                {
                    tooltip.add(Component.translatable(SiegeMachines.ID + ".uses_gunpowder").withStyle(ChatFormatting.BLUE));
                }
                tooltip.add(Component.literal("    ").append(Component.translatable(builder.item.getDescriptionId())).withStyle(ChatFormatting.BLUE));
            }
        }
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context)
    {
        Level world = context.getLevel();
        if (!(world instanceof ServerLevel))
            return InteractionResult.SUCCESS;
        
        ItemStack itemstack = context.getItemInHand();
        BlockPos blockpos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        BlockState blockstate = world.getBlockState(blockpos);
        if (blockstate.is(Blocks.SPAWNER))
        {
            BlockEntity tileentity = world.getBlockEntity(blockpos);
            if (tileentity instanceof SpawnerBlockEntity)
            {
                BaseSpawner abstractspawner = ((SpawnerBlockEntity)tileentity).getSpawner();
                EntityType<T> entitytype1 = this.getType(itemstack.get(DataComponents.ENTITY_DATA));
                abstractspawner.setEntityId(entitytype1,world,world.random,blockpos);
                tileentity.setChanged();
                world.sendBlockUpdated(blockpos, blockstate, blockstate, 3);
                itemstack.shrink(1);
                return InteractionResult.CONSUME;
            }
        }

        BlockPos blockpos2;
        if (blockstate.getCollisionShape(world, blockpos).isEmpty())
            blockpos2 = blockpos;
        else
            blockpos2 = blockpos.relative(direction);

        EntityType<T> entitytype = this.getType(itemstack.get(DataComponents.ENTITY_DATA));
        Machine entity = this.spawn(entitytype, (ServerLevel) world, itemstack, context.getPlayer(), blockpos2, MobSpawnType.SPAWN_EGG, true, !Objects.equals(blockpos, blockpos2) && direction == Direction.UP, context.getRotation());
        
        if (entity != null)
        {
            entity.deploymentticks = 200;
            itemstack.shrink(1);
        }

        return InteractionResult.CONSUME;
    }

    protected static double getYOffset(LevelReader reader, BlockPos pos, boolean bl, AABB aabb)
    {
        AABB axisalignedbb = new AABB(pos);
        if (bl)
            axisalignedbb = axisalignedbb.expandTowards(0.0D, -1.0D, 0.0D);

        Iterable<VoxelShape> iterable = reader.getCollisions(null, axisalignedbb);
        return 1.0D + Shapes.collide(Direction.Axis.Y, aabb, iterable, bl ? -2.0D : -1.0D);
    }

    @Nullable
    public Machine spawn(EntityType<T> entitytype, ServerLevel level, ItemStack stack, @Nullable Player player, BlockPos pos, MobSpawnType type, boolean bl, boolean bl2, float yaw)
    {
        return this.spawn(entitytype, level, stack.get(DataComponents.CUSTOM_DATA), stack.getHoverName(), player, pos, type, bl, bl2, yaw);
    }

    @Nullable
    public Machine spawn(EntityType<T> entitytype, ServerLevel level, @Nullable CustomData nbt, @Nullable Component component, @Nullable Player player, BlockPos pos, MobSpawnType type, boolean bl, boolean bl2, float yaw)
    {
        Machine machine = this.create(entitytype, level, nbt, component, player, pos, type, bl, bl2, yaw);
        if (machine != null)
        {
       //     if (net.minecraftforge.event.ForgeEventFactory.doSpecialSpawn(machine, (LevelAccessor)level, pos.getX(), pos.getY(), pos.getZ(), null, type)) return null;
            level.addFreshEntityWithPassengers(machine);
        }

        return machine;
    }

    @Nullable
    public Machine create(EntityType<T> entitytype, ServerLevel level, @Nullable CustomData nbt, @Nullable Component component, @Nullable Player player, BlockPos pos, MobSpawnType type, boolean bl, boolean bl2, float yaw)
    {
        Machine machine = entitytype.create(level);
        if (machine == null)
            return null;

        double d0;
        if (bl)
        {
            machine.setPos((double)pos.getX() + 0.5D, pos.getY() + 1, (double)pos.getZ() + 0.5D);
            d0 = getYOffset(level, pos, bl2, machine.getBoundingBox());
        }
        else
            d0 = 0.0D;

        if (nbt!=null)
        EntityType.updateCustomEntityTag(level, player, machine, nbt);

        if (component != null)
            machine.setCustomName(component);

        machine.moveTo((double)pos.getX() + 0.5D, (double)pos.getY() + d0, (double)pos.getZ() + 0.5D, Mth.wrapDegrees(yaw), 0.0F);
        machine.yHeadRot = machine.getYRot();
        machine.yBodyRot = machine.getYRot();
        machine.finalizeSpawn(level, level.getCurrentDifficultyAt(machine.blockPosition()), type, null);
        machine.playAmbientSound();

        return machine;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand)
    {
        ItemStack itemstack = player.getItemInHand(hand);
        BlockHitResult raytraceresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        
        if (raytraceresult.getType() != HitResult.Type.BLOCK)
            return InteractionResultHolder.pass(itemstack);
        
        if (!(level instanceof ServerLevel))
            return InteractionResultHolder.success(itemstack);
        
        BlockPos blockpos = raytraceresult.getBlockPos();
      //  if (!(level.getBlockState(blockpos).getBlock() instanceof IFluidBlock))
      //      return InteractionResultHolder.pass(itemstack);
        
        if (level.mayInteract(player, blockpos) && player.mayUseItemAt(blockpos, raytraceresult.getDirection(), itemstack))
        {
            EntityType<T> entitytype = this.getType(itemstack.get(DataComponents.ENTITY_DATA));
            Machine machine = this.spawn(entitytype, (ServerLevel) level, itemstack, player, blockpos, MobSpawnType.SPAWN_EGG, false, false, player.getYRot());
            if (machine != null)
            {
                machine.deploymentticks = 200;
                if (!player.isCreative())
                    itemstack.shrink(1);
                player.awardStat(Stats.ITEM_USED.get(this));
                return InteractionResultHolder.consume(itemstack);
            }
            else
                return InteractionResultHolder.pass(itemstack);

        }
        else
            return InteractionResultHolder.fail(itemstack);
    }

    @SuppressWarnings("unchecked")
    public EntityType<T> getType(@Nullable CustomData nbt)
    {
        EntityType<T> defaulttype = this.entitytype.get();

        if (nbt != null) {
            CompoundTag compoundnbt = nbt.getUnsafe();
            if (compoundnbt.contains("id", 8))
                return (EntityType<T>) EntityType.byString(compoundnbt.getString("id")).orElse(defaulttype);
        }

        return defaulttype;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }
}
