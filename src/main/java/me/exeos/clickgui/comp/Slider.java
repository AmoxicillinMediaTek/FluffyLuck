package me.exeos.clickgui.comp;

import me.exeos.clickgui.Clickgui;
import me.exeos.clickgui.setting.Setting;
import me.exeos.module.Module;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public final class Slider extends Comp {
    private static final double TRACK_WIDTH = 90;
    private boolean dragging;

    public Slider(double x, double y, Clickgui parent, Module module, Setting setting) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.module = module;
        this.setting = setting;
    }

    @Override
    public void mouseClicked(Click click, boolean doubled) {
        if (click.button() == 0 && isInside(click.x(), click.y(), parent.posX + x - 70, parent.posY + y + 10, parent.posX + x + 20, parent.posY + y + 20)) {
            dragging = true;
            updateValue(click.x());
        }
    }

    @Override
    public void mouseReleased(Click click) {
        dragging = false;
    }

    @Override
    public void drawScreen(DrawContext context, int mouseX, int mouseY) {
        if (dragging) {
            updateValue(mouseX);
        }
        double range = setting.getMax() - setting.getMin();
        double progress = range <= 0 ? 0 : (setting.getValDouble() - setting.getMin()) / range;
        int left = (int) parent.posX + (int) x - 70;
        int top = (int) parent.posY + (int) y;
        context.fill(left, top + 10, left + 90, top + 20, 0xff8f068f);
        context.fill(left, top + 10, left + (int) (TRACK_WIDTH * Math.max(0, Math.min(1, progress))), top + 20, 0xffe60ae6);
        context.drawText(parent.getTextRenderer(), Text.literal(setting.getName() + ": " + formatValue(setting.getValDouble())), left, top, 0xffffffff, false);
    }

    private void updateValue(double mouseX) {
        double fraction = Math.max(0, Math.min(1, (mouseX - (parent.posX + x - 70)) / TRACK_WIDTH));
        double value = setting.getMin() + fraction * (setting.getMax() - setting.getMin());
        setting.setValDouble(value);
    }

    private String formatValue(double value) {
        return setting.onlyInt() ? Integer.toString((int) value) : String.format(java.util.Locale.ROOT, "%.1f", value);
    }
}