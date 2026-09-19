package me.exeos;

import me.exeos.clickgui.Clickgui;
import me.exeos.clickgui.setting.Setting;
import me.exeos.module.Category;
import me.exeos.module.Module;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public final class ClickGuiModClient implements ClientModInitializer {
    private static final KeyBinding.Category MODULE_CATEGORY = KeyBinding.Category.create(Identifier.of("clickgui", "modules"));
    private static final Map<Module, KeyBinding> MODULE_KEYBINDS = new HashMap<>();
    private static final KeyBinding OPEN_GUI = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.clickgui.open",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            KeyBinding.Category.create(Identifier.of("clickgui", "controls"))
    ));

    @Override
    public void onInitializeClient() {
        for (Module module : Tutorial.INSTANCE.getModuleManager().getAllModules()) {
            KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key.clickgui.module." + module.getName().toLowerCase(java.util.Locale.ROOT),
                    InputUtil.Type.KEYSYM,
                    module.getKeyCode(),
                    MODULE_CATEGORY
            ));
            MODULE_KEYBINDS.put(module, keyBinding);
        }

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (OPEN_GUI.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new Clickgui());
                } else if (client.currentScreen instanceof Clickgui) {
                    client.setScreen(null);
                }
            }

            MODULE_KEYBINDS.forEach((module, keyBinding) -> {
                while (keyBinding.wasPressed()) {
                    module.toggle();
                }
            });

            Module autoAttack = Tutorial.INSTANCE.getModuleManager().getModules(Category.Combat).get(0);
            if (!autoAttack.isToggled() || client.player == null || client.world == null || client.interactionManager == null) {
                return;
            }

            Setting mobs = autoAttack.getSettings().get(0);
            Setting players = autoAttack.getSettings().get(1);
            Setting range = autoAttack.getSettings().get(2);
            double attackRange = range.getValDouble();

            Entity target = client.crosshairTarget instanceof EntityHitResult entityHitResult
                    ? entityHitResult.getEntity()
                    : null;

            boolean validTarget = target instanceof LivingEntity livingEntity
                    && livingEntity.isAlive()
                    && livingEntity.isAttackable()
                    && ((mobs.getValBoolean() && livingEntity instanceof MobEntity)
                    || (players.getValBoolean() && livingEntity instanceof PlayerEntity))
                    && client.player.squaredDistanceTo(livingEntity) <= attackRange * attackRange;

            if (validTarget && client.player.getAttackCooldownProgress(0.0f) >= 1.0f) {
                client.interactionManager.attackEntity(client.player, target);
            }
        });
    }
}