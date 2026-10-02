package com.github.smain8413.untitled.mixin;


//import com.github.smain8413.untitled.MixinUtils;
import com.github.smain8413.untitled.MixinUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {


    @Shadow protected boolean dead;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onDeath(DamageSource source, CallbackInfo ci) {
//        LivingEntity ts = (LivingEntity) (Object) this;

        // assumes standard
        if (this.getType() != EntityType.ENDER_DRAGON || this.dead || this.removed) return;

        MixinUtils.resetWorld(this.getServer(), 0);
    }

}
