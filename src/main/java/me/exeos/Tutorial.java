package me.exeos;

import me.exeos.clickgui.setting.SettingsManager;
import me.exeos.module.ModuleManager;

public final class Tutorial {
    public static final Tutorial INSTANCE = new Tutorial();

    private final ModuleManager moduleManager;
    private final SettingsManager settingsManager;

    private Tutorial() {
        moduleManager = new ModuleManager();
        settingsManager = new SettingsManager();
        moduleManager.getAllModules().forEach(module -> module.getSettings().forEach(settingsManager::rSetting));
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }
}