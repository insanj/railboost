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
import net.minecraft.item.ElytraItem;

@Mixin(value = ElytraItem.class)
public class MixinElytraItem {
    @Inject(method = "isUsable", at = @At(value = "RETURN"), cancellable=true) 
    private static void isUsable(ItemStack itemStack, CallbackInfoReturnable ci) {
        System.out.println(String.format("[MixinElytraItem] isUsable itemStack %s of item %s RETURN %s ", itemStack, itemStack.getItem(), ci.getReturnValue()));
    }
}
