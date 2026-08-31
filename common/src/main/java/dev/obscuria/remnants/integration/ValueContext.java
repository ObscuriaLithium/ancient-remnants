package dev.obscuria.remnants.integration;

public final class ValueContext {

    private final float baseValue;
    private float flatBonus = 0.0f;
    private float multiplier = 1.0f;

    public ValueContext(float baseDamage) {
        this.baseValue = baseDamage;
    }

    public ValueContext add(float damage) {
        this.flatBonus += damage;
        return this;
    }

    public ValueContext multiply(float multiplier) {
        this.multiplier *= multiplier;
        return this;
    }

    public float result() {
        return (baseValue + flatBonus) * multiplier;
    }
}