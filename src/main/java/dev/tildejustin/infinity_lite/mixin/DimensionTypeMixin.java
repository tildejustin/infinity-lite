package dev.tildejustin.infinity_lite.mixin;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import dev.tildejustin.infinity_lite.InfinityLite;
import net.minecraft.util.registry.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.dimension.*;
import net.minecraft.world.gen.chunk.SurfaceChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DimensionType.class)
public abstract class DimensionTypeMixin {
    @Inject(method = "createDefaultDimensionOptions", at = @At("TAIL"))
    private static void createDefaultDimensionOptions(CallbackInfoReturnable<SimpleRegistry<DimensionOptions>> cir) {
        List<Pair<Biome.MixedNoisePoint, Biome>> list = Lists.newArrayList();
        list.add(Pair.of(new Biome.MixedNoisePoint(0.57402825f, -0.7858064f, 0.6351423f, 0.626071f, 0.9219661f), Biomes.THE_VOID));
        list.add(Pair.of(new Biome.MixedNoisePoint(0.6562673f, 0.20634985f, -0.47522688f, -0.84682846f, 0.5401123f), Biomes.THE_VOID));
        list.add(Pair.of(new Biome.MixedNoisePoint(-0.35537648f, -0.52680445f, -0.4154086f, -0.73337233f, 0.5077289f), Biomes.THE_VOID));
        list.add(Pair.of(new Biome.MixedNoisePoint(-0.578076f, -0.14236069f, 0.5370338f, 0.75265884f, 0.58179873f), Biomes.THE_VOID));
        cir.getReturnValue().add(InfinityLite.PZ4C, new DimensionOptions(() -> InfinityLite.Pz4c, new SurfaceChunkGenerator(new MultiNoiseBiomeSource(1815005204, list), 1269985845, InfinityLite.chunkGeneratorType)));
        cir.getReturnValue().markLoaded(InfinityLite.PZ4C);
    }

    @Inject(method = "addRegistryDefaults", at = @At("TAIL"))
    private static void addRegistryDefaults(RegistryTracker.Modifiable registryTracker, CallbackInfoReturnable<RegistryTracker.Modifiable> cir) {
        cir.getReturnValue().addDimensionType(InfinityLite.pz4c, InfinityLite.Pz4c);
    }
}
