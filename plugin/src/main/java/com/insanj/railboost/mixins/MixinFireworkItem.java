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

@Mixin(value = Item.class)
public class MixinFireworkItem {
    @Inject(method = "use", at = @At(value = "RETURN"), cancellable=true)
    public void use(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable ci) {
        System.out.println("[MixinFireworkItem] use " + hand);
    }

    @Inject(method = "useOnBlock", at = @At(value = "RETURN"), cancellable=true)
    public void useOnBlock(ItemUsageContext usageContext, CallbackInfoReturnable ci) {
        System.out.println("[MixinFireworkItem] useOnBlock starting thang");

        if (usageContext.getWorld().getServer() != null && usageContext.getWorld().getServer().isRemote()) {
            System.out.println("[MixinFireworkItem] beyond isRemote check");

            ItemStack stack = usageContext.getItemStack();
            if (stack.getItem() instanceof FireworkItem) {
                System.out.println("[MixinFireworkItem] useOnBlock confirmed FireworkItem instance");

                PlayerEntity player = usageContext.getPlayer();
                if (player.hasVehicle() == true && player.getVehicle() instanceof MinecartEntity) {
                    System.out.println("[MixinFireworkItem] player confirmed riding minecart and used firework!");

                    Entity vehicle = player.getVehicle();
                    // BlockPos offPos = usageContext.getBlockPos().offset(usageContext.getFacing());
                    Direction facing = usageContext.getFacing();
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

    // ****
    @Inject(method = "getUseAction", at = @At(value = "RETURN"), cancellable=true)
    public void getUseAction(ItemStack stack, CallbackInfoReturnable ci) {
        Object action = ci.getReturnValue();
        System.out.println("[MixinFireworkItem] getUseAction " + action);
    }

    @Inject(method = "onUsingTick", at = @At(value = "HEAD"))
    public void onUsingTick(World world, LivingEntity entity, ItemStack stack, int timeLeft, CallbackInfo ci) {
        System.out.println("[MixinFireworkItem] onUsingTick " + stack);
    }

    @Inject(method = "onEntityTick", at = @At(value = "HEAD"))
    public void onEntityTick(ItemStack stack, World world, Entity entity, int invSlot, boolean selected, CallbackInfo ci) {
        System.out.println("[MixinFireworkItem] onEntityTick " + stack);
    }

    @Inject(method = "onItemFinishedUsing", at = @At(value = "RETURN"))
    public void onItemFinishedUsing(ItemStack stack, World world, LivingEntity entity, CallbackInfoReturnable ci) {
        System.out.println("[MixinFireworkItem] onItemFinishedUsing " + stack);
    }

    @Inject(method = "onItemStopUsing", at = @At(value = "HEAD"))
    public void onItemStopUsing(ItemStack stack, World world, LivingEntity entity, int i, CallbackInfo ci) {
        System.out.println("[MixinFireworkItem] onItemStopUsing " + stack);
    }
}
