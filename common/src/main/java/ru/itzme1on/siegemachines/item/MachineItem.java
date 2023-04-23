package ru.itzme1on.siegemachines.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.MobSpawnerLogic;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import ru.itzme1on.siegemachines.SiegeMachinesCore;
import ru.itzme1on.siegemachines.entity.machine.Machine;
import ru.itzme1on.siegemachines.entity.machine.MachineType;
import ru.itzme1on.siegemachines.entity.projectile.ProjectileBuilder;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;
import java.util.Objects;

public class MachineItem extends Item implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private final String entityKey;
    private final String typeKey;

    public MachineItem(Settings settings, String entityKey, String typeKey) {
        super(settings.maxCount(1));
        this.entityKey = entityKey;
        this.typeKey = typeKey;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        ProjectileBuilder<?>[] ammo = MachineType.valueOf(this.typeKey).ammo;
        if (ammo.length > 0) {
            tooltip.add(new TranslatableText(SiegeMachinesCore.MOD_ID + ".ammo").formatted(Formatting.BLUE));
            for (ProjectileBuilder<?> builder : ammo) {
                if (MachineType.valueOf(this.typeKey).usesGunpowder)
                    tooltip.add(new TranslatableText(SiegeMachinesCore.MOD_ID + ".uses_gunpowder").formatted(Formatting.BLUE));
                tooltip.add(new LiteralText("    ").append(new TranslatableText(builder.item.getTranslationKey())).formatted(Formatting.BLUE));
            }
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (!(world instanceof ServerWorld))
            return ActionResult.SUCCESS;

        else {
            ItemStack itemstack = context.getStack();
            BlockPos blockpos = context.getBlockPos();
            Direction direction = context.getSide();
            BlockState blockstate = world.getBlockState(blockpos);
            if (blockstate.isOf(Blocks.SPAWNER)) {
                BlockEntity tileEntity = world.getBlockEntity(blockpos);
                if (tileEntity instanceof MobSpawnerBlockEntity) {
                    MobSpawnerLogic abstractSpawner = ((MobSpawnerBlockEntity) tileEntity).getLogic();
                    EntityType<?> entityType = this.getType(itemstack.getNbt());
                    abstractSpawner.setEntityId(entityType);
                    tileEntity.markDirty();
                    world.updateListeners(blockpos, blockstate, blockstate, 3);
                    itemstack.decrement(1);
                    return ActionResult.CONSUME;
                }
            }

            BlockPos blockPos;
            if (blockstate.getCollisionShape(world, blockpos).isEmpty())
                blockPos = blockpos;

            else blockPos = blockpos.offset(direction);

            EntityType<?> entityType = this.getType(itemstack.getNbt());
            Entity entity = this.spawn(
                    entityType,
                    (ServerWorld) world,
                    itemstack,
                    context.getPlayer(),
                    blockPos,
                    SpawnReason.SPAWN_EGG,
                    true,
                    !Objects.equals(blockpos, blockPos) && direction == Direction.UP,
                    context.getPlayerYaw());

            if (entity instanceof Machine)
                ((Machine)entity).deploymentTicks = 200;

            if (entity != null)
                itemstack.decrement(1);

            return ActionResult.CONSUME;
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        BlockHitResult raytraceResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
        if (raytraceResult.getType() != HitResult.Type.BLOCK)
            return TypedActionResult.pass(itemStack);

        else if (!(world instanceof ServerWorld))
            return TypedActionResult.success(itemStack);

        else {
            BlockPos blockpos = raytraceResult.getBlockPos();
            if (!(world.getBlockState(blockpos).getBlock() instanceof FluidBlock))
                return TypedActionResult.pass(itemStack);

            else if (world.canPlayerModifyAt(user, blockpos) && user.canPlaceOn(blockpos, raytraceResult.getSide(), itemStack)) {
                EntityType<?> entityType = this.getType(itemStack.getNbt());
                Entity entity = this.spawn(entityType, (ServerWorld) world, itemStack, user, blockpos, SpawnReason.SPAWN_EGG, false, false, user.getYaw());
                if (entity instanceof Machine)
                    ((Machine) entity).deploymentTicks = 200;

                if (entity != null) {
                    if (!user.isCreative())
                        itemStack.decrement(1);

                    user.incrementStat(Stats.USED.getOrCreateStat(this));
                    return TypedActionResult.consume(itemStack);
                }

                else return TypedActionResult.pass(itemStack);
            }

            else return TypedActionResult.fail(itemStack);
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {}

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Nullable
    public Entity spawn(EntityType<?> entityType, ServerWorld world, @Nullable ItemStack itemStack, @Nullable PlayerEntity player, BlockPos pos, SpawnReason reason, boolean a, boolean b, float yaw) {
        return this.spawn(entityType, world, itemStack == null ? null : itemStack.getNbt(), itemStack != null && itemStack.hasCustomName() ? itemStack.getName() : null, player, pos, reason, a, b, yaw);
    }

    @Nullable
    public Entity spawn(EntityType<?> entityType, ServerWorld world, @Nullable NbtCompound nbt, @Nullable Text text, @Nullable PlayerEntity player, BlockPos pos, SpawnReason reason, boolean a, boolean b, float yaw) {
        Entity t = this.create(entityType, world, nbt, text, player, pos, reason, a, b, yaw);
        if (t != null) {
            if (t instanceof MobEntity) return null;
            world.spawnEntityAndPassengers(t);
        }

        return t;
    }

    protected static double getYOffset(WorldView view, BlockPos pos, boolean bool, Box box) {
        Box axisAlignedBox = new Box(pos);
        if (bool) axisAlignedBox = axisAlignedBox.stretch(0.0D, -1.0D, 0.0D);

        Iterable<VoxelShape> iterable = view.getCollisions(null, axisAlignedBox);
        return 1.0D + VoxelShapes.calculateMaxOffset(Direction.Axis.Y, box, iterable, bool ? -2.0D : -1.0D);
    }

    @Nullable
    public Entity create(EntityType<?> entityType, ServerWorld world, @Nullable NbtCompound nbt, @Nullable Text text, @Nullable PlayerEntity player, BlockPos pos, SpawnReason reason, boolean a, boolean b, float yaw) {
        Entity t = entityType.create(world);
        if (t == null)
            return null;

        else {
            double d0;
            if (a) {
                t.setPos((double) pos.getX() + 0.5D, pos.getY() + 1, (double) pos.getZ() + 0.5D);
                d0 = getYOffset(world, pos, b, t.getBoundingBox());
            }

            else d0 = 0.0D;

            t.refreshPositionAndAngles((double)pos.getX() + 0.5D, (double)pos.getY() + d0, (double)pos.getZ() + 0.5D, MathHelper.wrapDegrees(yaw), 0.0F);
            if (t instanceof MobEntity mobEntity) {
                mobEntity.headYaw = mobEntity.getYaw();
                mobEntity.bodyYaw = mobEntity.getYaw();
                mobEntity.initialize(world, world.getLocalDifficulty(mobEntity.getBlockPos()), reason, null, nbt);
                mobEntity.playAmbientSound();
            }

            return t;
        }
    }

    public EntityType<?> getType(@javax.annotation.Nullable NbtCompound nbt) {
        if (nbt != null && nbt.contains("EntityTag", 10)) {
            NbtCompound nbtCompound = nbt.getCompound("EntityTag");
            if (nbtCompound.contains("id", 8))
                return EntityType.get(nbtCompound.getString("id")).orElse(null);
        }
        return null;
    }
}
