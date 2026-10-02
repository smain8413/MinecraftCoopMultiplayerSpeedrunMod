package com.github.smain8413.untitled.mixin;

import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChunkGenerator.class)
public interface ChunkGeneratorAccessor {
    @Accessor("field_24748")
    long untitled$getSeed();

    @Accessor("field_24748")
    void untitled$setSeed(long seed);
}
