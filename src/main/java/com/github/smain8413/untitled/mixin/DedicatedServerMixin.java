package com.github.smain8413.untitled.mixin;


import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftDedicatedServer.class)
public class DedicatedServerMixin {

    @Inject(method = "setupServer", at = @At(value = "TAIL"))
    private void setupServer(CallbackInfoReturnable<Boolean> cir) {

    }
}
