package com.github.smain8413.untitled.mixin;

import com.github.smain8413.untitled.ServerWorldExtras;
import com.github.smain8413.untitled.utils.QueuedAction;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
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

    @Unique
    List<QueuedAction> tickQueueActions = new ArrayList<>();

    @Override
    public void untitled$runNextTick(QueuedAction action) {
        tickQueueActions.add(action);
    }

    protected ServerWorldMixin(MutableWorldProperties mutableWorldProperties, RegistryKey<World> registryKey, RegistryKey<DimensionType> registryKey2, DimensionType dimensionType, Supplier<Profiler> profiler, boolean bl, boolean bl2, long l) {
        super(mutableWorldProperties, registryKey, registryKey2, dimensionType, profiler, bl, bl2, l);
    }

//    @Unique
//    private static List<QueuedAction> tickQueueActions = new ArrayList<>();


    @Inject(method = "tick", at = @At("TAIL"))
    public void onTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci){ // maybe if you tag it onto save instead
//        tickQueueActions.parallelStream().forEach((queuedAction -> {queuedAction.ticked(); tickQueueActions.remove(queuedAction);}));
        if (tickQueueActions.isEmpty()) return;
        QueuedAction first = tickQueueActions.get(0);
        first.ticked();
        LOGGER.info("ticks: "+first.getTicksTillRun());
        if (first.getTicksTillRun() <= 0) tickQueueActions.remove(first);
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
