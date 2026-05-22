package com.aura.client.module.setting;

public abstract class Setting<T> {
    private final String id;
    private final String name;
    private T value;

    protected Setting(String id, String name, T value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public final String id() {
        return id;
    }

    public final String name() {
        return name;
    }

    public final T value() {
        return value;
    }

    public final void setValue(T value) {
        this.value = value;
    }

    public abstract void load(Object raw);
}
