package com.github.smain8413.untitled.mixin;

import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.GeneratorOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GeneratorOptions.class)
public interface GeneratorOptionsAccessor {
    @Accessor("seed")
    @Mutable
    void setSeed(long seed);

    @Accessor("options")
    @Mutable
    void untitled$setOptions(SimpleRegistry<DimensionOptions> options);

//    @Accessor("chunkGenerat")
}
