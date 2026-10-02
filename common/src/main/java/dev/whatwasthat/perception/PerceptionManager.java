package dev.whatwasthat.perception;

import net.minecraft.world.entity.LivingEntity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class PerceptionManager {

    private static final Map<PerceptionKey, PerceptionMemory> MEMORIES =
            new ConcurrentHashMap<>();

    private PerceptionManager() {
    }

    public static DetectionResult evaluate(
            LivingEntity observer,
            LivingEntity target
    ) {
        if (!observer.isAlive() || !target.isAlive()) {
            return new DetectionResult(
                    0.0F,
                    PerceptionState.UNAWARE
            );
        }

        PerceptionKey key = new PerceptionKey(
                observer.getUUID(),
                target.getUUID()
        );

        PerceptionMemory memory =
                MEMORIES.computeIfAbsent(
                        key,
                        ignored -> new PerceptionMemory()
                );

        PerceptionContext context =
                new PerceptionContext(observer, target);

        DetectionResult instantaneous =
                DetectionCalculator.calculate(context);

        memory.update(instantaneous.detection());

        return memory.result();
    }

    public static void forget(
            LivingEntity observer,
            LivingEntity target
    ) {
        MEMORIES.remove(
                new PerceptionKey(
                        observer.getUUID(),
                        target.getUUID()
                )
        );
    }

    private record PerceptionKey(
            UUID observer,
            UUID target
    ) {
    }

    private static final class PerceptionMemory {

        private float suspicion;

        private void update(float detection) {
            if (detection > 0.0F) {
                suspicion += detection * 0.08F;
            } else {
                suspicion -= 0.025F;
            }

            suspicion = Math.clamp(suspicion, 0.0F, 1.0F);
        }

        private DetectionResult result() {
            return new DetectionResult(
                    suspicion,
                    determineState(suspicion)
            );
        }

        private PerceptionState determineState(float value) {
            if (value >= 0.85F) {
                return PerceptionState.DETECTED;
            }

            if (value >= 0.60F) {
                return PerceptionState.ALERT;
            }

            if (value >= 0.30F) {
                return PerceptionState.SUSPICIOUS;
            }

            if (value >= 0.10F) {
                return PerceptionState.CURIOUS;
            }

            return PerceptionState.UNAWARE;
        }
    }
}