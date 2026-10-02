package dev.whatwasthat.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WhatWasThatConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Path path;
    private static WhatWasThatConfig instance = new WhatWasThatConfig();

    // ---- Options ----
    public boolean stealthEnabled = true;
    public int detectionRange = 16;
    public boolean sneakReducesNoise = true;

    public static WhatWasThatConfig get() {
        return instance;
    }

    public static void init(Path configDir) {
        path = configDir.resolve("whatwasthat.json");
        load();
    }

    public static void load() {
        if (path == null) return;
        try {
            if (Files.exists(path)) {
                WhatWasThatConfig loaded = GSON.fromJson(Files.readString(path), WhatWasThatConfig.class);
                if (loaded != null) instance = loaded;
            }
        } catch (Exception e) {
            System.err.println("[WhatWasThat] Failed to read config, using defaults: " + e);
        }
        instance.validate();
        save(); // writes defaults / newly added fields
    }

    public static void save() {
        if (path == null) return;
        try {
            instance.validate();
            Files.writeString(path, GSON.toJson(instance));
        } catch (IOException e) {
            System.err.println("[WhatWasThat] Failed to save config: " + e);
        }
    }

    private void validate() {
        detectionRange = Math.clamp(detectionRange, 1, 64);
    }
}
