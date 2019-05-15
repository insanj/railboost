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
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;

@Mixin(EntityType.class)
public class MixinEntityType {
    @Inject(method = "spawnFromItemStack", at = @At(value="RETURN"))
    private void onSpawnFromItemStack(World world, ItemStack stack, PlayerEntity player, BlockPos pos, SpawnType spawnType, boolean b1, boolean b2, CallbackInfoReturnable ci) {
        System.out.println("[MixinEntityType] onSpawnFromItemStack ");
    }
/*
    @Inject(method = "spawn", at = @At(value="RETURN"))
    private void onSpawn(World world, CompoundTag itemTag, Component name, PlayerEntity player, BlockPos pos, SpawnType spawnType, boolean b1, boolean b2, CallbackInfoReturnable ci) {
        System.out.println("[MixinEntityType] onSpawn ");
    }*/
}

/*
Expected (Lnet/minecraft/class_1937;Lnet/minecraft/class_1799;Lnet/minecraft/class_1657;Lnet/minecraft/class_2338;Lnet/minecraft/class_3730;ZZLorg/spongepowered/asm/mixin/injection/callback/CallbackInfoReturnable;)V

 but found (Lnet/minecraft/class_1937;Lnet/minecraft/class_1799;Lnet/minecraft/class_1657;Lnet/minecraft/class_2338;Lnet/minecraft/class_3730;Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfoReturnable;)V
 at 
 */