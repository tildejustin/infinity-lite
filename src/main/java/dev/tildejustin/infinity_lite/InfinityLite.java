package dev.tildejustin.infinity_lite;

import dev.tildejustin.infinity_lite.mixin.accessor.BlockEntityTypeAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;

public class InfinityLite implements ClientModInitializer {
    public static BlockEntityType<NetherPortalBlockEntity> type;

    @Override
    public void onInitializeClient() {
        type = BlockEntityTypeAccessor.callCreate("nether", BlockEntityType.Builder.create(NetherPortalBlockEntity::new, Blocks.NETHER_PORTAL));
    }
}
