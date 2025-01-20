package dev.tildejustin.infinity_lite.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    public ServerPlayerEntityMixin(World world, BlockPos blockPos, GameProfile gameProfile) {
        super(world, blockPos, gameProfile);
    }

    @Shadow
    public abstract ServerWorld getServerWorld();

    @ModifyExpressionValue(method = "changeDimension", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getRegistryKey()Lnet/minecraft/util/registry/RegistryKey;", ordinal = 0), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V")))
    private RegistryKey<World> doNotHardcodeEndSpawnIfFromNether(RegistryKey<World> dest) {
        // anything other than end if not dest == end & curr == overworld
        return dest == World.END && this.getServerWorld().getRegistryKey() == World.OVERWORLD ? World.END : World.OVERWORLD;
    }

    @ModifyExpressionValue(method = "changeDimension", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/dimension/DimensionType;isShrunk()Z", ordinal = 3))
    private boolean keepCoordsToEndIfFromNether(boolean original, ServerWorld dest) {
        return dest.getRegistryKey() == World.END || original;
    }

    @WrapOperation(method = "changeDimension", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;createEndSpawnPlatform(Lnet/minecraft/server/world/ServerWorld;)V"))
    private void makeSpawnPlatformInOldPosIfFromNether(ServerWorld dest, Operation<Void> original) {
        if (this.getServerWorld().getRegistryKey() == World.OVERWORLD && dest.getRegistryKey() == World.END) {
            original.call(dest);
        } else {
            // ServerWorld#createEndSpawnPlatform
            int x = MathHelper.floor(this.getX());
            int y = MathHelper.floor(this.getY()) - 2;
            int z = MathHelper.floor(this.getZ());
            BlockPos.iterate(x - 2, y + 1, z - 2, x + 2, y + 3, z + 2).forEach(pos -> dest.setBlockState(pos, Blocks.AIR.getDefaultState()));
            BlockPos.iterate(x - 2, y, z - 2, x + 2, y, z + 2).forEach(pos -> dest.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState()));
        }
    }
}
