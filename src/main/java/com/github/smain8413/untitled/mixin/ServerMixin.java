package com.github.smain8413.untitled.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public class ServerMixin {

    @Shadow
    @Final
    private Map<RegistryKey<World>, ServerWorld> worlds;

    @Inject(method = "prepareStartRegion",  at = @At(value = "TAIL"))
    private void method(WorldGenerationProgressListener worldGenerationProgressListener, CallbackInfo ci) {

    }

    @Inject(method = "save", at = @At(value = "TAIL"))
    private void onWorldTick(boolean bl, boolean bl2, boolean bl3, CallbackInfoReturnable<Boolean> cir){
        MinecraftServer ts = (MinecraftServer) (Object) this;
        System.out.println("worldtick");
//        ServerPlayerEntity player0 = ts.getPlayerManager().getPlayerList().getFirst();

        ServerWorld endWorld = worlds.get(World.END);
        ServerWorld overWorld = worlds.get(World.OVERWORLD);
        ServerPlayerEntity player = overWorld.getRandomAlivePlayer();
        if (player != null) {
            player.teleport(endWorld, 0d, 0d, 0d, 0f, 0f);
        }
    }
}
