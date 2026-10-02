package dev.whatwasthat.perception;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

public final class InvestigationController {

    private static final double ARRIVAL_DISTANCE = 1.75D;
    private static final double ARRIVAL_DISTANCE_SQR =
            ARRIVAL_DISTANCE * ARRIVAL_DISTANCE;

    private static final double INVESTIGATION_SPEED = 1.0D;

    private static final int SEARCH_DURATION = 60;

    private InvestigationController() {
    }

    public static void tick(Mob mob) {
        InvestigationManager.InvestigationMemory memory =
                InvestigationManager.get(mob);

        if (memory == null || !memory.investigating()) {
            return;
        }

        Vec3 lastKnownPosition = memory.lastKnownPosition();

        if (lastKnownPosition == null) {
            InvestigationManager.stop(mob);
            return;
        }

        if (memory.searching()) {
            tickSearch(mob, memory);
            return;
        }

        tickMovement(mob, memory, lastKnownPosition);
    }

    private static void tickMovement(
            Mob mob,
            InvestigationManager.InvestigationMemory memory,
            Vec3 targetPosition
    ) {
        double distanceSqr =
                mob.distanceToSqr(targetPosition);

        if (distanceSqr <= ARRIVAL_DISTANCE_SQR) {
            arrive(mob, memory);
            return;
        }

        mob.getNavigation().moveTo(
                targetPosition.x,
                targetPosition.y,
                targetPosition.z,
                INVESTIGATION_SPEED
        );
    }

    private static void arrive(
            Mob mob,
            InvestigationManager.InvestigationMemory memory
    ) {
        mob.getNavigation().stop();

        memory.beginSearch();

        lookAtPosition(
                mob,
                memory.lastKnownPosition()
        );
    }

    private static void tickSearch(
            Mob mob,
            InvestigationManager.InvestigationMemory memory
    ) {
        mob.getNavigation().stop();

        memory.tickSearch();

        Vec3 position = memory.lastKnownPosition();

        if (position != null) {
            performSearchLook(
                    mob,
                    position,
                    memory.searchTicks()
            );
        }

        if (memory.searchTicks() >= SEARCH_DURATION) {
            InvestigationManager.stop(mob);
        }
    }

    private static void performSearchLook(
            Mob mob,
            Vec3 position,
            int ticks
    ) {
        /*
         * Sweep the mob's attention around the last-known position.
         *
         * Four directions give the investigation a visible
         * "looking around" behavior without introducing random AI yet.
         */
        int phase = (ticks / 10) % 4;

        double radius = 3.0D;

        double xOffset;
        double zOffset;

        switch (phase) {
            case 0 -> {
                xOffset = radius;
                zOffset = 0.0D;
            }

            case 1 -> {
                xOffset = 0.0D;
                zOffset = radius;
            }

            case 2 -> {
                xOffset = -radius;
                zOffset = 0.0D;
            }

            default -> {
                xOffset = 0.0D;
                zOffset = -radius;
            }
        }

        mob.getLookControl().setLookAt(
                position.x + xOffset,
                position.y + 1.0D,
                position.z + zOffset
        );
    }

    private static void lookAtPosition(
            Mob mob,
            Vec3 position
    ) {
        mob.getLookControl().setLookAt(
                position.x,
                position.y + 1.0D,
                position.z
        );
    }
}