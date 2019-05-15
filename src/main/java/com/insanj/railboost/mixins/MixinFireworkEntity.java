package com.insanj.railboost.mixins;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.Item;
import net.minecraft.item.FireworkItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.FireworkEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.lang.reflect.Field;

@Mixin(FireworkEntity.class)
public class MixinFireworkEntity {
    @Inject(method = "<init>*", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        System.out.println("[MixinFireworkEntity] init " + this);

        FireworkEntity firework = (FireworkEntity)(Object)this;
        Field[] fields = firework.getClass().getDeclaredFields();
        System.out.println("[MixinFireworkEntity] fields = " + fields);

        Field shooterField;
        LivingEntity entity;
        try {
            shooterField = firework.getClass().getDeclaredField("shooter");
            shooterField.setAccessible(true);
            entity = (LivingEntity)shooterField.get(firework);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("[MixinFireworkEntity] exception accessing shooter of firework: " + e);
            return;
        }

        if (entity == null) {
            System.out.println("[MixinFireworkEntity] null entity");
            return;
        }

        if (!entity.hasVehicle() || !(entity.getVehicle() instanceof MinecartEntity)) {
            System.out.println("[MixinFireworkEntity] entity not inside of minecart");
            return;
        }

        Direction facing = entity.getHorizontalFacing();
        System.out.println(String.format("[MixinFireworkEntity] facing %s x %d y %d z %d", facing, facing.getOffsetX(), facing.getOffsetY(), facing.getOffsetZ()));

        MovementType type = MovementType.SELF;
        Vec3i origin = facing.getVector();
        Vec3d offset = new Vec3d(origin.getX() * 4, 0, origin.getZ() * 4); 

        System.out.println(String.format("[MixinFireworkEntity] sending origin %s x %d z %d offset %s x %f y %f z %f", origin, origin.getX(), origin.getZ(), offset, offset.x, offset.y, offset.z));

        entity.move(type, offset);
    }
}
