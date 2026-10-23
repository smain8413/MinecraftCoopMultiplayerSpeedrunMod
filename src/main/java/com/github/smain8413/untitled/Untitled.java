package com.github.smain8413.untitled;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.*;

public class Untitled implements ModInitializer {


    public static WatchService WATCHER;
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "coop_mod";
    @Override
    public void onInitialize() {
        System.out.println("what");
//        Timer timer = new Timer();
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run(){
//                something();
//            }
//        };

//        EntityAttrib
//        LakeFeature.
//        new Thread(() -> {
//            try {
//                watchService(Paths.get("/Users/smain8413/IdeaProjects/untitled/run/saves/New World/level.dat"));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }));
//        ExecutorService executorService  = Executors.newSingleThreadScheduledExecutor();
        try {
            WATCHER = watchService(Paths.get("/Users/smain8413/IdeaProjects/untitled/run/saves/New World"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static WatchService watchService(Path path) throws IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE);
        return watchService;
    }

    public static final RegistryKey<World> TEMP_WORLD = RegistryKey.of(
            Registry.DIMENSION,
            new Identifier(MODID, "temporary_world")
    );

    public static final RegistryKey<DimensionType> TEMP_WORLD_TYPE = RegistryKey.of(
            Registry.DIMENSION_TYPE_KEY, new Identifier(MODID)
    );


}
