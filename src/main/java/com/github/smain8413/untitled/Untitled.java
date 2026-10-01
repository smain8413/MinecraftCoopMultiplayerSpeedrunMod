package com.github.smain8413.untitled;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.feature.LakeFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;
import java.util.TimerTask;

public class Untitled implements ModInitializer {

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

    }
    public void something() {
//        serverStuff.forceCloseRegionFiles(null);
//

    }

    public static final RegistryKey<World> TEMP_WORLD = RegistryKey.of(
            Registry.DIMENSION,
            new Identifier(MODID, "temporary_world")
    );

    public static final RegistryKey<DimensionType> TEMP_WORLD_TYPE = RegistryKey.of(
            Registry.DIMENSION_TYPE_KEY, new Identifier(MODID)
    );


}
