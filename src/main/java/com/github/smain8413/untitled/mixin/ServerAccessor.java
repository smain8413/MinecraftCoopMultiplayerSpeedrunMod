package com.github.smain8413.untitled.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(MinecraftServer.class)
public interface ServerAccessor {
    @Accessor("worlds")
    @Mutable
    Map<RegistryKey<World>, ServerWorld> getWorlds();

    @Accessor("session")
    LevelStorage.Session getSession();
}
