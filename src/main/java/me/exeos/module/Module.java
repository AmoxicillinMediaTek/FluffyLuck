package me.exeos.module;

import me.exeos.clickgui.setting.Setting;

import java.util.ArrayList;
import java.util.List;

public final class Module {
    private final String name;
    private final Category category;
    private final int keyCode;
    private final List<Setting> settings = new ArrayList<>();
    private boolean toggled;

    public Module(String name, Category category, int keyCode) {
        this.name = name;
        this.category = category;
        this.keyCode = keyCode;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void toggle() {
        toggled = !toggled;
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public void addSetting(Setting setting) {
        settings.add(setting);
    }
}