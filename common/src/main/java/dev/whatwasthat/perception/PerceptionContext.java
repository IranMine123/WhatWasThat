package dev.whatwasthat.perception;

import net.minecraft.world.entity.LivingEntity;

public final class PerceptionContext {
    private final LivingEntity observer;
    private final LivingEntity target;

    public PerceptionContext(LivingEntity observer, LivingEntity target) {
        this.observer = observer;
        this.target = target;
    }

    public LivingEntity observer() {
        return observer;
    }

    public LivingEntity target() {
        return target;
    }
}