package dev.neoforge;

import net.neoforged.fml.common.Mod;

import dev.WhatWasThat;

@Mod(WhatWasThat.MOD_ID)
public final class WhatWasThatNeoForge {
    public WhatWasThatNeoForge() {
        // Run our common setup.
        WhatWasThat.init();
    }
}
