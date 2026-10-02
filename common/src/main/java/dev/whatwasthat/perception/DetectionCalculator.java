package dev.whatwasthat.perception;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class DetectionCalculator {

    private DetectionCalculator() {
    }

    public static DetectionResult calculate(PerceptionContext context) {
        LivingEntity observer = context.observer();
        LivingEntity target = context.target();

        float distanceFactor = calculateDistanceFactor(observer, target);
        float facingFactor = calculateFacingFactor(observer, target);
        float movementFactor = calculateMovementFactor(target);
        float sneakingFactor = calculateSneakingFactor(target);
        float visibilityFactor = 1.0F;
        float lineOfSightFactor = calculateLineOfSightFactor(observer, target);

        return calculate(
                distanceFactor,
                facingFactor,
                movementFactor,
                sneakingFactor,
                visibilityFactor,
                lineOfSightFactor
        );
    }

    public static DetectionResult calculate(
            float distanceFactor,
            float facingFactor,
            float movementFactor,
            float sneakingFactor,
            float visibilityFactor,
            float lineOfSightFactor
    ) {
        float detection =
                distanceFactor
                        * facingFactor
                        * movementFactor
                        * sneakingFactor
                        * visibilityFactor
                        * lineOfSightFactor;

        detection = Math.clamp(detection, 0.0F, 1.0F);

        return new DetectionResult(
                detection,
                determineState(detection)
        );
    }

    private static float calculateDistanceFactor(
            LivingEntity observer,
            LivingEntity target
    ) {
        double distance = observer.distanceTo(target);

        // Initial perception range.
        double maximumDistance = 8.0D;

        if (distance >= maximumDistance) {
            return 0.0F;
        }

        return (float) (1.0D - (distance / maximumDistance));
    }

    private static float calculateFacingFactor(
            LivingEntity observer,
            LivingEntity target
    ) {
        Vec3 observerLook = observer.getLookAngle().normalize();

        Vec3 directionToTarget = target.position()
                .subtract(observer.position())
                .normalize();

        double dot = observerLook.dot(directionToTarget);

        /*
         * dot:
         *
         *  1.0 = directly in front
         *  0.0 = directly to the side
         * -1.0 = directly behind
         */
        if (dot > 0.3) {
            return 10F;
        }

        if (dot > -0.5D) {
            return 2.25F;
        }

        if (dot >= -1.0D) {
            return 1.5F;
        }

        return (float) Math.min(1.0D, dot);
    }

    private static float calculateMovementFactor(LivingEntity target) {
        double speed = target.getDeltaMovement().horizontalDistance();

        if (speed <= 0.01D) {
            return 0.25F;
        }

        if (speed <= 0.05D) {
            return 0.5F;
        }

        if (speed <= 0.12D) {
            return 0.8F;
        }

        return 1.0F;
    }

    private static float calculateSneakingFactor(LivingEntity target) {
        return target.isCrouching() ? 0.35F : 1.0F;
    }

    private static float calculateLineOfSightFactor(
            LivingEntity observer,
            LivingEntity target
    ) {
        return observer.hasLineOfSight(target) ? 1.0F : 0.0F;
    }

    private static PerceptionState determineState(float detection) {
        if (detection >= 0.85F) {
            return PerceptionState.DETECTED;
        }

        if (detection >= 0.60F) {
            return PerceptionState.ALERT;
        }

        if (detection >= 0.30F) {
            return PerceptionState.SUSPICIOUS;
        }

        if (detection >= 0.10F) {
            return PerceptionState.CURIOUS;
        }

        return PerceptionState.UNAWARE;
    }
}