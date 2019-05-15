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
    // @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;alphaFunc(IF)V"), method = "render")
     //"FIELD")), target="Lnet/minecraft/entity/FireworkEntity;shooter:Lnet/minecraft/entity/LivingEntity;"))
    @Inject(method = "<init>*", at = @At(value="RETURN"))
    private void onFireworkShooterInit(CallbackInfo ci) {
        System.out.println(String.format("[MixinFireworkEntity] onFireworkShooterInit %s ci %s getId %s", this, ci, ci.getId()));

        FireworkEntity firework = (FireworkEntity)(Object)this;
        /*if (firework.getServer() == null) {
            System.out.println("[MixinFireworkEntity] detected non-server, don't do anything?");
            return;
        }*/

        //if (firework.wasShotByEntity() == false) {
        //    System.out.println("[MixinFireworkEntity] firework was not shot by entity, ignoring");
        //    return;
        //}

        Field shooterField;
        LivingEntity entity;
        try {
            Field[] fields = firework.getClass().getDeclaredFields();
            System.out.println("[MixinFireworkEntity] fields = " + fields);
            for (Field f : fields) {
                System.out.println("[MixinFireworkEntity] detected field " + f.getName());
                f.setAccessible(true);
                System.out.println("[MixinFireworkEntity] value = " + f.get(firework));
            }

            shooterField = firework.getClass().getDeclaredField("field_7616");
            shooterField.setAccessible(true);
            entity = (LivingEntity)shooterField.get(firework);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("[MixinFireworkEntity] exception accessing shooter of firework: " + e);
            return;
        }

        if (entity == null) {
            System.out.println("[MixinFireworkEntity] null shooter entity");
            return;
        }

        System.out.println("[MixinFireworkEntity] shooter entity = " + entity);

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
