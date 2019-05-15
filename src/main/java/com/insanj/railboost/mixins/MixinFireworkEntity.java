package com.insanj.railboost.mixins;

import java.util.function.Predicate;
import java.lang.reflect.Field;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.FireworkEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.FireworkItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(FireworkEntity.class)
public class MixinFireworkEntity {
    @Inject(method = "<init>*", at = @At(value="RETURN"))
    private void onFireworkInit(CallbackInfo ci) {
        FireworkEntity firework = (FireworkEntity)(Object)this;
        if (firework.getEntityWorld() instanceof ServerWorld) {
            ServerWorld world = (ServerWorld)firework.getEntityWorld();
            Predicate pred = (e) -> e instanceof MinecartEntity && ((MinecartEntity)e).hasPassengers() == true && ((Entity)e).distanceTo(firework) <= 10.0F;
            List<Entity> entities = world.getEntities(EntityType.MINECART, pred);

            for (Entity e : entities) {
                for (Entity p : e.getPassengerList()) {
                    Direction facing = p.getHorizontalFacing();
                    String cardinal = facing.toString();
                    if (cardinal.equals("north")) {
                        e.move(MovementType.SELF, new Vec3d(0, 0.5, -4));
                    } else if (cardinal.equals("east")) {
                        e.move(MovementType.SELF, new Vec3d(4, 0.5, 0));
                    } else if (cardinal.equals("west")) {
                        e.move(MovementType.SELF, new Vec3d(-4, 0.5, 0));
                    } else if (cardinal.equals("south")) {
                        e.move(MovementType.SELF, new Vec3d(0, 0.5, 4));
                    }
                }
            }
        }
    }
}
