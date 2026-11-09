package com.github.smain8413.untitled.mixin;

import com.github.smain8413.untitled.Untitled;
import com.github.smain8413.untitled.utils.ProgressData;
import com.github.smain8413.untitled.utils.ServerExtras;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.snooper.SnooperListener;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

import net.minecraft.nbt.NbtIo;

@Mixin(MinecraftServer.class)
public abstract class ServerMixin extends ReentrantThreadExecutor<ServerTask> implements SnooperListener, CommandOutput, AutoCloseable, ServerExtras {

    @Shadow @Nullable public abstract ServerWorld getWorld(RegistryKey<World> key);

    @Shadow @Final protected SaveProperties saveProperties;

    @Shadow public abstract Path getSavePath(WorldSavePath worldSavePath);

    @Unique
    protected ProgressData progressData;

    public ServerMixin(String string) {
        super(string);
    }

    @Inject(method = "startServer", at = @At("TAIL"))
    private static <S extends MinecraftServer> void onStartServer(Function<Thread, S> serverFactory, CallbackInfoReturnable<S> cir) {
        Untitled.SERVER = cir.getReturnValue();
        ((ServerExtras)Untitled.SERVER).minecraftCoopMultiplayerSpeedrunMod$setProgressData(new ProgressData(Untitled.SERVER));
    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    private void onShutdown(CallbackInfo ci) {
        Untitled.SERVER = null;
    }

    @Inject(method = "save", at = @At("TAIL"))
    private void onSave(boolean bl, boolean bl2, boolean bl3, CallbackInfoReturnable<Boolean> cir) {
        Path savePath = ProgressData.getProgressDataPath(((MinecraftServer)(Object)this));
        try {
            //noinspection ResultOfMethodCallIgnored
            savePath.toFile().createNewFile();
        } catch (IOException e) {
            Untitled.LOGGER.fatal(e.fillInStackTrace());
        }
        try {
            NbtIo.writeCompressed(progressData.getSaveDataNBT(), Files.newOutputStream(savePath));
        } catch (IOException e) {
            Untitled.LOGGER.fatal(e.fillInStackTrace());
        }
    }

    @Unique
    public ProgressData minecraftCoopMultiplayerSpeedrunMod$getProgressData() {
        return progressData;
    }

    @Unique
    public void minecraftCoopMultiplayerSpeedrunMod$setProgressData(ProgressData data) {
        progressData = data;
    }


}
