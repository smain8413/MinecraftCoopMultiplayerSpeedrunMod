package com.github.smain8413.untitled.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Npc;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.GameRules;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.apache.http.annotation.Immutable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements ServerWorldAccess, ServerWorldExtras {
//    @Shadow
//    public abstract void tick(BooleanSupplier shouldKeepTicking);

    protected ServerWorldMixin(MutableWorldProperties mutableWorldProperties, RegistryKey<World> registryKey, RegistryKey<DimensionType> registryKey2, DimensionType dimensionType, Supplier<Profiler> profiler, boolean bl, boolean bl2, long l) {
        super(mutableWorldProperties, registryKey, registryKey2, dimensionType, profiler, bl, bl2, l);
    }

//    @Unique
//    private static List<QueuedAction> tickQueueActions = new ArrayList<>();


    @Inject(method = "tick", at = @At("TAIL"))
    public void onTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci){
        tickQueueActions.parallelStream().forEach((queuedAction -> {queuedAction.ticked(); tickQueueActions.remove(queuedAction);}));
    }

//    @Unique
//    private static void runNextTick(QueuedAction action) {
//        tickQueueActions.add(action);
//    }
//

//    public static void runNextTick(QueuedAction action) {
//        tickQueueActions.add(action);
//    }


}
