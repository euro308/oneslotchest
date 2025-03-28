package net.educanet.oneslotchest.events;

import net.educanet.oneslotchest.Oneslotchest;
import net.educanet.oneslotchest.block.OneSlotChestBlock;
import net.educanet.oneslotchest.blockentity.OneSlotChestBlockEntity;
import net.educanet.oneslotchest.menu.OneSlotChestMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Oneslotchest.MODID)
public class OneSlotChestEventHandler {

    @SubscribeEvent
    public static void onContainerClose(PlayerContainerEvent.Close event) {
        if (event.getContainer() instanceof OneSlotChestMenu menu) {
            Container container = menu.getContainer();
            if (container instanceof OneSlotChestBlockEntity blockEntity) {
                Level level = event.getEntity().level();
                BlockPos pos = blockEntity.getBlockPos();
                BlockState state = level.getBlockState(pos);

                if (state.getBlock() instanceof OneSlotChestBlock &&
                        state.getValue(OneSlotChestBlock.OPEN)) {
                    level.setBlock(pos, state.setValue(OneSlotChestBlock.OPEN, Boolean.FALSE), 3);
                }
            }
        }
    }
}
