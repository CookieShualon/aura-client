package com.aura.client.module;

public enum Category {
    HUD("HUD"),
    VISUAL("Visual");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
