package dev.whatwasthat.neoforge;

import dev.whatwasthat.WhatWasThat;
import dev.whatwasthat.neoforge.perception.NeoForgePerceptionEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.common.Mod;

@Mod(WhatWasThat.MOD_ID)
public final class WhatWasThatNeoForge {

    public WhatWasThatNeoForge(IEventBus modEventBus) {
        WhatWasThat.init();

        NeoForge.EVENT_BUS.register(NeoForgePerceptionEvents.class);
    }
}