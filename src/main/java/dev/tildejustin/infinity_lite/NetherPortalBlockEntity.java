package dev.tildejustin.infinity_lite;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import org.jetbrains.annotations.Nullable;

public class NetherPortalBlockEntity extends BlockEntity {
    private boolean end = false;

    public NetherPortalBlockEntity() {
        super(InfinityLite.type);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putBoolean("End", this.end);
        return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        this.end = tag.getBoolean("End");
    }


    @Nullable
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return new BlockEntityUpdateS2CPacket(this.pos, 15, this.toInitialChunkDataTag());
    }

    @Override
    public CompoundTag toInitialChunkDataTag() {
        return this.toTag(new CompoundTag());
    }

    public boolean isEnd() {
        return this.end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }
}
