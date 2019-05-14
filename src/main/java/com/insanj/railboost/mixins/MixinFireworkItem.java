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

@Mixin(Item.class)
public class MixinFireworkItem {
    @Inject(method = "onEntityTick", at = @At(value = "HEAD"))
    public void onEntityTick(ItemStack stack, World world, Entity entity, int invSlot, boolean selected, CallbackInfo ci) {
        System.out.println("[MixinFireworkItem] onEntityTick world isRemote check");

        if (stack.getItem() instanceof FireworkItem && entity instanceof PlayerEntity) {
            System.out.println("[MixinFireworkItem] onEntityTick confirmed FireworkItem instance & PlayerEntity instance");

            PlayerEntity player = (PlayerEntity)entity;
            if (player.hasVehicle() == true && player.getVehicle() instanceof MinecartEntity && player.isUsingItem() == true) {
                System.out.println("[MixinFireworkItem] player confirmed riding minecart and using firework!");

                Entity vehicle = player.getVehicle();
                // BlockPos offPos = usageContext.getBlockPos().offset(usageContext.getFacing());
                Direction facing = player.getHorizontalFacing();
                System.out.println(String.format("[MixinFireworkItem] adjusting velocity, facing = %s, x = %d, z = %d", facing, facing.getOffsetX(), facing.getOffsetZ()));

                double deltaX = facing.getOffsetX() * 4.0;
                double deltaZ = facing.getOffsetZ() * 4.0;

                player.addVelocity(deltaX, 0, deltaZ);
                player.velocityDirty = true;

                System.out.println(String.format("[MixinFireworkItem] finished adding velocity + %s, deltaX = %d, z = %d", player.getVelocity(), deltaX, deltaZ));
            }
        }
    }
}
