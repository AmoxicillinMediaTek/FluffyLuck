package me.exeos.module;

import me.exeos.clickgui.setting.Setting;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public final class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        Module autoAttack = new Module("AutoAttack", Category.Combat, GLFW.GLFW_KEY_R);
        autoAttack.addSetting(new Setting("Toggle Mobs", autoAttack, true));
        autoAttack.addSetting(new Setting("Toggle Players", autoAttack, true));
        autoAttack.addSetting(new Setting("Attack Range", autoAttack, 3, 3, 10, true));
        add(autoAttack);

        Module hitbox = new Module("Hitbox", Category.Combat, GLFW.GLFW_KEY_H);
        hitbox.addSetting(new Setting("Toggle Players", hitbox, true));
        hitbox.addSetting(new Setting("Toggle Mobs", hitbox, true));
        hitbox.addSetting(new Setting("Hitbox Size", hitbox, 1, 1, 10, true));
        add(hitbox);
    }

    private void add(Module module) {
        modules.add(module);
    }

    public List<Module> getModules(Category category) {
        return modules.stream().filter(module -> module.getCategory() == category).toList();
    }

    public List<Module> getAllModules() {
        return List.copyOf(modules);
    }
}