package ru.magistu.siegemachines.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
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
import net.minecraftforge.fluids.IFluidBlock;
import org.jetbrains.annotations.NotNull;
import ru.magistu.siegemachines.SiegeMachines;
import ru.magistu.siegemachines.entity.EntityTypes;
import ru.magistu.siegemachines.entity.machine.Machine;
import ru.magistu.siegemachines.entity.machine.MachineType;
import ru.magistu.siegemachines.entity.projectile.ProjectileBuilder;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class MachineItem extends Item implements IAnimatable
{
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private final String entitykey;
    private final String typekey;

    public MachineItem(Properties prop, String entitykey, String typekey)
    {
        super(prop.stacksTo(1));
        this.entitykey = entitykey;
        this.typekey = typekey;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> tooltip, TooltipFlag p_41424_)
    {
        ProjectileBuilder[] ammo = MachineType.valueOf(this.typekey).ammo;
        if (ammo.length > 0)
        {
            tooltip.add(new TranslatableComponent(SiegeMachines.MOD_ID + ".ammo").withStyle(ChatFormatting.BLUE));
            for (ProjectileBuilder builder : ammo)
            {
                if (MachineType.valueOf(this.typekey).usesgunpowder)
                {
                    tooltip.add(new TranslatableComponent(SiegeMachines.MOD_ID + ".uses_gunpowder").withStyle(ChatFormatting.BLUE));
                }
                tooltip.add(new TextComponent("    ").append(new TranslatableComponent(builder.item.getDescriptionId())).withStyle(ChatFormatting.BLUE));
            }
        }
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext p_195939_1_)
    {
        Level world = p_195939_1_.getLevel();
        if (!(world instanceof ServerLevel))
        {
            return InteractionResult.SUCCESS;
        }
        else
        {
            ItemStack itemstack = p_195939_1_.getItemInHand();
            BlockPos blockpos = p_195939_1_.getClickedPos();
            Direction direction = p_195939_1_.getClickedFace();
            BlockState blockstate = world.getBlockState(blockpos);
            if (blockstate.is(Blocks.SPAWNER))
            {
                BlockEntity tileentity = world.getBlockEntity(blockpos);
                if (tileentity instanceof SpawnerBlockEntity)
                {
                    BaseSpawner abstractspawner = ((SpawnerBlockEntity)tileentity).getSpawner();
                    EntityType<?> entitytype1 = this.getType(itemstack.getTag());
                    abstractspawner.setEntityId(entitytype1);
                    tileentity.setChanged();
                    world.sendBlockUpdated(blockpos, blockstate, blockstate, 3);
                    itemstack.shrink(1);
                    return InteractionResult.CONSUME;
                }
            }

            BlockPos blockpos1;
            if (blockstate.getCollisionShape(world, blockpos).isEmpty())
            {
                blockpos1 = blockpos;
            }
            else
            {
                blockpos1 = blockpos.relative(direction);
            }

            EntityType<?> entitytype = this.getType(itemstack.getTag());
            Entity entity = this.spawn(entitytype, (ServerLevel)world, itemstack, p_195939_1_.getPlayer(), blockpos1, MobSpawnType.SPAWN_EGG, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP, p_195939_1_.getRotation());

            if (entity instanceof Machine)
            {
                ((Machine)entity).deploymentticks = 200;
            }
            if (entity != null)
            {
                itemstack.shrink(1);
            }

            return InteractionResult.CONSUME;
        }
    }

    protected static double getYOffset(LevelReader p_208051_0_, BlockPos p_208051_1_, boolean p_208051_2_, AABB p_208051_3_)
    {
        AABB axisalignedbb = new AABB(p_208051_1_);
        if (p_208051_2_)
        {
            axisalignedbb = axisalignedbb.expandTowards(0.0D, -1.0D, 0.0D);
        }

        Iterable<VoxelShape> iterable = p_208051_0_.getCollisions(null, axisalignedbb);
        return 1.0D + Shapes.collide(Direction.Axis.Y, p_208051_3_, iterable, p_208051_2_ ? -2.0D : -1.0D);
    }

    @Nullable
    public Entity spawn(EntityType<?> entitytype, ServerLevel p_220331_1_, @Nullable ItemStack p_220331_2_, @Nullable Player p_220331_3_, BlockPos p_220331_4_, MobSpawnType p_220331_5_, boolean p_220331_6_, boolean p_220331_7_, float yaw) {
        return this.spawn(entitytype, p_220331_1_, p_220331_2_ == null ? null : p_220331_2_.getTag(), p_220331_2_ != null && p_220331_2_.hasCustomHoverName() ? p_220331_2_.getHoverName() : null, p_220331_3_, p_220331_4_, p_220331_5_, p_220331_6_, p_220331_7_, yaw);
    }

    @Nullable
    public Entity spawn(EntityType<?> entitytype, ServerLevel p_20601_, @Nullable CompoundTag p_20602_, @Nullable Component p_20603_, @Nullable Player p_20604_, BlockPos p_20605_, MobSpawnType p_20606_, boolean p_20607_, boolean p_20608_, float yaw) {
        Entity t = this.create(entitytype, p_20601_, p_20602_, p_20603_, p_20604_, p_20605_, p_20606_, p_20607_, p_20608_, yaw);
        if (t != null)
        {
            if (t instanceof Mob && net.minecraftforge.event.ForgeEventFactory.doSpecialSpawn((Mob) t, (LevelAccessor)p_20601_, p_20605_.getX(), p_20605_.getY(), p_20605_.getZ(), null, p_20606_)) return null;
            p_20601_.addFreshEntityWithPassengers(t);
        }

        return t;
    }

    @Nullable
    public Entity create(EntityType<?> entitytype, ServerLevel p_20656_, @Nullable CompoundTag p_20657_, @Nullable Component p_20658_, @Nullable Player p_20659_, BlockPos p_20660_, MobSpawnType p_20661_, boolean p_20662_, boolean p_20663_, float yaw)
    {
        Entity t = entitytype.create(p_20656_);
        if (t == null) {
            return null;
        } else {
            double d0;
            if (p_20662_)
            {
                t.setPos((double)p_20660_.getX() + 0.5D, (double)(p_20660_.getY() + 1), (double)p_20660_.getZ() + 0.5D);
                d0 = getYOffset(p_20656_, p_20660_, p_20663_, t.getBoundingBox());
            }
            else
            {
                d0 = 0.0D;
            }

            t.moveTo((double)p_20660_.getX() + 0.5D, (double)p_20660_.getY() + d0, (double)p_20660_.getZ() + 0.5D, Mth.wrapDegrees(yaw), 0.0F);
            if (t instanceof Mob) {
                Mob mobentity = (Mob)t;
                mobentity.yHeadRot = mobentity.getYRot();
                mobentity.yBodyRot = mobentity.getYRot();
                mobentity.finalizeSpawn(p_20656_, p_20656_.getCurrentDifficultyAt(mobentity.blockPosition()), p_20661_, (SpawnGroupData)null, p_20657_);
                mobentity.playAmbientSound();
            }

            return t;
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand)
    {
        ItemStack itemstack = player.getItemInHand(hand);
        BlockHitResult raytraceresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (raytraceresult.getType() != HitResult.Type.BLOCK)
        {
            return InteractionResultHolder.pass(itemstack);
        }
        else if (!(level instanceof ServerLevel))
        {
            return InteractionResultHolder.success(itemstack);
        }
        else
        {
            BlockPos blockpos = raytraceresult.getBlockPos();
            if (!(level.getBlockState(blockpos).getBlock() instanceof IFluidBlock))
            {
                return InteractionResultHolder.pass(itemstack);
            }
            else if (level.mayInteract(player, blockpos) && player.mayUseItemAt(blockpos, raytraceresult.getDirection(), itemstack))
            {
                EntityType<?> entitytype = this.getType(itemstack.getTag());
                Entity entity = this.spawn(entitytype, (ServerLevel) level, itemstack, player, blockpos, MobSpawnType.SPAWN_EGG, false, false, player.getYRot());
                if (entity instanceof Machine)
                {
                    ((Machine)entity).deploymentticks = 200;
                }
                if (entity != null)
                {
                    if (!player.isCreative())
                    {
                        itemstack.shrink(1);
                    }
                    player.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.consume(itemstack);
                }
                else
                {
                    return InteractionResultHolder.pass(itemstack);
                }

            }
            else
            {
                return InteractionResultHolder.fail(itemstack);
            }
        }
    }

    public EntityType<?> getType(@Nullable CompoundTag nbt)
    {
        EntityType<?> defaulttype = EntityTypes.DEFERRED_REGISTER.getEntries().stream().filter(r -> r.get().getRegistryName().getPath().equals(this.entitykey)).findFirst().get().get();

        if (nbt != null && nbt.contains("EntityTag", 10))
        {
            CompoundTag compoundnbt = nbt.getCompound("EntityTag");
            if (compoundnbt.contains("id", 8))
            {
                return EntityType.byString(compoundnbt.getString("id")).orElse(defaulttype);
            }
        }

        return defaulttype;
    }

    @Override
    public void registerControllers(AnimationData data)
    {

    }

    @Override
    public AnimationFactory getFactory()
    {
        return this.factory;
    }
}
