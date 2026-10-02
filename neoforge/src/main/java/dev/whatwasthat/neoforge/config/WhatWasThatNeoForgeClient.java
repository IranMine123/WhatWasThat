package dev.whatwasthat.neoforge.config;

import dev.whatwasthat.config.WhatWasThatConfigScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

/** Client-only entry point: registers the "Config" button in NeoForge's mod list. */
@Mod(value = "whatwasthat", dist = Dist.CLIENT)
public class WhatWasThatNeoForgeClient {
    public WhatWasThatNeoForgeClient(ModContainer container) {
        if (ModList.get().isLoaded("cloth_config")) {
            container.registerExtensionPoint(IConfigScreenFactory.class,
                    (modContainer, parent) -> WhatWasThatConfigScreen.create(parent));
        }
    }
}