package me.exeos.clickgui.comp;

import me.exeos.clickgui.Clickgui;
import me.exeos.clickgui.setting.Setting;
import me.exeos.module.Module;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public final class Combo extends Comp {
    public Combo(double x, double y, Clickgui parent, Module module, Setting setting) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.module = module;
        this.setting = setting;
    }

    @Override
    public void mouseClicked(Click click, boolean doubled) {
        if (click.button() != 0 || !isInside(click.x(), click.y(), parent.posX + x - 70, parent.posY + y, parent.posX + x, parent.posY + y + 10)) {
            return;
        }
        int current = setting.getOptions().indexOf(setting.getValString());
        int next = current < 0 || current + 1 >= setting.getOptions().size() ? 0 : current + 1;
        if (!setting.getOptions().isEmpty()) {
            setting.setValString(setting.getOptions().get(next));
        }
    }

    @Override
    public void drawScreen(DrawContext context, int mouseX, int mouseY) {
        int left = (int) parent.posX + (int) x - 70;
        int top = (int) parent.posY + (int) y;
        context.fill(left, top, left + 70, top + 10, 0xff1e1e1e);
        context.drawText(parent.getTextRenderer(), Text.literal(setting.getName() + ": " + setting.getValString()), left + 1, top + 1, 0xffc8c8c8, false);
    }
}