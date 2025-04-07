package net.educanet.oneslotchest.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.educanet.oneslotchest.Oneslotchest;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Oneslotchest.MODID);

    public static final RegistryObject<SoundEvent> CHEST_OPEN =
            registerSound("chest_open");

    public static final RegistryObject<SoundEvent> CHEST_IDLE =
            registerSound("chest_idle");

    private static RegistryObject<SoundEvent> registerSound(String name) {
        return SOUND_EVENTS.register(name,
                () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Oneslotchest.MODID, name)));
    }
}
