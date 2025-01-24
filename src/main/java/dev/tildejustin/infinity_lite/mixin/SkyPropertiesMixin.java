package dev.tildejustin.infinity_lite.mixin;

import dev.tildejustin.infinity_lite.InfinityLite;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkyProperties.class)
public abstract class SkyPropertiesMixin {
    @Inject(method = "method_29092", at = @At("TAIL"))
    private static void addCustomSkyProperties(Object2ObjectArrayMap<RegistryKey<DimensionType>, SkyProperties> map, CallbackInfo ci) {
        map.put(InfinityLite.pz4c, new SkyProperties(61, true, SkyProperties.SkyType.NORMAL, true, true) {
            @Override
            public Vec3d adjustSkyColor(Vec3d color, float sunHeight) {
                return color.multiply(
                        (double) sunHeight * 0.3743471222475755 + 0.05160870815818097,
                        (double) sunHeight * 0.2450798164429634 + 0.9323807919144077,
                        (double) sunHeight * 0.7136559214110227 + 0.5336386436827816
                );
            }

            @Override
            public boolean useThickFog(int camX, int camY) {
                return true;
            }
        });
    }
}
