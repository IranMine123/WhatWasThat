package dev.whatwasthat.neoforge.client;

import dev.whatwasthat.neoforge.config.NeoForgeConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = "whatwasthat", dist = Dist.CLIENT)
public class NeoForgeClient {
    public NeoForgeClient(ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, NeoForgeConfig.CONFIG_SPEC);

        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        NeoForge.EVENT_BUS.register(NeoForgeClientEvents.class);
    }
}