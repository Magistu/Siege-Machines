package magistu.siegemachines.item;

import magistu.siegemachines.SiegeMachines;
import magistu.siegemachines.entity.EntityTypes;
import magistu.siegemachines.entity.machine.Machine;
import magistu.siegemachines.entity.machine.MachineType;
import magistu.siegemachines.entity.projectile.ProjectileBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.AbstractSpawner;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

public class MachineItem extends Item implements IAnimatable
{
    private final AnimationFactory factory = new AnimationFactory(this);

    private final String entitykey;
    private final String typekey;

    public MachineItem(Item.Properties prop, String entitykey, String typekey)
    {
        super(prop.stacksTo(1));
        this.entitykey = entitykey;
        this.typekey = typekey;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        ProjectileBuilder[] ammo = MachineType.valueOf(this.typekey).ammo;
        if (ammo.length > 0)
        {
            tooltip.add(new TranslationTextComponent(SiegeMachines.ID + ".ammo").withStyle(TextFormatting.BLUE));
            for (ProjectileBuilder builder : ammo)
            {
                if (MachineType.valueOf(this.typekey).usesgunpowder)
                {
                    tooltip.add(new StringTextComponent("Uses gunpowder").withStyle(TextFormatting.BLUE));
                }
                tooltip.add(new StringTextComponent("    ").append(new TranslationTextComponent(builder.item.getDescriptionId())).withStyle(TextFormatting.BLUE));
            }
        }
    }

    @Override
    public @NotNull ActionResultType useOn(ItemUseContext p_195939_1_)
    {
        World world = p_195939_1_.getLevel();
        if (!(world instanceof ServerWorld))
        {
            return ActionResultType.SUCCESS;
        }
        else
        {
            ItemStack itemstack = p_195939_1_.getItemInHand();
            BlockPos blockpos = p_195939_1_.getClickedPos();
            Direction direction = p_195939_1_.getClickedFace();
            BlockState blockstate = world.getBlockState(blockpos);
            if (blockstate.is(Blocks.SPAWNER))
            {
                TileEntity tileentity = world.getBlockEntity(blockpos);
                if (tileentity instanceof MobSpawnerTileEntity)
                {
                    AbstractSpawner abstractspawner = ((MobSpawnerTileEntity)tileentity).getSpawner();
                    EntityType<?> entitytype1 = this.getType(itemstack.getTag());
                    abstractspawner.setEntityId(entitytype1);
                    tileentity.setChanged();
                    world.sendBlockUpdated(blockpos, blockstate, blockstate, 3);
                    itemstack.shrink(1);
                    return ActionResultType.CONSUME;
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
            Entity entity = this.spawn(entitytype, (ServerWorld)world, itemstack, p_195939_1_.getPlayer(), blockpos1, SpawnReason.SPAWN_EGG, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP, p_195939_1_.getRotation());

            if (entity instanceof Machine)
            {
                ((Machine)entity).deploymentticks = 200;
            }
            if (entity != null)
            {
                itemstack.shrink(1);
            }

            return ActionResultType.CONSUME;
        }
    }

    protected static double getYOffset(IWorldReader p_208051_0_, BlockPos p_208051_1_, boolean p_208051_2_, AxisAlignedBB p_208051_3_) {
        AxisAlignedBB axisalignedbb = new AxisAlignedBB(p_208051_1_);
        if (p_208051_2_) {
            axisalignedbb = axisalignedbb.expandTowards(0.0D, -1.0D, 0.0D);
        }

        Stream<VoxelShape> stream = p_208051_0_.getCollisions((Entity)null, axisalignedbb, (p_233596_0_) -> {
            return true;
        });
        return 1.0D + VoxelShapes.collide(Direction.Axis.Y, p_208051_3_, stream, p_208051_2_ ? -2.0D : -1.0D);
    }

    @Nullable
    public Entity spawn(EntityType<?> entitytype, ServerWorld p_220331_1_, @Nullable ItemStack p_220331_2_, @Nullable PlayerEntity p_220331_3_, BlockPos p_220331_4_, SpawnReason p_220331_5_, boolean p_220331_6_, boolean p_220331_7_, float yaw) {
        return this.spawn(entitytype, p_220331_1_, p_220331_2_ == null ? null : p_220331_2_.getTag(), p_220331_2_ != null && p_220331_2_.hasCustomHoverName() ? p_220331_2_.getHoverName() : null, p_220331_3_, p_220331_4_, p_220331_5_, p_220331_6_, p_220331_7_, yaw);
    }

    @Nullable
    public Entity spawn(EntityType<?> entitytype, ServerWorld p_220342_1_, @Nullable CompoundNBT p_220342_2_, @Nullable ITextComponent p_220342_3_, @Nullable PlayerEntity p_220342_4_, BlockPos p_220342_5_, SpawnReason p_220342_6_, boolean p_220342_7_, boolean p_220342_8_, float yaw) {
        Entity t = this.create(entitytype, p_220342_1_, p_220342_2_, p_220342_3_, p_220342_4_, p_220342_5_, p_220342_6_, p_220342_7_, p_220342_8_, yaw);
        if (t != null) {
            if (t instanceof net.minecraft.entity.MobEntity && net.minecraftforge.event.ForgeEventFactory.doSpecialSpawn((net.minecraft.entity.MobEntity) t, p_220342_1_, p_220342_5_.getX(), p_220342_5_.getY(), p_220342_5_.getZ(), null, p_220342_6_)) return null;
            p_220342_1_.addFreshEntityWithPassengers(t);
        }

        return t;
    }

    @Nullable
    public Entity create(EntityType<?> entitytype, ServerWorld p_220349_1_, @Nullable CompoundNBT p_220349_2_, @Nullable ITextComponent p_220349_3_, @Nullable PlayerEntity p_220349_4_, BlockPos p_220349_5_, SpawnReason p_220349_6_, boolean p_220349_7_, boolean p_220349_8_, float yaw) {
        Entity t = entitytype.create(p_220349_1_);
        if (t == null) {
            return null;
        } else {
            double d0;
            if (p_220349_7_) {
                t.setPos((double)p_220349_5_.getX() + 0.5D, (double)(p_220349_5_.getY() + 1), (double)p_220349_5_.getZ() + 0.5D);
                d0 = getYOffset(p_220349_1_, p_220349_5_, p_220349_8_, t.getBoundingBox());
            } else {
                d0 = 0.0D;
            }

            EntityType.updateCustomEntityTag(p_220349_1_, p_220349_4_, t, p_220349_2_);

            if (p_220349_3_ != null && t instanceof LivingEntity) {
                t.setCustomName(p_220349_3_);
            }

            t.moveTo((double)p_220349_5_.getX() + 0.5D, (double)p_220349_5_.getY() + d0, (double)p_220349_5_.getZ() + 0.5D, MathHelper.wrapDegrees(yaw), 0.0F);
            if (t instanceof MobEntity) {
                MobEntity mobentity = (MobEntity)t;
                mobentity.yHeadRot = mobentity.yRot;
                mobentity.yBodyRot = mobentity.yRot;
                mobentity.finalizeSpawn(p_220349_1_, p_220349_1_.getCurrentDifficultyAt(mobentity.blockPosition()), p_220349_6_, (ILivingEntityData)null, p_220349_2_);
                mobentity.playAmbientSound();
            }

            return t;
        }
    }

    @Override
    public @NotNull ActionResult<ItemStack> use(@NotNull World level, PlayerEntity player, @NotNull Hand hand)
    {
        ItemStack itemstack = player.getItemInHand(hand);
        BlockRayTraceResult raytraceresult = getPlayerPOVHitResult(level, player, RayTraceContext.FluidMode.SOURCE_ONLY);
        if (raytraceresult.getType() != RayTraceResult.Type.BLOCK)
        {
            return ActionResult.pass(itemstack);
        }
        else if (!(level instanceof ServerWorld))
        {
            return ActionResult.success(itemstack);
        }
        else
        {
            BlockPos blockpos = raytraceresult.getBlockPos();
            if (!(level.getBlockState(blockpos).getBlock() instanceof FlowingFluidBlock))
            {
                return ActionResult.pass(itemstack);
            }
            else if (level.mayInteract(player, blockpos) && player.mayUseItemAt(blockpos, raytraceresult.getDirection(), itemstack))
            {
                EntityType<?> entitytype = this.getType(itemstack.getTag());
                Entity entity = this.spawn(entitytype, (ServerWorld) level, itemstack, player, blockpos, SpawnReason.SPAWN_EGG, false, false, player.yRot);
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
                    return ActionResult.consume(itemstack);
                }
                else
                {
                    return ActionResult.pass(itemstack);
                }

            }
            else
            {
                return ActionResult.fail(itemstack);
            }
        }
    }

    public EntityType<?> getType(@Nullable CompoundNBT nbt)
    {
        EntityType<?> defaulttype = EntityTypes.DEFERRED_REGISTER.getEntries().stream().filter(r -> r.get().getRegistryName().getPath().equals(this.entitykey)).findFirst().get().get();

        if (nbt != null && nbt.contains("EntityTag", 10))
        {
            CompoundNBT compoundnbt = nbt.getCompound("EntityTag");
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
