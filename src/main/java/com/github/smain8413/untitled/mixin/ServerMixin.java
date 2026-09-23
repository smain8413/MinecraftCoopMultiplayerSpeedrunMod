package com.github.smain8413.untitled.mixin;

import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;

import static com.github.smain8413.untitled.Untitled.LOGGER;

@Mixin(MinecraftServer.class)
public class ServerMixin {

    private static int Triggers = 0;

    @Shadow
    @Final
    private Map<RegistryKey<World>, ServerWorld> worlds;

//    @Shadow
//    private ServerResourceManager serverResourceManager;

    @Final
    @Shadow
    protected LevelStorage.Session session;

//    @Inject(method = "prepareStartRegion",  at = @At(value = "TAIL"))
//    private void method(WorldGenerationProgressListener worldGenerationProgressListener, CallbackInfo ci) {
//        ServerWorld endWorld = worlds.get(World.END);
//        ServerWorld overWorld = worlds.get(World.OVERWORLD);
//        ServerPlayerEntity player = overWorld.getRandomAlivePlayer();
//        if (player != null) {
//            player.teleport(endWorld, 0d, 0d, 0d, 0f, 0f);
//        }
//    }

    @Inject(method = "save", at = @At(value = "TAIL"))
    private void onSave(boolean bl, boolean bl2, boolean bl3, CallbackInfoReturnable<Boolean> cir){
        Triggers++;
        System.out.printf("Triggers: %d%n", Triggers);

        MinecraftServer ts = (MinecraftServer) (Object) this;
        System.out.println("world saved");
//        ServerPlayerEntity player0 = ts.getPlayerManager().getPlayerList().getFirst();

        ServerWorld endWorld = worlds.get(World.END);
        ServerWorld overWorld = worlds.get(World.OVERWORLD);
//        ServerWorld newWorld = new ServerWorld();
        ServerPlayerEntity player = overWorld.getRandomAlivePlayer();
        if (player != null) {
            player.teleport(endWorld, 0d, 0d, 0d, 0f, 0f);
        }
        if (Triggers == 1) return;

//        for(ServerWorld serverWorld : ts.getWorlds()) {
//            if (serverWorld != null && !serverWorld.getPlayers().isEmpty()) {
//                System.out.println((long) serverWorld.getPlayers().size());
//                System.out.println(serverWorld.getDimension().getClass().getName());
//                try {
//                    serverWorld.close();
//                } catch (IOException iOException) {
//                    LOGGER.error("Exception closing the level", iOException);
//                }
//            }
//        }

//        if (ts.getSnooper().isActive()) {
//        ts.getSnooper().cancel();
//    }
//        serverResourceManager.close();

        try {
            session.deleteSessionLock();
        } catch (IOException iOException2) {
            LOGGER.error("Failed to unlock level {}", session.getDirectoryName(), iOException2);
        }

        File worldDir = session.getWorldDirectory(World.OVERWORLD);
        //noinspection ResultOfMethodCallIgnored
        worldDir.delete();


//        ts.getSaveProperties().getGeneratorOptions().getSeed();
//        seed = ts.getSaveProperties().getGeneratorOptions().getSeed();
        try {
            setFinalStatic(ts.getSaveProperties().getGeneratorOptions().getClass().getField("seed"), 1L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        overWorld.getSeed();
    }

    @Unique
    private static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }
}
