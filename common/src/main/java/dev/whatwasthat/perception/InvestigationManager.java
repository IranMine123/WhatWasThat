package dev.whatwasthat.perception;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class InvestigationManager {

    private static final Map<UUID, InvestigationMemory> INVESTIGATIONS =
            new ConcurrentHashMap<>();

    private InvestigationManager() {
    }

    public static void update(
            Mob mob,
            UUID targetId,
            PerceptionState state,
            Vec3 targetPosition
    ) {
        switch (state) {
            case CURIOUS, SUSPICIOUS, ALERT -> {
                InvestigationMemory memory =
                        INVESTIGATIONS.computeIfAbsent(
                                mob.getUUID(),
                                ignored -> new InvestigationMemory()
                        );

                memory.targetId = targetId;
                memory.lastKnownPosition = targetPosition;
                memory.investigating = true;
            }

            case DETECTED -> {
                InvestigationMemory memory =
                        INVESTIGATIONS.get(mob.getUUID());

                if (memory != null) {
                    memory.targetId = targetId;
                    memory.lastKnownPosition = targetPosition;
                    memory.investigating = false;
                }
            }

            case UNAWARE -> {
                // The controller handles decay and eventual cleanup.
            }
        }
    }

    public static InvestigationMemory get(Mob mob) {
        return INVESTIGATIONS.get(mob.getUUID());
    }

    public static boolean isInvestigating(Mob mob) {
        InvestigationMemory memory = get(mob);

        return memory != null && memory.investigating;
    }

    public static Vec3 getLastKnownPosition(Mob mob) {
        InvestigationMemory memory = get(mob);

        return memory == null
                ? null
                : memory.lastKnownPosition;
    }

    public static UUID getTargetId(Mob mob) {
        InvestigationMemory memory = get(mob);

        return memory == null
                ? null
                : memory.targetId;
    }

    public static void stop(Mob mob) {
        INVESTIGATIONS.remove(mob.getUUID());
    }

    public static final class InvestigationMemory {

        private UUID targetId;
        private Vec3 lastKnownPosition;

        private boolean investigating;
        private boolean searching;

        private int searchTicks;

        public UUID targetId() {
            return targetId;
        }

        public Vec3 lastKnownPosition() {
            return lastKnownPosition;
        }

        public boolean investigating() {
            return investigating;
        }

        public boolean searching() {
            return searching;
        }

        public int searchTicks() {
            return searchTicks;
        }

        public void beginSearch() {
            searching = true;
            searchTicks = 0;
        }

        public void tickSearch() {
            searchTicks++;
        }

        public void stopSearch() {
            searching = false;
            searchTicks = 0;
        }
    }
}