package dev.whatwasthat.neoforge.perception;

import dev.whatwasthat.perception.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public final class NeoForgePerceptionEvents {

    private static final double PERCEPTION_RANGE = 32.0D;

    private NeoForgePerceptionEvents() {
    }

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof Mob mob)) {
            return;
        }

        if (mob.level().isClientSide()) {
            return;
        }

        evaluatePlayers(mob);

        InvestigationController.tick(mob);
    }

    private static void evaluatePlayers(Mob mob) {
        for (Player player : mob.level().players()) {
            if (!(player instanceof ServerPlayer serverPlayer)) {
                continue;
            }

            if (!serverPlayer.isAlive()) {
                continue;
            }

            if (mob.distanceToSqr(serverPlayer) > PERCEPTION_RANGE * PERCEPTION_RANGE) {
                continue;
            }

            DetectionResult result =
                    PerceptionManager.evaluate(mob, serverPlayer);

            handleDetection(mob, serverPlayer, result);
        }
    }

    private static void handleDetection(
            Mob mob,
            ServerPlayer player,
            DetectionResult result
    ) {
        InvestigationManager.update(
                mob,
                player.getUUID(),
                result.state(),
                player.position()
        );

        System.out.println(
                "[What Was That?] "
                        + mob.getName().getString()
                        + " -> "
                        + player.getName().getString()
                        + " | suspicion="
                        + String.format("%.2f", result.detection())
                        + " | state="
                        + result.state()
                        + " | investigating="
                        + InvestigationManager.isInvestigating(mob)
        );
    }
}