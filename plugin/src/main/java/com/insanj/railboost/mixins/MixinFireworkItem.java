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
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.entity.Entity;

@Mixin(value = Item.class, priority = 5000)
public class MixinFireworkItem {
    @Inject(method = "onItemStopUsing", at = @At(value = "HEAD")) 
    private void onItemStopUsing(ItemStack stack, World world, LivingEntity entity, int i, CallbackInfo ci) {//(World world, LivingEntity entity, ItemStack stack, int timeLeft, CallbackInfo ci) {
        System.out.println("step 1");

        Item item = stack.getItem(); //(Item)(Object)this;

        if (item instanceof FireworkItem) {
            System.out.println("step 2");

            if (entity.hasVehicle() == true) {
                System.out.println("step 3");

                Entity vehicle = entity.getVehicle();
                if (vehicle instanceof MinecartEntity) {
                    System.out.println("step 4");

                    Direction facing = entity.getHorizontalFacing();
                    entity.addVelocity(facing.getOffsetX(), facing.getOffsetY(), facing.getOffsetZ());
                    entity.velocityDirty = true;

                    System.out.println("step 5");
                }
            }
        }
    }
}
