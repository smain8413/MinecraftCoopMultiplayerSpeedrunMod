package com.github.smain8413.untitled.mixin;

import com.github.smain8413.untitled.Untitled;
import net.minecraft.util.thread.MessageListener;
import net.minecraft.util.thread.ThreadExecutor;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.LambdaUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.sql.Time;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Mixin(ThreadExecutor.class)
public abstract class ThreadExecutorMixin<R extends Runnable> implements MessageListener<R>, Executor {
    @Shadow
    @Final
    private static Logger LOGGER;

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

    @Shadow
    protected abstract Thread getThread();

    // playing with fire (yay)
    @Inject(method = "executeTask", at = @At("TAIL"))
    private void onExecute(R task, CallbackInfo ci) {
//        WatchKey key = Untitled.WATCHER.poll();
//        System.out.println("uhhhhhhhh");
        WatchKey key;
        try {
            while ((key = Untitled.WATCHER.poll(10, TimeUnit.MICROSECONDS)) != null) {
                System.out.println(Arrays.toString(getThread().getStackTrace()));

//                System.out.println(task.getClass().getClass());
                System.out.println(task + ": ");

//                if(task instanceof Lambda)
                for (WatchEvent<?> event : key.pollEvents()) {
                    //process
                    System.out.println(
                            "Event kind:" + event.kind()
                                    + ". File affected: " + event.context() + ".");
                }
                key.reset();
            }
        } catch (Exception e) {
            LOGGER.fatal("very bad (ThreadExecutorMixin 1)");
        }
    }
}
