package com.github.smain8413.untitled.mixin;

import net.minecraft.util.thread.MessageListener;
import net.minecraft.util.thread.ThreadExecutor;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.Executor;

@Mixin(ThreadExecutor.class)
public abstract class ThreadExecutorMixin<R extends Runnable> implements MessageListener<R>, Executor {
//    @Shadow
//    @Final
//    private static Logger LOGGER;
//
//    /**
//     * @author hi
//     * @reason because
//     */
//    @Overwrite
//    protected void executeTask(R task) {
//        try {
//            task.run();
//
//        } catch (Exception exception) {
//            LOGGER.fatal("Error executing task on {}", this.getName(), exception);
//        }
//
//    }
    @Inject(method = "executeTask", at = @At("TAIL"))
    private void onExecute(R task, CallbackInfo ci) {

    }
}
