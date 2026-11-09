package com.github.smain8413.untitled.utils;

import com.github.smain8413.untitled.Untitled;
import com.github.smain8413.untitled.WorldReset;
import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.WorldSavePath;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

public class ProgressData {
    public MinecraftServer server;
    public boolean dragonAlive;
    public int playersFinished;
    public int playersToFinish;
    public int voteFinishEarly;
    public float finishEarlyPercentage; // 0.00f-1.00f
    // player permissions pertain to voting as well as config
    protected Map<ServerPlayerEntity, ProgressDataHelper.ResetPermissions> playerPermissionsMap = new HashMap<>();

    public Map<ServerPlayerEntity, ProgressDataHelper.ResetPermissions> getPlayerPermissionsMap() {
        return playerPermissionsMap;
    }

    public void setPlayerPermissions(ServerPlayerEntity player, ProgressDataHelper.ResetPermissions permissions) {
        playerPermissionsMap.replace(player, permissions);
        if (!playerPermissionsMap.containsKey(player)) {
            playerPermissionsMap.put(player, permissions);
        }
    }

    public ProgressDataHelper.ResetPermissions getPlayerPermissions(ServerPlayerEntity player) {
        if (!playerPermissionsMap.containsKey(player)) playerPermissionsMap.put(player, ProgressDataHelper.ResetPermissions.PLAYER);
        return playerPermissionsMap.get(player);
    }

    // reset progress data on run completion
    public void reset() {
        dragonAlive = true;
        playersFinished = 0;
        voteFinishEarly = 0;
    }

    public void addVoteToFinish(ServerPlayerEntity player) {
        voteFinishEarly++;
        if ((float)Untitled.expectedPlayers / (float)voteFinishEarly >= finishEarlyPercentage) {
            finish();
        }
    }

    public void finish() {
        reset();
        WorldReset.Reset(server);
    }

    public ProgressData(MinecraftServer server) {
        this.server = server;
        if (!getProgressDataPath(server).toFile().exists()) {
            createSavePath();
            loadDefaults();
            return;
        }
        GZIPInputStream gzip = null;
        CompoundTag nbt = null;
        try {
            gzip = new GZIPInputStream(Files.newInputStream(getProgressDataPath(server)));
            nbt = NbtIo.read(new DataInputStream(gzip));
        } catch (IOException e) {
//            throw new RuntimeException(e);
            Untitled.LOGGER.fatal(e.fillInStackTrace());
            return;
        }
        setFromSaveDataNBT(nbt);
    }

    public CompoundTag getSaveDataNBT() {
        CompoundTag fileTag = new CompoundTag();
        CompoundTag tag = new CompoundTag();
        // current world
        tag.put("dragon_alive", ByteTag.of(dragonAlive));
        tag.put("players_finished", IntTag.of(playersFinished));
        // config
        tag.put("players_to_finish", IntTag.of(playersToFinish));
        tag.put("finish_early_percentage", FloatTag.of(finishEarlyPercentage));
        // player permissions
        CompoundTag playerPerms = new CompoundTag();
        for (ServerPlayerEntity player: playerPermissionsMap.keySet()) {
            playerPerms.putInt(player.getUuid().toString(), getPlayerPermissions(player).ordinal());
        }
        tag.put("player_permissions", playerPerms);
        fileTag.put("progress_data", tag);
        return fileTag;
    }

    protected void setFromSaveDataNBT(CompoundTag nbt) {
        CompoundTag pData = nbt.getCompound("progress_data");
        dragonAlive = pData.getBoolean("dragon_alive");
        playersFinished = pData.getInt("players_finished");
        playersToFinish = pData.getInt("players_to_finish");
        finishEarlyPercentage = pData.getFloat("finish_early_percentage");
        CompoundTag playerPerms = pData.getCompound("player_permissions");
        ServerPlayerEntity player;
        for (String uuid : playerPerms.getKeys()) {
            player = ProgressDataHelper.getPlayerFromUUID(uuid);
            if (player == null) continue;
            playerPermissionsMap.put(player, ProgressDataHelper.ResetPermissions.values()[playerPerms.getInt(uuid)]);
        }
    }

    protected void loadDefaults() {
        reset();
        playersToFinish = Untitled.expectedPlayers;
        finishEarlyPercentage = 1.00f; // 0 - 1
    }

    public static Path getProgressDataPath(MinecraftServer server) {
        String rootPath = server.getSavePath(WorldSavePath.ROOT).toString();
        rootPath = rootPath.substring(0, rootPath.lastIndexOf("\\"));
        Path path = Paths.get(rootPath);
        return path.resolve(Untitled.MODID).resolve(Untitled.MODID + ".dat");
    }

    private void createSavePath() {
        String rootPath = server.getSavePath(WorldSavePath.ROOT).toString();
        rootPath = rootPath.substring(0, rootPath.lastIndexOf("\\"));
        Path path = Paths.get(rootPath);
        try {
            Files.createDirectory(path.resolve(Untitled.MODID));
            Files.createFile(path.resolve(Untitled.MODID).resolve(Untitled.MODID + ".dat"));
        } catch (IOException e) {
            Untitled.LOGGER.fatal(e.fillInStackTrace());
        }
    }

    public static class ProgressDataHelper {
        public enum ResetPermissions {
            PLAYER,
            ADMIN,
            OWNER
        }

        public static ServerPlayerEntity getPlayerFromUUID(String uuid) {
            return Untitled.SERVER.getPlayerManager().getPlayer(UUID.fromString(uuid));
        }

    }
}
