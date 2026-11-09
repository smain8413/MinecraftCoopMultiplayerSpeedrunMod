package com.github.smain8413.untitled;

import com.github.smain8413.untitled.utils.ServerWorldExtras;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class Untitled implements ModInitializer {


//    public static WatchService WATCHER;
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "coop_mod";
    public static MinecraftServer SERVER;
    public static int expectedPlayers = 1;

    @Override
    public void onInitialize() {
        System.out.println("what");

    }

    public static final RegistryKey<World> TEMP_WORLD = RegistryKey.of(
            Registry.DIMENSION,
            new Identifier(MODID, "temporary_world")
    );

    public static final RegistryKey<DimensionType> TEMP_WORLD_TYPE = RegistryKey.of(
            Registry.DIMENSION_TYPE_KEY, new Identifier(MODID)
    );

}

