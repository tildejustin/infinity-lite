package dev.tildejustin.infinity_lite;

import me.contaria.speedrunapi.config.api.SpeedrunConfig;
import net.minecraft.block.Block;

public class InfinityLite implements SpeedrunConfig {
    public static Block NEITHER_PORTAL;

    public static InfinityLite config;

    public boolean enabled = true;

    {
        config = this;
    }

    @Override
    public String modID() {
        return "infinity-lite";
    }
}
