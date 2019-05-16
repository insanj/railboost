package com.insanj.railboost;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.Tag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.FireworkItem;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class RailboostMod implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("railboost");

    private final float RAILBOOST_EXHAUSTION = 0.005F;
    private final float RAILBOOST_HORIZONTAL_VELOCITY = 4;
    private final float RAILBOOST_VERTICAL_VELOCITY = 1.5F;

    @Override
    public void onInitialize() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (!(world instanceof ServerWorld)) {
                return ActionResult.PASS;
            }

            ItemStack stack = player.getStackInHand(hand);
            if (!(stack.getItem() instanceof FireworkItem)) {
                return ActionResult.PASS;
            }

            if (player.hasVehicle() == false || player.getVehicle() == null) {
                return ActionResult.PASS;
            }

            Entity vehicle = player.getVehicle();

            stack.subtractAmount(1);
            player.swingHand(hand);
            player.addExhaustion(RAILBOOST_EXHAUSTION);

            Direction facing = player.getHorizontalFacing();
            String cardinal = facing.toString();
            float pitch = player.getPitch(0);

            // pitch works like this: 90=down, 0=horizon, -90=sky
            float clamped = Math.min(Math.max(pitch, -45), 45);
            float in_min = -45, in_max = 45, out_min = RAILBOOST_VERTICAL_VELOCITY, out_max = -RAILBOOST_VERTICAL_VELOCITY;
            float vertical = (clamped - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;

            Vec3d boost = new Vec3d(0, vertical, 0);
            if (cardinal.equals("north")) {
                boost = new Vec3d(0, vertical, -RAILBOOST_HORIZONTAL_VELOCITY);
            } else if (cardinal.equals("east")) {
                boost = new Vec3d(RAILBOOST_HORIZONTAL_VELOCITY, vertical, 0);
            } else if (cardinal.equals("west")) {
                boost = new Vec3d(-RAILBOOST_HORIZONTAL_VELOCITY, vertical, 0);
            } else if (cardinal.equals("south")) {
                boost = new Vec3d(0, vertical, RAILBOOST_HORIZONTAL_VELOCITY);
            }

            vehicle.addVelocity(boost.x, boost.y, boost.z);
            vehicle.velocityDirty = true;
            vehicle.move(MovementType.SELF, boost);

            BlockPos pos = player.getBlockPos();
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.PLAYERS, 1.0F, 1.0F);

            return ActionResult.SUCCESS;
        });
    }
}
