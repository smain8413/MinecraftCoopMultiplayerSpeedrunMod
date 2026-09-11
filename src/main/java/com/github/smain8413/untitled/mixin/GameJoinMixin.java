package com.github.smain8413.untitled.mixin;

//import net.minecraft.client.network.ClientPlayNetworkHandler

import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(targets = "net.minecraft.client.network.ClientPlayNetworkHandler")
public class GameJoinMixin implements ClientPlayPacketListener {


//    @WrapMethod(method = "onGameJoin", at = @At(value = "TAIL", target = "net/minecraft/client/network/ClientPlayNetworkHandler"))
    @Inject(method = "onGameJoin", at = @At("TAIL"))
    private void injected(GameJoinS2CPacket packet, CallbackInfo info) {
        System.out.println("hame goined");
        System.exit(0);
    }

    //region overrides
    @Override
    public void onEntitySpawn(EntitySpawnS2CPacket packet) {

    }

    @Override
    public void onExperienceOrbSpawn(ExperienceOrbSpawnS2CPacket packet) {

    }

    @Override
    public void onMobSpawn(MobSpawnS2CPacket packet) {

    }

    @Override
    public void onScoreboardObjectiveUpdate(ScoreboardObjectiveUpdateS2CPacket packet) {

    }

    @Override
    public void onPaintingSpawn(PaintingSpawnS2CPacket packet) {

    }

    @Override
    public void onPlayerSpawn(PlayerSpawnS2CPacket packet) {

    }

    @Override
    public void onEntityAnimation(EntityAnimationS2CPacket packet) {

    }

    @Override
    public void onStatistics(StatisticsS2CPacket packet) {

    }

    @Override
    public void onUnlockRecipes(UnlockRecipesS2CPacket packet) {

    }

    @Override
    public void onBlockDestroyProgress(BlockBreakingProgressS2CPacket packet) {

    }

    @Override
    public void onSignEditorOpen(SignEditorOpenS2CPacket packet) {

    }

    @Override
    public void onBlockEntityUpdate(BlockEntityUpdateS2CPacket packet) {

    }

    @Override
    public void onBlockEvent(BlockEventS2CPacket packet) {

    }

    @Override
    public void onBlockUpdate(BlockUpdateS2CPacket packet) {

    }

    @Override
    public void onGameMessage(GameMessageS2CPacket packet) {

    }

    @Override
    public void onChunkDeltaUpdate(ChunkDeltaUpdateS2CPacket packet) {

    }

    @Override
    public void onMapUpdate(MapUpdateS2CPacket packet) {

    }

    @Override
    public void onGuiActionConfirm(ConfirmGuiActionS2CPacket packet) {

    }

    @Override
    public void onCloseScreen(CloseScreenS2CPacket packet) {

    }

    @Override
    public void onInventory(InventoryS2CPacket packet) {

    }

    @Override
    public void onOpenHorseScreen(OpenHorseScreenS2CPacket packet) {

    }

    @Override
    public void onScreenHandlerPropertyUpdate(ScreenHandlerPropertyUpdateS2CPacket packet) {

    }

    @Override
    public void onScreenHandlerSlotUpdate(ScreenHandlerSlotUpdateS2CPacket packet) {

    }

    @Override
    public void onCustomPayload(CustomPayloadS2CPacket packet) {

    }

    @Override
    public void onDisconnect(DisconnectS2CPacket packet) {

    }

    @Override
    public void onEntityStatus(EntityStatusS2CPacket packet) {

    }

    @Override
    public void onEntityAttach(EntityAttachS2CPacket packet) {

    }

    @Override
    public void onEntityPassengersSet(EntityPassengersSetS2CPacket packet) {

    }

    @Override
    public void onExplosion(ExplosionS2CPacket packet) {

    }

    @Override
    public void onGameStateChange(GameStateChangeS2CPacket packet) {

    }

    @Override
    public void onKeepAlive(KeepAliveS2CPacket packet) {

    }

    @Override
    public void onChunkData(ChunkDataS2CPacket packet) {

    }

    @Override
    public void onUnloadChunk(UnloadChunkS2CPacket packet) {

    }

    @Override
    public void onWorldEvent(WorldEventS2CPacket packet) {

    }

    @Override
    public void onGameJoin(GameJoinS2CPacket packet) {
//        super(packet);
    }

    @Override
    public void onEntityUpdate(EntityS2CPacket packet) {

    }

    @Override
    public void onPlayerPositionLook(PlayerPositionLookS2CPacket packet) {

    }

    @Override
    public void onParticle(ParticleS2CPacket packet) {

    }

    @Override
    public void onPlayerAbilities(PlayerAbilitiesS2CPacket packet) {

    }

    @Override
    public void onPlayerList(PlayerListS2CPacket packet) {

    }

    @Override
    public void onEntitiesDestroy(EntitiesDestroyS2CPacket packet) {

    }

    @Override
    public void onRemoveEntityEffect(RemoveEntityStatusEffectS2CPacket packet) {

    }

    @Override
    public void onPlayerRespawn(PlayerRespawnS2CPacket packet) {

    }

    @Override
    public void onEntitySetHeadYaw(EntitySetHeadYawS2CPacket packet) {

    }

    @Override
    public void onHeldItemChange(HeldItemChangeS2CPacket packet) {

    }

    @Override
    public void onScoreboardDisplay(ScoreboardDisplayS2CPacket packet) {

    }

    @Override
    public void onEntityTrackerUpdate(EntityTrackerUpdateS2CPacket packet) {

    }

    @Override
    public void onVelocityUpdate(EntityVelocityUpdateS2CPacket packet) {

    }

    @Override
    public void onEquipmentUpdate(EntityEquipmentUpdateS2CPacket packet) {

    }

    @Override
    public void onExperienceBarUpdate(ExperienceBarUpdateS2CPacket packet) {

    }

    @Override
    public void onHealthUpdate(HealthUpdateS2CPacket packet) {

    }

    @Override
    public void onTeam(TeamS2CPacket packet) {

    }

    @Override
    public void onScoreboardPlayerUpdate(ScoreboardPlayerUpdateS2CPacket packet) {

    }

    @Override
    public void onPlayerSpawnPosition(PlayerSpawnPositionS2CPacket packet) {

    }

    @Override
    public void onWorldTimeUpdate(WorldTimeUpdateS2CPacket packet) {

    }

    @Override
    public void onPlaySound(PlaySoundS2CPacket packet) {

    }

    @Override
    public void onPlaySoundFromEntity(PlaySoundFromEntityS2CPacket packet) {

    }

    @Override
    public void onPlaySoundId(PlaySoundIdS2CPacket packet) {

    }

    @Override
    public void onItemPickupAnimation(ItemPickupAnimationS2CPacket packet) {

    }

    @Override
    public void onEntityPosition(EntityPositionS2CPacket packet) {

    }

    @Override
    public void onEntityAttributes(EntityAttributesS2CPacket packet) {

    }

    @Override
    public void onEntityPotionEffect(EntityStatusEffectS2CPacket packet) {

    }

    @Override
    public void onSynchronizeTags(SynchronizeTagsS2CPacket packet) {

    }

    @Override
    public void onCombatEvent(CombatEventS2CPacket packet) {

    }

    @Override
    public void onDifficulty(DifficultyS2CPacket packet) {

    }

    @Override
    public void onSetCameraEntity(SetCameraEntityS2CPacket packet) {

    }

    @Override
    public void onWorldBorder(WorldBorderS2CPacket packet) {

    }

    @Override
    public void onTitle(TitleS2CPacket packet) {

    }

    @Override
    public void onPlayerListHeader(PlayerListHeaderS2CPacket packet) {

    }

    @Override
    public void onResourcePackSend(ResourcePackSendS2CPacket packet) {

    }

    @Override
    public void onBossBar(BossBarS2CPacket packet) {

    }

    @Override
    public void onCooldownUpdate(CooldownUpdateS2CPacket packet) {

    }

    @Override
    public void onVehicleMove(VehicleMoveS2CPacket packet) {

    }

    @Override
    public void onAdvancements(AdvancementUpdateS2CPacket packet) {

    }

    @Override
    public void onSelectAdvancementTab(SelectAdvancementTabS2CPacket packet) {

    }

    @Override
    public void onCraftFailedResponse(CraftFailedResponseS2CPacket packet) {

    }

    @Override
    public void onCommandTree(CommandTreeS2CPacket packet) {

    }

    @Override
    public void onStopSound(StopSoundS2CPacket packet) {

    }

    @Override
    public void onCommandSuggestions(CommandSuggestionsS2CPacket packet) {

    }

    @Override
    public void onSynchronizeRecipes(SynchronizeRecipesS2CPacket packet) {

    }

    @Override
    public void onLookAt(LookAtS2CPacket packet) {

    }

    @Override
    public void onTagQuery(TagQueryResponseS2CPacket packet) {

    }

    @Override
    public void onLightUpdate(LightUpdateS2CPacket packet) {

    }

    @Override
    public void onOpenWrittenBook(OpenWrittenBookS2CPacket packet) {

    }

    @Override
    public void onOpenScreen(OpenScreenS2CPacket packet) {

    }

    @Override
    public void onSetTradeOffers(SetTradeOffersS2CPacket packet) {

    }

    @Override
    public void onChunkLoadDistance(ChunkLoadDistanceS2CPacket packet) {

    }

    @Override
    public void onChunkRenderDistanceCenter(ChunkRenderDistanceCenterS2CPacket packet) {

    }

    @Override
    public void onPlayerActionResponse(PlayerActionResponseS2CPacket packet) {

    }

    @Override
    public void onDisconnected(Text reason) {

    }

    @Override
    public ClientConnection getConnection() {
        return null;
    }
    //endregion
}
