package dev.whatwasthat.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class WhatWasThatConfigScreen {
    private WhatWasThatConfigScreen() {}

    public static Screen create(Screen parent) {
        WhatWasThatConfig cfg = WhatWasThatConfig.get();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("config.whatwasthat.title"))
                .setSavingRunnable(WhatWasThatConfig::save);

        ConfigCategory general = builder.getOrCreateCategory(
                Component.translatable("config.whatwasthat.category.general"));
        ConfigEntryBuilder eb = builder.entryBuilder();

        general.addEntry(eb.startBooleanToggle(
                        Component.translatable("config.whatwasthat.stealthEnabled"), cfg.stealthEnabled)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("config.whatwasthat.stealthEnabled.tooltip"))
                .setSaveConsumer(v -> cfg.stealthEnabled = v)
                .build());

        general.addEntry(eb.startIntSlider(
                        Component.translatable("config.whatwasthat.detectionRange"), cfg.detectionRange, 1, 64)
                .setDefaultValue(16)
                .setTooltip(Component.translatable("config.whatwasthat.detectionRange.tooltip"))
                .setSaveConsumer(v -> cfg.detectionRange = v)
                .build());

        general.addEntry(eb.startBooleanToggle(
                        Component.translatable("config.whatwasthat.sneakReducesNoise"), cfg.sneakReducesNoise)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("config.whatwasthat.sneakReducesNoise.tooltip"))
                .setSaveConsumer(v -> cfg.sneakReducesNoise = v)
                .build());

        return builder.build();
    }
}