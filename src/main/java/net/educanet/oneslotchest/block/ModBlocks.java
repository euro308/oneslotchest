package net.educanet.oneslotchest.block;

import net.educanet.oneslotchest.Oneslotchest;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    // Register blocks
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Oneslotchest.MODID);

    // Register items (including block items)
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Oneslotchest.MODID);

    // The chest block
    public static final RegistryObject<Block> ONE_SLOT_CHEST =
            BLOCKS.register("one_slot_chest", OneSlotChestBlock::new);

    // The chest block item
    public static final RegistryObject<Item> ONE_SLOT_CHEST_ITEM =
            ITEMS.register("one_slot_chest",
                    () -> new BlockItem(ONE_SLOT_CHEST.get(), new Item.Properties()));
}
