package com.github.smain8413.untitled.mixin;

import com.mojang.datafixers.util.Either;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.server.world.*;
import net.minecraft.util.collection.SortedArraySet;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;

import java.util.concurrent.CompletableFuture;

@Mixin(ChunkTicketManager.class)
public abstract class ChunkTicketManagerMixin {
//    protected void purge() {
//        ++this.age;
//        ObjectIterator<Long2ObjectMap.Entry<SortedArraySet<ChunkTicket<?>>>> objectIterator = this.ticketsByPosition.long2ObjectEntrySet().fastIterator();
//
//        while(objectIterator.hasNext()) {
//            Long2ObjectMap.Entry<SortedArraySet<ChunkTicket<?>>> entry = (Long2ObjectMap.Entry)objectIterator.next();
//            if (((SortedArraySet)entry.getValue()).removeIf((chunkTicket) -> {
//                return chunkTicket.isExpired(this.age);
//            })) {
//                this.distanceFromTicketTracker.updateLevel(entry.getLongKey(), getLevel((SortedArraySet)entry.getValue()), false);
//            }
//
//            if (((SortedArraySet)entry.getValue()).isEmpty()) {
//                objectIterator.remove();
//            }
//        }
//
//    }

//    public boolean tick(ThreadedAnvilChunkStorage chunkStorage) {
//        this.distanceFromNearestPlayerTracker.updateLevels();
//        this.nearbyChunkTicketUpdater.updateLevels();
//        int i = Integer.MAX_VALUE - this.distanceFromTicketTracker.update(Integer.MAX_VALUE);
//        boolean bl = i != 0;
//        if (bl) {
//        }
//
//        if (!this.chunkHolders.isEmpty()) {
//            this.chunkHolders.forEach((chunkHolderx) -> {
//                chunkHolderx.tick(chunkStorage);
//            });
//            this.chunkHolders.clear();
//            return true;
//        } else {
//            if (!this.chunkPositions.isEmpty()) {
//                LongIterator longIterator = this.chunkPositions.iterator();
//
//                while(longIterator.hasNext()) {
//                    long l = longIterator.nextLong();
//                    if (this.getTicketSet(l).stream().anyMatch((chunkTicket) -> {
//                        return chunkTicket.getType() == ChunkTicketType.PLAYER;
//                    })) {
//                        ChunkHolder chunkHolder = chunkStorage.getCurrentChunkHolder(l);
//                        if (chunkHolder == null) {
//                            throw new IllegalStateException();
//                        }
//
//                        CompletableFuture<Either<WorldChunk, ChunkHolder.Unloaded>> completableFuture = chunkHolder.getEntityTickingFuture();
//                        completableFuture.thenAccept((either) -> {
//                            this.mainThreadExecutor.execute(() -> {
//                                this.playerTicketThrottlerSorter.send(ChunkTaskPrioritySystem.createSorterMessage(() -> {
//                                }, l, false));
//                            });
//                        });
//                    }
//                }
//
//                this.chunkPositions.clear();
//            }
//
//            return bl;
//        }
//    }

//    @Override

}
