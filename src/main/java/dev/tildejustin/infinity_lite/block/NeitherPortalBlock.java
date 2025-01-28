package dev.tildejustin.infinity_lite.block;

import dev.tildejustin.infinity_lite.InfinityLite;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NeitherPortalBlock extends NetherPortalBlock {
    public NeitherPortalBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!InfinityLite.enabled) return;

        super.onEntityCollision(state, world, pos, entity);
    }
}
