package net.educanet.oneslotchest.blockentity;

import net.educanet.oneslotchest.Oneslotchest;
import net.educanet.oneslotchest.Oneslotchest;
import net.educanet.oneslotchest.block.ModBlocks;
import net.educanet.oneslotchest.blockentity.OneSlotChestBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Oneslotchest.MODID);

    public static final RegistryObject<BlockEntityType<OneSlotChestBlockEntity>> ONE_SLOT_CHEST =
            BLOCK_ENTITIES.register("one_slot_chest",
                    () -> BlockEntityType.Builder.of(OneSlotChestBlockEntity::new,
                            ModBlocks.ONE_SLOT_CHEST.get()).build(null));
}
