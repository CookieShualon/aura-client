package com.aura.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class AuraConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("aura.json");

    public GuiState gui = new GuiState();
    public Map<String, ModuleState> modules = new HashMap<>();

    public static AuraConfig load() {
        if (!Files.exists(PATH)) {
            return new AuraConfig();
        }

        try (Reader reader = Files.newBufferedReader(PATH)) {
            AuraConfig loaded = GSON.fromJson(reader, AuraConfig.class);
            return loaded == null ? new AuraConfig() : loaded;
        } catch (IOException ignored) {
            return new AuraConfig();
        }
    }

    public void save() {
        try {
            Files.createDirectories(PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(PATH)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException ignored) {
        }
    }

    public ModuleState module(String id) {
        return modules.computeIfAbsent(id, ignored -> new ModuleState());
    }

    public static final class GuiState {
        public int x = 80;
        public int y = 60;
        public int width = 420;
        public int height = 280;
    }

    public static final class ModuleState {
        public boolean enabled = false;
        public boolean expanded = false;
        public int hudX = 8;
        public int hudY = 8;
        public Map<String, Object> settings = new HashMap<>();
    }
}
