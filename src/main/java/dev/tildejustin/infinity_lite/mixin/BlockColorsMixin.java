package dev.tildejustin.infinity_lite.mixin;

import dev.tildejustin.infinity_lite.NetherPortalBlockEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.color.block.BlockColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockColors.class)
public abstract class BlockColorsMixin {
    @Inject(method = "create", at = @At(value = "TAIL"))
    private static void changePortalColorIfEnd(CallbackInfoReturnable<BlockColors> cir) {
        cir.getReturnValue().registerColorProvider((state, world, pos, tintIndex) -> {
            if (world != null && pos != null) {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof NetherPortalBlockEntity) {
                    int dim = ((NetherPortalBlockEntity) blockEntity).getDimension();
                    return (dim != 0 ? dim : 0xFFFFFF) & 0xFFFFFF;
                }
            }
            return 0xFFFFFF;
        }, Blocks.NETHER_PORTAL);
    }
}
