package me.exeos.clickgui.setting;

import me.exeos.module.Module;

import java.util.ArrayList;

public final class SettingsManager {
    private final ArrayList<Setting> settings = new ArrayList<>();

    public void rSetting(Setting setting) {
        settings.add(setting);
    }

    public ArrayList<Setting> getSettings() {
        return settings;
    }

    public ArrayList<Setting> getSettingsByMod(Module module) {
        ArrayList<Setting> result = new ArrayList<>();
        for (Setting setting : settings) {
            if (setting.getParentMod().equals(module)) {
                result.add(setting);
            }
        }
        return result;
    }

    public Setting getSettingByName(String name) {
        for (Setting setting : settings) {
            if (setting.getName().equalsIgnoreCase(name)) {
                return setting;
            }
        }
        return null;
    }
}