package me.exeos.clickgui;

import me.exeos.Tutorial;
import me.exeos.clickgui.comp.CheckBox;
import me.exeos.clickgui.comp.Combo;
import me.exeos.clickgui.comp.Comp;
import me.exeos.clickgui.comp.Slider;
import me.exeos.clickgui.setting.Setting;
import me.exeos.module.Category;
import me.exeos.module.Module;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;

import java.util.ArrayList;

public final class Clickgui extends Screen {
    public double posX, posY, panelWidth, panelHeight, dragX, dragY;
    public boolean dragging;
    public Category selectedCategory;
    private Module selectedModule;
    public final ArrayList<Comp> comps = new ArrayList<>();

    public Clickgui() {
        super(Text.literal("FluffyLuck ClickGUI"));
        selectedCategory = Category.Combat;
    }

    @Override
    protected void init() {
        super.init();
        posX = this.width / 2.0 - 150;
        posY = this.height / 2.0 - 100;
        panelWidth = posX + 300;
        panelHeight = posY + 200;
        dragging = false;
    }

    @Override
    protected void applyBlur(DrawContext context) {
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        renderBackground(context, mouseX, mouseY, deltaTicks);
        if (dragging) {
            posX = mouseX - dragX;
            posY = mouseY - dragY;
        }
        panelWidth = posX + 300;
        panelHeight = posY + 200;
        context.fill((int) posX, (int) posY - 10, (int) panelWidth, (int) posY, 0xff640a64);
        context.fill((int) posX, (int) posY, (int) panelWidth, (int) panelHeight, 0xff2d2d2d);

        int offset = 0;
        for (Category category : Category.values()) {
            context.fill((int) posX, (int) posY + 1 + offset, (int) posX + 60, (int) posY + 15 + offset,
                    category == selectedCategory ? 0xffe60ae6 : 0xff1c1c1c);
            context.drawText(textRenderer, Text.literal(category.name()), (int) posX + 2, (int) posY + 5 + offset, 0xffaaaaaa, false);
            offset += 15;
        }

        offset = 0;
        for (Module module : Tutorial.INSTANCE.getModuleManager().getModules(selectedCategory)) {
            context.fill((int) posX + 65, (int) posY + 1 + offset, (int) posX + 125, (int) posY + 15 + offset,
                    module.isToggled() ? 0xffe60ae6 : 0xff1c1c1c);
            context.drawText(textRenderer, Text.literal(module.getName()), (int) posX + 67, (int) posY + 5 + offset, 0xffaaaaaa, false);
            offset += 15;
        }

        for (Comp comp : comps) {
            comp.drawScreen(context, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (click.button() == 0 && isInside(click.x(), click.y(), posX, posY - 10, panelWidth, posY)) {
            dragging = true;
            dragX = click.x() - posX;
            dragY = click.y() - posY;
        }

        int offset = 0;
        for (Category category : Category.values()) {
            if (click.button() == 0 && isInside(click.x(), click.y(), posX, posY + 1 + offset, posX + 60, posY + 15 + offset)) {
                selectedCategory = category;
                comps.clear();
            }
            offset += 15;
        }

        offset = 0;
        for (Module module : Tutorial.INSTANCE.getModuleManager().getModules(selectedCategory)) {
            if (isInside(click.x(), click.y(), posX + 65, posY + 1 + offset, posX + 125, posY + 15 + offset)) {
                if (click.button() == 0) {
                    module.toggle();
                } else if (click.button() == 1) {
                    if (selectedModule == module) {
                        selectedModule = null;
                        comps.clear();
                    } else {
                        selectedModule = module;
                        buildComponents(module);
                    }
                }
            }
            offset += 15;
        }

        for (Comp comp : comps) {
            comp.mouseClicked(click, doubled);
        }
        return true;
    }

    @Override
    public boolean mouseReleased(Click click) {
        dragging = false;
        for (Comp comp : comps) {
            comp.mouseReleased(click);
        }
        return true;
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        for (Comp comp : comps) {
            comp.keyPressed(input);
        }
        return super.keyPressed(input);
    }

    private void buildComponents(Module module) {
        comps.clear();
        int settingOffset = 3;
        for (Setting setting : Tutorial.INSTANCE.getSettingsManager().getSettingsByMod(module)) {
            if (setting.isCombo()) {
                comps.add(new Combo(275, settingOffset, this, selectedModule, setting));
                settingOffset += 15;
            } else if (setting.isCheck()) {
                comps.add(new CheckBox(275, settingOffset, this, selectedModule, setting));
                settingOffset += 15;
            } else if (setting.isSlider()) {
                comps.add(new Slider(275, settingOffset, this, selectedModule, setting));
                settingOffset += 25;
            }
        }
    }

    private boolean isInside(double mouseX, double mouseY, double left, double top, double right, double bottom) {
        return mouseX > left && mouseX < right && mouseY > top && mouseY < bottom;
    }
}