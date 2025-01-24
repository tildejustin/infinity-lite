package dev.tildejustin.infinity_lite;

import com.google.common.collect.Maps;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.*;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.HorizontalVoronoiBiomeAccessType;
import net.minecraft.world.dimension.*;
import net.minecraft.world.gen.chunk.*;

import java.util.*;

public class InfinityLite implements ClientModInitializer {
    public static BlockEntityType<NetherPortalBlockEntity> type;
    public static BooleanProperty tint = BooleanProperty.of("tint");

    public static final RegistryKey<DimensionOptions> PZ4C = RegistryKey.of(Registry.DIMENSION_OPTIONS, new Identifier("pz4c"));
    public static final RegistryKey<DimensionType> pz4c = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, new Identifier("pz4c"));
    public static final RegistryKey<World> pZ4c = RegistryKey.of(Registry.WORLD_KEY, new Identifier("pz4c"));

    public static ChunkGeneratorType chunkGeneratorType;
    public static DimensionType Pz4c;

    @Override
    public void onInitializeClient() {
        type = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("nether"), BlockEntityType.Builder.create(NetherPortalBlockEntity::new, Blocks.NETHER_PORTAL).build(null));
        Pz4c = new DimensionType(
                OptionalLong.empty(),
                true,
                true,
                true,
                true,
                false,
                false,
                false,
                false,
                false,
                false,
                256,
                HorizontalVoronoiBiomeAccessType.INSTANCE,
                null,
                0.75832975f
        ) {
            @Override
            public float getSkyAngle(long time) {
                double d = MathHelper.fractionalPart(time / 100.0 /* ticks per day */ - 0.25);
                double e = 0.5 - Math.cos(d * Math.PI) / 2.0;
                return (float) (d * 2.0 + e) / 3.0F;
            }
        };

        chunkGeneratorType = new ChunkGeneratorType(
                new StructuresConfig(Optional.empty(), Maps.newHashMap()),
                new NoiseConfig(
                        128,
                        new NoiseSamplingConfig(1.0, 3.0, 80.0, 60.0),
                        new SlideConfig(120, 3, 0),
                        new SlideConfig(320, 4, -1),
                        1,
                        2,
                        0.0,
                        0.019921875,
                        false,
                        false,
                        false,
                        false
                ),
                Blocks.LIGHT_BLUE_WOOL.getDefaultState(),
                Blocks.TNT.getDefaultState().with(TntBlock.UNSTABLE, false),
                127,
                0,
                63,
                false,
                Optional.empty()
        );
    }
}
