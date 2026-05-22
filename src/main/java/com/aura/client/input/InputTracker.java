package com.aura.client.input;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayDeque;
import java.util.Deque;

public final class InputTracker {
    private static final Deque<Long> leftClicks = new ArrayDeque<>();
    private static final Deque<Long> rightClicks = new ArrayDeque<>();
    private static boolean leftDown;
    private static boolean rightDown;

    private InputTracker() {
    }

    public static void tick(MinecraftClient client) {
        if (client.getWindow() == null) {
            return;
        }

        long window = client.getWindow().getHandle();
        long now = System.currentTimeMillis();
        boolean newLeftDown = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
        boolean newRightDown = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;

        if (newLeftDown && !leftDown) {
            leftClicks.addLast(now);
        }
        if (newRightDown && !rightDown) {
            rightClicks.addLast(now);
        }

        leftDown = newLeftDown;
        rightDown = newRightDown;
        prune(leftClicks, now);
        prune(rightClicks, now);
    }

    public static int leftCps() {
        prune(leftClicks, System.currentTimeMillis());
        return leftClicks.size();
    }

    public static int rightCps() {
        prune(rightClicks, System.currentTimeMillis());
        return rightClicks.size();
    }

    private static void prune(Deque<Long> clicks, long now) {
        while (!clicks.isEmpty() && now - clicks.peekFirst() > 1000L) {
            clicks.removeFirst();
        }
    }
}
