package com.aura.client.module.setting;

public final class BooleanSetting extends Setting<Boolean> {
    public BooleanSetting(String id, String name, boolean value) {
        super(id, name, value);
    }

    public void toggle() {
        setValue(!value());
    }

    @Override
    public void load(Object raw) {
        if (raw instanceof Boolean value) {
            setValue(value);
        }
    }
}
