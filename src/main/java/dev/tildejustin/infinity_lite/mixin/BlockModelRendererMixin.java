package dev.tildejustin.infinity_lite.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.tildejustin.infinity_lite.InfinityLite;
import net.minecraft.block.*;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Util;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(BlockModelRenderer.class)
public abstract class BlockModelRendererMixin {
    @Unique
    private final Vector3f[] shades = Util.make(new Vector3f[4], list -> {
        list[0] = new Vector3f(0.05974573f, 0.852096f, 0.4237004f);
        list[1] = new Vector3f(0.6781415f, 0.035365462f, 0.42299908f);
        list[2] = new Vector3f(0.7539159f, 0.1815679f, 0.3309874f);
        list[3] = new Vector3f(0.6361866f, 0.16823453f, 0.14385635f);
    });

    @ModifyArgs(method = "renderQuad(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/model/BakedQuad;FFFFIIIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumer;quad(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/model/BakedQuad;[FFFF[IIZ)V"))
    private void intensifyColors(Args args, @Local(argsOnly = true) BlockRenderView world, @Local(argsOnly = true) BlockState state) {
        if (((ChunkRendererRegion) world).world.getDimensionRegistryKey() == InfinityLite.pz4c) {
            int i = -1;
            if (state == Blocks.LIGHT_BLUE_WOOL.getDefaultState()) i = 2;
            else if (state == Blocks.TNT.getDefaultState()) i = 0;
            if (i == -1) return;
            // int i = Block.STATE_IDS.getId(state);
            Vector3f v = this.shades[i /* i % this.shades.length */];
            args.set(3, args.<Float>get(3) * v.getX());
            args.set(4, args.<Float>get(4) * v.getY());
            args.set(5, args.<Float>get(5) * v.getZ());
        }
    }
}
