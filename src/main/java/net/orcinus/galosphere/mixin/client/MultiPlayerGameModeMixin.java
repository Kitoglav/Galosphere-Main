package net.orcinus.galosphere.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.player.Player;
import net.orcinus.galosphere.api.Spectatable;
import net.orcinus.galosphere.api.SpectreBoundSpyglass;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @Shadow @Final private Minecraft minecraft;

    @Inject(at = @At("HEAD"), method = "hasExperience", cancellable = true)
    private void GE$hasExperience(CallbackInfoReturnable<Boolean> cir) {
        if (this.minecraft.getCameraEntity() instanceof Spectatable spectatable && spectatable.getManipulatorUUID() != null && this.minecraft.level != null) {
            Player player = this.minecraft.level.getPlayerByUUID(spectatable.getManipulatorUUID());
            if (player == this.minecraft.player) {
                cir.setReturnValue(false);
            }
        }
        Optional.ofNullable(this.minecraft.player).filter(SpectreBoundSpyglass.class::isInstance).map(SpectreBoundSpyglass.class::cast).filter(SpectreBoundSpyglass::isUsingSpectreBoundedSpyglass).ifPresent(localPlayer -> cir.setReturnValue(false));
    }

}
