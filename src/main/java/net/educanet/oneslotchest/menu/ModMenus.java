package net.educanet.oneslotchest.menu;

import net.educanet.oneslotchest.Oneslotchest;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, Oneslotchest.MODID);

    public static final RegistryObject<MenuType<OneSlotChestMenu>> ONE_SLOT_CHEST_MENU =
            MENUS.register("one_slot_chest",
                    () -> IForgeMenuType.create((windowId, inv, data) -> new OneSlotChestMenu(windowId, inv)));
}

