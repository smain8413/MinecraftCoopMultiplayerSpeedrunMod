package com.github.smain8413.untitled.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.*;
import org.apache.logging.log4j.core.config.Scheduled;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


@Mixin(targets = {"net.minecraft.client.network.ClientPlayNetworkHandler"})
public class OnWorldJoinMixin {
    @Shadow
    private ClientWorld world;

    @Shadow
    private int chunkLoadDistance;

    @Shadow
    private MinecraftClient client;

    //    @WrapMethod(method = "onGameJoin", at = @At(value = "TAIL", target = "net/minecraft/client/network/ClientPlayNetworkHandler"))
    @Inject(method = "onGameJoin", at = @At(value = "TAIL"))
    private void injected(GameJoinS2CPacket packet, CallbackInfo info) {
        ClientPlayNetworkHandler thisObject = (ClientPlayNetworkHandler) (Object)this;
        System.out.println("game joined");
        ClientWorld oldClientWorld = world;
        world = null;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run(){
                world = oldClientWorld;
            }}, 3000);

    }

//    @Unique
//    private ClientWorld generateClientWorld(ClientPlayNetworkHandler thisObject, GameJoinS2CPacket packet) {
//        return new ClientWorld(thisObject, new ClientWorld.Properties(Difficulty.HARD, true, true), packet.getDimensionId(), DimensionType.THE_NETHER_REGISTRY_KEY, DimensionType.getOverworldDimensionType(), chunkLoadDistance, client::getProfiler, client.worldRenderer, packet.isDebugWorld(), packet.getSha256Seed() );
//    }

}
