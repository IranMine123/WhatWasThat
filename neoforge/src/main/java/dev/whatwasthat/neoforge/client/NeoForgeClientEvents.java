package dev.whatwasthat.neoforge.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.whatwasthat.neoforge.config.NeoForgeConfig;
import dev.whatwasthat.perception.DetectionResult;
import dev.whatwasthat.perception.PerceptionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;

public class NeoForgeClientEvents {

    private static final double DEBUG_RENDER_DISTANCE = 32.0D;
    private static final double DEBUG_RENDER_DISTANCE_SQUARED =
            DEBUG_RENDER_DISTANCE * DEBUG_RENDER_DISTANCE;

    @SubscribeEvent
    public static void onRenderLivingPost(RenderLivingEvent.Post<?, ?> event) {
        if (!NeoForgeConfig.CONFIG.debugOverlay.get()) {
            return;
        }

        LivingEntity entity = event.getEntity();

        // Don't render the debug text above the player.
        if (entity instanceof net.minecraft.world.entity.player.Player) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            return;
        }

        // Don't render debug text for mobs that are too far away.
        if (minecraft.player.distanceToSqr(entity) > DEBUG_RENDER_DISTANCE_SQUARED) {
            return;
        }

        DetectionResult result =
                PerceptionManager.getResult(entity, minecraft.player);

        String text = String.format(
                "Suspicion: %.2f | State: %s",
                result.detection(),
                result.state()
        );

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource buffer = event.getMultiBufferSource();
        Font font = minecraft.font;

        poseStack.pushPose();

        // Move the text above the mob's head.
        poseStack.translate(
                0.0D,
                entity.getBbHeight() + 0.5D,
                0.0D
        );

        // Make the text face the camera.
        poseStack.mulPose(
                minecraft.getEntityRenderDispatcher().cameraOrientation()
        );

        // Minecraft's nameplate scale.
        float scale = 0.025F;
        poseStack.scale(scale, -scale, scale);

        float x = -font.width(text) / 2.0F;

        font.drawInBatch(
                text,
                x,
                0,
                0xFFFFFF,
                false,
                poseStack.last().pose(),
                buffer,
                Font.DisplayMode.NORMAL,
                0,
                15728880
        );

        poseStack.popPose();
    }
}