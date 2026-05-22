package com.aura.client.module;

import com.aura.client.config.AuraConfig;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class ModuleRegistry {
    private final AuraConfig config;
    private final List<Module> modules = new ArrayList<>();
    private final Map<Category, List<Module>> byCategory = new EnumMap<>(Category.class);

    public ModuleRegistry(AuraConfig config) {
        this.config = config;
        for (Category category : Category.values()) {
            byCategory.put(category, new ArrayList<>());
        }
    }

    public void register(Module module) {
        modules.add(module);
        byCategory.get(module.category()).add(module);
    }

    public void load() {
        for (Module module : modules) {
            if (config.modules.containsKey(module.id())) {
                module.load(config.module(module.id()));
            } else {
                module.save(config.module(module.id()));
            }
        }
        config.save();
    }

    public void save() {
        for (Module module : modules) {
            module.save(config.module(module.id()));
        }
        config.save();
    }

    public void tick(MinecraftClient client) {
        for (Module module : modules) {
            if (module.enabled()) {
                module.tick(client);
            }
        }
    }

    public List<Module> all() {
        return List.copyOf(modules);
    }

    public List<Module> in(Category category) {
        return List.copyOf(byCategory.get(category));
    }

    public Optional<Module> find(String id) {
        return modules.stream().filter(module -> module.id().equals(id)).findFirst();
    }
}
