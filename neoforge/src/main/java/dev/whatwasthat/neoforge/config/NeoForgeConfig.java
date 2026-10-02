package dev.whatwasthat.neoforge.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class NeoForgeConfig {
    public static final NeoForgeConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    public final ModConfigSpec.BooleanValue debugOverlay;

    static {
        Pair<NeoForgeConfig, ModConfigSpec> pair =
                new ModConfigSpec.Builder()
                        .configure(NeoForgeConfig::new);

        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }

    private NeoForgeConfig(ModConfigSpec.Builder builder) {
        builder.push("debug");

        debugOverlay = builder
                .comment("Show What Was That debug information above mobs.")
                .translation("whatwasthat.config.debug_overlay")
                .define("debug_overlay", false);

        builder.pop();
    }
}
