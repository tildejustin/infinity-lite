package dev.tildejustin.infinity_lite;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class InfinityLite implements ClientModInitializer {
    public static BlockEntityType<NetherPortalBlockEntity> type;

    @Override
    public void onInitializeClient() {
        type = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("nether"), BlockEntityType.Builder.create(NetherPortalBlockEntity::new, Blocks.NETHER_PORTAL).build(null));
    }
}
