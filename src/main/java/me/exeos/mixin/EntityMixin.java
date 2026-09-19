package me.exeos.mixin;

import me.exeos.Tutorial;
import me.exeos.clickgui.setting.Setting;
import me.exeos.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "getBoundingBox", at = @At("RETURN"), cancellable = true)
    private void expandHitbox(CallbackInfoReturnable<Box> cir) {
        if (!((Object) this instanceof LivingEntity livingEntity)) {
            return;
        }

        Module hitbox = Tutorial.INSTANCE.getModuleManager().getAllModules().stream()
                .filter(module -> module.getName().equals("Hitbox"))
                .findFirst()
                .orElse(null);
        if (hitbox == null || !hitbox.isToggled()) {
            return;
        }

        boolean player = livingEntity instanceof PlayerEntity;
        boolean mob = livingEntity instanceof MobEntity;
        Setting players = hitbox.getSettings().get(0);
        Setting mobs = hitbox.getSettings().get(1);
        if ((!player || !players.getValBoolean()) && (!mob || !mobs.getValBoolean())) {
            return;
        }

        double expansion = hitbox.getSettings().get(2).getValDouble() / 10.0D;
        cir.setReturnValue(cir.getReturnValue().expand(expansion));
    }
}