package dev.tildejustin.infinity_lite.mixin;

import com.google.common.collect.*;
import dev.tildejustin.infinity_lite.*;
import dev.tildejustin.infinity_lite.block.NeitherPortalBlock;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
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
public abstract class NetherPortalBlockMixin {
    @Shadow
    @Final
    public static EnumProperty<Direction.Axis> AXIS;

    @Unique
    private static final ParticleEffect NEITHER_PORTAL;

    static {
        int i = 2;
        Vec3d vec3d = Vec3d.unpackRgb(i);
        double d = 1.0 + (double) (i >> 16 & 0xFF) / 255.0;
        NEITHER_PORTAL = new DustParticleEffect((float) vec3d.x, (float) vec3d.y, (float) vec3d.z, (float) d);
    }

    @Environment(EnvType.CLIENT)
    @ModifyArg(method = "randomDisplayTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
    private ParticleEffect changeParticleIfEnd(ParticleEffect original) {
        if (!InfinityLite.config.enabled) return original;

        return (Object) this instanceof NeitherPortalBlock ? NEITHER_PORTAL : original;
    }

    @Inject(method = "onEntityCollision", at = @At("HEAD"))
    public void changeDimensionOfPortal(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (!InfinityLite.config.enabled) return;

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

    // bad mapping name, but better to keep it
    @Unique
    private void teleportTo(World world, BlockPos pos, BlockState state) {
        Set<BlockPos> set = Sets.newHashSet();
        Queue<BlockPos> queue = Queues.newArrayDeque();
        Direction.Axis axis = state.get(AXIS);
        BlockState blockState = InfinityLite.NEITHER_PORTAL.getDefaultState().with(AXIS, axis);
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
            BlockState blockState2 = world.getBlockState(blockPos);
            if (blockState2 == state) {
                world.setBlockState(blockPos, blockState, 0b10010);

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
