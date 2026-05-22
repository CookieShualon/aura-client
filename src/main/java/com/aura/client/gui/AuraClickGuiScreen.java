package com.aura.client.gui;

import com.aura.client.config.AuraConfig;
import com.aura.client.module.Category;
import com.aura.client.module.Module;
import com.aura.client.module.ModuleRegistry;
import com.aura.client.module.setting.BooleanSetting;
import com.aura.client.module.setting.NumberSetting;
import com.aura.client.module.setting.Setting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class AuraClickGuiScreen extends Screen {
    private static final int TITLE_BAR_HEIGHT = 40;
    private static final int CATEGORY_WIDTH = 112;
    private static final int ROW_HEIGHT = 34;
    private static final int SETTING_HEIGHT = 24;
    private static final int OUTLINE = 0xFF27303B;
    private static final int PANEL = 0xF20B0F14;
    private static final int PANEL_SOFT = 0xF2131820;
    private static final int PANEL_LIGHT = 0xF21A222D;
    private static final int ACCENT = 0xFF5DF2D6;
    private static final int ACCENT_DEEP = 0xFF138F80;
    private static final int TEXT = 0xFFF4F7FB;
    private static final int MUTED = 0xFFA4AFBD;
    private static final int FAINT = 0xFF667181;

    private final ModuleRegistry modules;
    private final AuraConfig config;
    private final Map<Category, Integer> scrollOffsets = new EnumMap<>(Category.class);
    private Category selectedCategory = Category.HUD;
    private static final Identifier TITLE_FONT = Identifier.of("aura", "inter_title");
    private static final Identifier UI_FONT = Identifier.of("aura", "inter_ui");
    private static final Identifier BODY_FONT = Identifier.of("aura", "inter_body");
    private static final Identifier TINY_FONT = Identifier.of("aura", "inter_tiny");
    private NumberSetting draggingSlider;
    private int sliderX;
    private int sliderWidth;

    public AuraClickGuiScreen(ModuleRegistry modules, AuraConfig config) {
        super(Text.literal("Aura"));
        this.modules = modules;
        this.config = config;
        for (Category category : Category.values()) {
            scrollOffsets.put(category, 0);
        }
    }

    @Override
    protected void init() {
        fitToScreen();
        centerWindow();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        fitToScreen();
        int x = config.gui.x;
        int y = config.gui.y;
        int width = config.gui.width;
        int height = config.gui.height;

        drawRoundedRect(context, x - 1, y - 1, width + 2, height + 2, 8, OUTLINE);
        drawRoundedRect(context, x, y, width, height, 8, PANEL);
        context.fillGradient(x + 1, y + 1, x + width - 1, y + TITLE_BAR_HEIGHT, 0xFF19212C, 0xFF10151D);
        context.fill(x + CATEGORY_WIDTH, y + TITLE_BAR_HEIGHT, x + CATEGORY_WIDTH + 1, y + height - 1, 0xAA27303B);
        context.fill(x + 1, y + TITLE_BAR_HEIGHT - 1, x + width - 1, y + TITLE_BAR_HEIGHT, 0xAA27303B);

        drawText(context, TITLE_FONT, "Aura", x + 18, y + 11, TEXT);
        drawPill(context, x + width - 116, y + 11, 48, 18, "HUD Edit", false);
        drawPill(context, x + width - 62, y + 11, 48, 18, "R-Shift", true);

        renderCategories(context, mouseX, mouseY, x, y);
        renderModules(context, mouseX, mouseY, x, y, width, height);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    private void renderCategories(DrawContext context, int mouseX, int mouseY, int x, int y) {
        int categoryY = y + TITLE_BAR_HEIGHT;
        context.fill(x + 1, categoryY, x + CATEGORY_WIDTH, y + config.gui.height - 1, 0xD80D1219);

        for (Category category : Category.values()) {
            boolean active = selectedCategory == category;
            boolean hovered = inBounds(mouseX, mouseY, x + 8, categoryY + 5, CATEGORY_WIDTH - 16, 24);
            drawRoundedRect(context, x + 8, categoryY + 5, CATEGORY_WIDTH - 16, 24, 5, active ? 0xFF1B2932 : hovered ? 0xFF151D26 : 0x00000000);
            if (active) {
                drawRoundedRect(context, x + 10, categoryY + 12, 3, 10, 2, ACCENT);
            }
            drawText(context, UI_FONT, category.displayName(), x + 20, categoryY + 9, active ? TEXT : MUTED);
            categoryY += ROW_HEIGHT;
        }
    }

    private void renderModules(DrawContext context, int mouseX, int mouseY, int x, int y, int width, int height) {
        int contentX = x + CATEGORY_WIDTH;
        int contentY = y + TITLE_BAR_HEIGHT;
        int contentWidth = width - CATEGORY_WIDTH;
        int contentHeight = height - TITLE_BAR_HEIGHT;
        int scroll = scrollForSelected();
        int rowY = contentY - scroll;
        List<Module> categoryModules = modules.in(selectedCategory);

        context.fill(contentX + 1, contentY, x + width - 1, y + height - 1, 0xF20A0E13);
        if (categoryModules.isEmpty()) {
            drawText(context, BODY_FONT, "No modules yet", contentX + 18, contentY + 18, MUTED);
            return;
        }

        context.enableScissor(contentX, contentY, x + width, y + height);
        for (Module module : categoryModules) {
            boolean hovered = inBounds(mouseX, mouseY, contentX + 10, rowY + 4, contentWidth - 20, ROW_HEIGHT - 6);
            drawRoundedRect(context, contentX + 10, rowY + 4, contentWidth - 20, ROW_HEIGHT - 6, 5, hovered ? PANEL_LIGHT : PANEL_SOFT);
            if (module.enabled()) {
                drawRoundedRect(context, contentX + 12, rowY + 8, 3, ROW_HEIGHT - 14, 2, ACCENT);
            }
            drawText(context, UI_FONT, module.name(), contentX + 24, rowY + 7, module.enabled() ? TEXT : MUTED);
            if (!module.description().isEmpty()) {
                drawText(context, BODY_FONT, trimToWidth(module.description(), contentWidth - 96), contentX + 24, rowY + 20, MUTED);
            }
            renderSwitch(context, contentX + contentWidth - 46, rowY + 12, module.enabled());

            if (!module.settings().isEmpty()) {
                String arrow = module.expanded() ? "v" : ">";
                drawText(context, UI_FONT, arrow, contentX + contentWidth - 62, rowY + 10, MUTED);
            }

            rowY += ROW_HEIGHT;
            if (module.expanded()) {
                for (Setting<?> setting : module.settings()) {
                    renderSetting(context, setting, contentX + 14, rowY, contentWidth - 28);
                    rowY += SETTING_HEIGHT;
                }
            }
        }
        context.disableScissor();
        renderScrollbar(context, contentX + contentWidth - 4, contentY, contentHeight, contentHeightFor(categoryModules), scroll);
    }

    private void renderSetting(DrawContext context, Setting<?> setting, int x, int y, int width) {
        drawRoundedRect(context, x, y + 3, width, SETTING_HEIGHT - 6, 4, 0xAA101822);
        drawText(context, BODY_FONT, setting.name(), x + 8, y + 7, MUTED);

        if (setting instanceof BooleanSetting booleanSetting) {
            renderSwitch(context, x + width - 40, y + 7, booleanSetting.value());
        } else if (setting instanceof NumberSetting numberSetting) {
            int barX = x + width - 104;
            int barY = y + 12;
            int barWidth = 68;
            double ratio = (numberSetting.value() - numberSetting.min()) / (numberSetting.max() - numberSetting.min());
            int filled = (int) Math.round(barWidth * ratio);
            drawRoundedRect(context, barX, barY, barWidth, 5, 3, 0xFF27303B);
            drawRoundedRect(context, barX, barY, filled, 5, 3, ACCENT);
            drawText(context, TINY_FONT, String.format("%.1f", numberSetting.value()), x + width - 30, y + 7, TEXT);
        }
    }

    private void renderSwitch(DrawContext context, int x, int y, boolean enabled) {
        int track = enabled ? ACCENT_DEEP : 0xFF303844;
        context.fill(x, y, x + 28, y + 14, track);
        context.fill(x + 1, y + 1, x + 27, y + 13, track);
        int knobX = enabled ? x + 16 : x + 3;
        context.fill(knobX, y + 3, knobX + 9, y + 11, enabled ? 0xFFFFFFFF : 0xFFB5BFCA);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = config.gui.x;
        int y = config.gui.y;

        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && inBounds(mouseX, mouseY, x + config.gui.width - 116, y + 10, 48, 18)) {
            MinecraftClient.getInstance().setScreen(new HudEditScreen(modules));
            return true;
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && handleCategoryClick(mouseX, mouseY)) {
            return true;
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && handleModuleClick(mouseX, mouseY)) {
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean handleCategoryClick(double mouseX, double mouseY) {
        int categoryY = config.gui.y + TITLE_BAR_HEIGHT;
        for (Category category : Category.values()) {
            if (inBounds(mouseX, mouseY, config.gui.x, categoryY, CATEGORY_WIDTH, ROW_HEIGHT)) {
                selectedCategory = category;
                return true;
            }
            categoryY += ROW_HEIGHT;
        }
        return false;
    }

    private boolean handleModuleClick(double mouseX, double mouseY) {
        int contentX = config.gui.x + CATEGORY_WIDTH;
        int contentY = config.gui.y + TITLE_BAR_HEIGHT;
        int contentWidth = config.gui.width - CATEGORY_WIDTH;
        int contentHeight = config.gui.height - TITLE_BAR_HEIGHT;
        if (!inBounds(mouseX, mouseY, contentX, contentY, contentWidth, contentHeight)) {
            return false;
        }

        int rowY = contentY - scrollForSelected();

        for (Module module : modules.in(selectedCategory)) {
            if (inBounds(mouseX, mouseY, contentX, rowY, contentWidth, ROW_HEIGHT)) {
                if (!module.settings().isEmpty() && mouseX >= contentX + contentWidth - 84 && mouseX <= contentX + contentWidth - 60) {
                    module.setExpanded(!module.expanded());
                } else {
                    module.toggle();
                }
                modules.save();
                return true;
            }

            rowY += ROW_HEIGHT;
            if (module.expanded()) {
                for (Setting<?> setting : module.settings()) {
                    if (inBounds(mouseX, mouseY, contentX + 14, rowY, contentWidth - 28, SETTING_HEIGHT)) {
                        handleSettingClick(setting, mouseX, contentX + 14, contentWidth - 28);
                        modules.save();
                        return true;
                    }
                    rowY += SETTING_HEIGHT;
                }
            }
        }

        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int contentX = config.gui.x + CATEGORY_WIDTH;
        int contentY = config.gui.y + TITLE_BAR_HEIGHT;
        int contentWidth = config.gui.width - CATEGORY_WIDTH;
        int contentHeight = config.gui.height - TITLE_BAR_HEIGHT;

        if (!inBounds(mouseX, mouseY, contentX, contentY, contentWidth, contentHeight)) {
            return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        }

        int current = scrollForSelected();
        int max = maxScrollForSelected();
        int next = clamp(current - (int) Math.round(verticalAmount * 18.0D), 0, max);
        scrollOffsets.put(selectedCategory, next);
        return true;
    }

    private void handleSettingClick(Setting<?> setting, double mouseX, int x, int width) {
        if (setting instanceof BooleanSetting booleanSetting) {
            booleanSetting.toggle();
        } else if (setting instanceof NumberSetting numberSetting) {
            sliderX = x + width - 104;
            sliderWidth = 68;
            draggingSlider = numberSetting;
            numberSetting.setFromRatio((mouseX - sliderX) / sliderWidth);
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingSlider != null) {
            draggingSlider.setFromRatio((mouseX - sliderX) / sliderWidth);
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (draggingSlider != null) {
            draggingSlider = null;
            modules.save();
            return true;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            close();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void close() {
        modules.save();
        MinecraftClient.getInstance().setScreen(null);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private static boolean inBounds(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    private String trimToWidth(String text, int maxWidth) {
        if (textWidth(BODY_FONT, text) <= maxWidth) {
            return text;
        }

        String suffix = "...";
        int suffixWidth = textWidth(BODY_FONT, suffix);
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < text.length(); index++) {
            String next = builder + text.substring(index, index + 1);
            if (textWidth(BODY_FONT, next) + suffixWidth > maxWidth) {
                break;
            }
            builder.append(text.charAt(index));
        }
        return builder + suffix;
    }

    private void drawPill(DrawContext context, int x, int y, int width, int height, String label, boolean active) {
        context.fill(x, y, x + width, y + height, active ? 0xFF1D2B34 : 0xFF141B24);
        drawText(context, TINY_FONT, label, x + width / 2 - textWidth(TINY_FONT, label) / 2, y + 5, active ? TEXT : MUTED);
    }

    private void drawText(DrawContext context, Identifier font, String text, int x, int y, int color) {
        context.drawText(textRenderer, Text.literal(text).setStyle(Style.EMPTY.withFont(font)), x, y, color, false);
    }

    private int textWidth(Identifier font, String text) {
        return textRenderer.getWidth(Text.literal(text).setStyle(Style.EMPTY.withFont(font)));
    }

    private static void drawRoundedRect(DrawContext context, int x, int y, int width, int height, int radius, int color) {
        if ((color >>> 24) == 0 || width <= 0 || height <= 0) {
            return;
        }

        context.fill(x, y, x + width, y + height, color);
    }

    private void renderScrollbar(DrawContext context, int x, int y, int viewportHeight, int contentHeight, int scroll) {
        if (contentHeight <= viewportHeight) {
            return;
        }

        int thumbHeight = Math.max(24, viewportHeight * viewportHeight / contentHeight);
        int travel = viewportHeight - thumbHeight;
        int thumbY = y + (travel * scroll / Math.max(1, contentHeight - viewportHeight));
        context.fill(x, y + 4, x + 2, y + viewportHeight - 4, 0x55323944);
        context.fill(x - 1, thumbY, x + 3, thumbY + thumbHeight, ACCENT);
    }

    private int scrollForSelected() {
        return clamp(scrollOffsets.getOrDefault(selectedCategory, 0), 0, maxScrollForSelected());
    }

    private int maxScrollForSelected() {
        return Math.max(0, contentHeightFor(modules.in(selectedCategory)) - (config.gui.height - TITLE_BAR_HEIGHT));
    }

    private int contentHeightFor(List<Module> categoryModules) {
        int contentHeight = 0;
        for (Module module : categoryModules) {
            contentHeight += ROW_HEIGHT;
            if (module.expanded()) {
                contentHeight += module.settings().size() * SETTING_HEIGHT;
            }
        }
        return contentHeight;
    }

    private void fitToScreen() {
        int margin = 12;
        int targetWidth = Math.min(440, Math.max(300, width - margin * 2));
        int targetHeight = Math.min(300, Math.max(210, height - margin * 2));

        config.gui.width = targetWidth;
        config.gui.height = targetHeight;

        int minX = width > config.gui.width + margin * 2 ? margin : 0;
        int minY = height > config.gui.height + margin * 2 ? margin : 0;
        int maxX = Math.max(minX, width - config.gui.width - minX);
        int maxY = Math.max(minY, height - config.gui.height - minY);
        config.gui.x = clamp(config.gui.x, minX, maxX);
        config.gui.y = clamp(config.gui.y, minY, maxY);
    }

    private void centerWindow() {
        config.gui.x = Math.max(0, (width - config.gui.width) / 2);
        config.gui.y = Math.max(0, (height - config.gui.height) / 2);
    }

    private static int clamp(int value, int min, int max) {
        if (max < min) {
            return min;
        }
        return Math.max(min, Math.min(max, value));
    }
}
