package dev.whatwasthat.perception;

public final class DetectionResult {
    private final float detection;
    private final PerceptionState state;

    public DetectionResult(float detection, PerceptionState state) {
        this.detection = detection;
        this.state = state;
    }

    public float detection() {
        return detection;
    }

    public PerceptionState state() {
        return state;
    }

    public boolean detected() {
        return state == PerceptionState.DETECTED;
    }
}