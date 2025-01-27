package dev.tildejustin.infinity_lite.mixin;

import dev.tildejustin.infinity_lite.InfinityLite;
import dev.tildejustin.infinity_lite.block.NeitherPortalBlock;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Blocks.class)
public abstract class BlocksMixin {
    @Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/util/registry/Registry;BLOCK:Lnet/minecraft/util/registry/DefaultedRegistry;"))
    private static void addNeitherPortal(CallbackInfo ci) {
        InfinityLite.NEITHER_PORTAL = Registry.register(
                Registry.BLOCK,
                "neither_portal",
                new NeitherPortalBlock(
                        AbstractBlock.Settings.of(Material.PORTAL)
                                .noCollision()
                                .strength(-1.0F)
                                .sounds(BlockSoundGroup.GLASS)
                                .lightLevel(state -> 11)
                                .dropsNothing()
                )
        );
    }
}
