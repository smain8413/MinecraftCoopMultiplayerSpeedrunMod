package com.github.smain8413.untitled.mixin;


//import com.github.smain8413.untitled.MixinUtils;
import com.github.smain8413.untitled.MixinUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements ScreenHandlerListener {

    @Shadow @Final public MinecraftServer server;

//    @Shadow public abstract void teleport(ServerWorld targetWorld, double x, double y, double z, float yaw, float pitch);

    public ServerPlayerEntityMixin(World world, BlockPos blockPos, GameProfile gameProfile) {
        super(world, blockPos, gameProfile);
    }

    @Override
    public boolean startRiding(Entity entity, boolean force){
//        try {
//            ServerWorld tempWorld = MixinUtils.temporaryWorld(this.server);
//            teleport(tempWorld, 1000000F, 255F, 10000000F, 0f, 0f);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            LOGGER.error(e.toString());
//        }
        LOGGER.info("resetting world");
        MixinUtils.resetWorld(server, new Random().nextLong());
        return false;
    }
}
