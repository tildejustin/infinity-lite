package dev.tildejustin.infinity_lite.mixin;

import com.google.common.collect.*;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.tildejustin.infinity_lite.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.Text;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.stream.*;

@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin extends Block {
    @Shadow
    @Final
    public static EnumProperty<Direction.Axis> AXIS;

    public NetherPortalBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setDefaultEndState(Settings settings, CallbackInfo ci) {
        this.setDefaultState(this.stateManager.getDefaultState().with(InfinityLite.END, false));
    }

    @Inject(method = "appendProperties", at = @At("TAIL"))
    private void appendEndState(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(InfinityLite.END);
    }

    @Dynamic // mcdev doesn't like @Coerce
    @ModifyExpressionValue(method = "randomDisplayTick", at = @At(value = "FIELD", target = "Lnet/minecraft/particle/ParticleTypes;PORTAL:Lnet/minecraft/particle/DefaultParticleType;"))
    private @Coerce ParticleEffect changeParticleIfEnd(DefaultParticleType original, BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(InfinityLite.END)) {
            int i = 2;
            Vec3d vec3d = Vec3d.unpackRgb(i);
            double d = 1.0 + (double) (i >> 16 & 0xFF) / 255.0;
            return new DustParticleEffect((float) vec3d.x, (float) vec3d.y, (float) vec3d.z, (float) d);
        }
        return original;
    }

    @Inject(method = "onEntityCollision", at = @At("HEAD"))
    public void changeDimensionOfPortal(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (entity instanceof ItemEntity) {
            ItemStack itemStack = ((ItemEntity) entity).getStack();
            if (itemStack.getItem() == Items.WRITTEN_BOOK || itemStack.getItem() == Items.WRITABLE_BOOK) {
                BookContent bookContent = BookContent.fromStack(itemStack);
                String string = IntStream.range(0, bookContent.getPageCount()).mapToObj(bookContent::getPage).map(Text::getString).collect(Collectors.joining("\n"));
                if (!string.isEmpty()) {
                    int dimension = DimensionHashHelper.getHash(string);
                    // only go to end dimension
                    if (dimension != 2) {
                        return;
                    }
                    this.teleportTo(world, pos, state);
                    entity.remove();
                }
            }
        }
    }

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void stopPigmanSpawningIfEnd(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (state.get(InfinityLite.END)) {
            ci.cancel();
        }
    }

    @Unique
    private void teleportTo(World world, BlockPos pos, BlockState state) {
        Set<BlockPos> set = Sets.newHashSet();
        Queue<BlockPos> queue = Queues.newArrayDeque();
        Direction.Axis axis = state.get(AXIS);
        Direction direction;
        Direction direction2;
        switch (axis) {
            case X:
                direction = Direction.UP;
                direction2 = Direction.EAST;
                break;
            case Y:
                direction = Direction.SOUTH;
                direction2 = Direction.EAST;
                break;
            case Z:
            default:
                direction = Direction.UP;
                direction2 = Direction.SOUTH;
                break;
        }

        Direction direction3 = direction.getOpposite();
        Direction direction4 = direction2.getOpposite();
        queue.add(pos);

        BlockPos blockPos;
        while ((blockPos = queue.poll()) != null) {
            set.add(blockPos);
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState == state) {
                world.setBlockState(blockPos, blockState.with(InfinityLite.END, true), 18);

                BlockPos blockPos2 = blockPos.offset(direction);
                if (!set.contains(blockPos2)) {
                    queue.add(blockPos2);
                }

                blockPos2 = blockPos.offset(direction3);
                if (!set.contains(blockPos2)) {
                    queue.add(blockPos2);
                }

                blockPos2 = blockPos.offset(direction2);
                if (!set.contains(blockPos2)) {
                    queue.add(blockPos2);
                }

                blockPos2 = blockPos.offset(direction4);
                if (!set.contains(blockPos2)) {
                    queue.add(blockPos2);
                }
            }
        }
    }
}
