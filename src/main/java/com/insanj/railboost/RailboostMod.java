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
    private final float RAILBOOST_VERTICAL_VELOCITY = 1;

    @Override
    public void onInitialize() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (!(world instanceof ServerWorld)) {
                LOGGER.info("[RailboostMod] PASS because world is not server world");
                return ActionResult.PASS;
            }

            ItemStack stack = player.getStackInHand(hand);
            if (!(stack.getItem() instanceof FireworkItem)) {
                LOGGER.info("[RailboostMod] PASS because item is not instance of FireworkItem");
                return ActionResult.PASS;
            }

            if (player.hasVehicle() == false || player.getVehicle() == null) {
                LOGGER.info("[RailboostMod] PASS because vehicle is false or null");
                return ActionResult.PASS;
            }

            LOGGER.info("[RailboostMod] beginning success flow!");

            List<Entity> entities = player.getVehicle().getPassengerList();
            for (Entity e : entities) {
                for (Entity p : e.getPassengerList()) {
                    Direction facing = p.getHorizontalFacing();
                    String cardinal = facing.toString();
                    float pitch = p.getPitch(0);

                    float in_min = -180, in_max = 180, out_min = -RAILBOOST_VERTICAL_VELOCITY, out_max = RAILBOOST_VERTICAL_VELOCITY;
                    float vertical = (pitch - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;

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

                    e.move(MovementType.SELF, boost);
                    e.addVelocity(boost.x, boost.y, boost.z);
                    e.velocityModified = true;
                }
            }

            LOGGER.info("[onInitialize] finished railboosting it up yo!");

            player.swingHand(hand);
            player.addExhaustion(RAILBOOST_EXHAUSTION);
            player.playSound(SoundEvents.ENTITY_FIREWORK_ROCKET_SHOOT, 1.0F, 1.0F);
            stack.subtractAmount(1);

            LOGGER.info("[onInitialize] swinging hand and returning");

            return ActionResult.SUCCESS;
        });
    }
}
