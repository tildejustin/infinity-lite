package dev.tildejustin.infinity_lite;

import net.minecraft.block.Block;
import org.mcsr.speedrunapi.config.api.SpeedrunConfig;
import org.mcsr.speedrunapi.config.api.annotations.Config;

public class InfinityLite implements SpeedrunConfig {
    @Config.Ignored
    public static Block NEITHER_PORTAL;

    public static boolean enabled = true;

    @Override
    public String modID() {
        return "infinity-lite";
    }
}
