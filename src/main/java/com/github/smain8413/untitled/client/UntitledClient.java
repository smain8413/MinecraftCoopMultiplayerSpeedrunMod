package com.github.smain8413.untitled.client;

import com.github.smain8413.untitled.serverStuff;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.core.config.Scheduled;

import java.util.Timer;
import java.util.TimerTask;

public class UntitledClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        System.out.println("I did everything right and they");
    }
}
