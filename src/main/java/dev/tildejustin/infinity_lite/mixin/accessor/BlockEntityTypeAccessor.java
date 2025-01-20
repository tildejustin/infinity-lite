package dev.tildejustin.infinity_lite.mixin.accessor;

import net.minecraft.block.entity.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockEntityType.class)
public interface BlockEntityTypeAccessor {
    @Invoker("create")
    static <T extends BlockEntity> BlockEntityType<T> callCreate(String string, BlockEntityType.Builder<T> builder) {
        return null;
    }
}
