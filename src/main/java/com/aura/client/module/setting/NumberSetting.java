package com.aura.client.module.setting;

public final class NumberSetting extends Setting<Double> {
    private final double min;
    private final double max;
    private final double step;

    public NumberSetting(String id, String name, double value, double min, double max, double step) {
        super(id, name, value);
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public double min() {
        return min;
    }

    public double max() {
        return max;
    }

    public double step() {
        return step;
    }

    public void setFromRatio(double ratio) {
        double clampedRatio = Math.max(0.0, Math.min(1.0, ratio));
        double raw = min + (max - min) * clampedRatio;
        double stepped = Math.round(raw / step) * step;
        setValue(Math.max(min, Math.min(max, stepped)));
    }

    @Override
    public void load(Object raw) {
        if (raw instanceof Number number) {
            setValue(Math.max(min, Math.min(max, number.doubleValue())));
        }
    }
}
