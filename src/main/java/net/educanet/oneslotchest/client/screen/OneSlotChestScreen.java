package net.educanet.oneslotchest.client.screen;

import net.educanet.oneslotchest.Oneslotchest;
import net.educanet.oneslotchest.menu.OneSlotChestMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class OneSlotChestScreen extends AbstractContainerScreen<OneSlotChestMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Oneslotchest.MODID, "textures/gui/one_slot_chest_gui.png");

    // In OneSlotChestScreen.java
    public OneSlotChestScreen(OneSlotChestMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 131;
        this.inventoryLabelY = 38;
    }


    @Override
    protected void renderBg(GuiGraphics gui, float partialTick, int mouseX, int mouseY) {
        gui.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
        renderBackground(gui);
        super.render(gui, mouseX, mouseY, partialTick);
        renderTooltip(gui, mouseX, mouseY);
    }
}

