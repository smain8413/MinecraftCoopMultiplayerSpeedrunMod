package com.github.smain8413.untitled.mixin;


import com.github.smain8413.untitled.utils.ProgressData;
import com.github.smain8413.untitled.utils.ServerExtras;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Shadow @Final private MinecraftServer server;

    @Shadow @Final private static Logger LOGGER;

    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    private void playerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        System.out.println("hi");
        ServerExtras serverE = (ServerExtras) server;
        if (!serverE.minecraftCoopMultiplayerSpeedrunMod$getProgressData().getPlayerPermissionsMap().containsKey(player)) {
            ProgressData.ProgressDataHelper.ResetPermissions playerPerms = ProgressData.ProgressDataHelper.ResetPermissions.PLAYER;
            playerPerms = server.getPlayerManager().getOpList().isOp(player.getGameProfile()) ? ProgressData.ProgressDataHelper.ResetPermissions.ADMIN : playerPerms;
            playerPerms = player.hasPermissionLevel(4) ? ProgressData.ProgressDataHelper.ResetPermissions.OWNER : playerPerms;
            serverE.minecraftCoopMultiplayerSpeedrunMod$getProgressData().setPlayerPermissions(player, playerPerms);
        }
    }
}
