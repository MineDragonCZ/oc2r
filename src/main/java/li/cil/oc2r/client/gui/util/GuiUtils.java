/* SPDX-License-Identifier: MIT */

package li.cil.oc2r.client.gui.util;

import li.cil.oc2r.api.bus.device.DeviceType;
import li.cil.oc2r.api.bus.device.DeviceTypes;
import li.cil.oc2r.client.gui.widget.Sprite;
import li.cil.oc2r.common.container.DeviceTypeSlotItemHandler;
import li.cil.oc2r.common.util.TooltipUtils;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;

import java.util.*;

import static li.cil.oc2r.common.util.TranslationUtils.text;

public final class GuiUtils {
    private static final Map<DeviceType, Component> WARNING_BY_DEVICE_TYPE = Util.make(() -> {
        final HashMap<DeviceType, Component> map = new HashMap<>();

        map.put(DeviceTypes.FLASH_MEMORY, text("tooltip.{mod}.flash_memory_missing"));
        map.put(DeviceTypes.MEMORY, text("tooltip.{mod}.memory_missing"));
        map.put(DeviceTypes.HARD_DRIVE, text("tooltip.{mod}.hard_drive_missing"));
        map.put(DeviceTypes.CPU, text("tooltip.{mod}.cpu_missing"));

        return map;
    });

    private static final int SLOT_SIZE = 18;
    private static final int DEVICE_INFO_ICON_SIZE = 28;
    private static final int RELATIVE_ICON_POSITION = (SLOT_SIZE - DEVICE_INFO_ICON_SIZE) / 2;

    ///////////////////////////////////////////////////////////////////

    public static <TContainer extends AbstractContainerMenu> void renderMissingDeviceInfoIcon(final GuiGraphics graphics, final AbstractContainerScreen<TContainer> screen, final DeviceType type, final Sprite icon) {
        findFirstSlotOfTypeIfAllSlotsOfTypeEmpty(screen.getMenu(), type).ifPresent(slot -> icon.draw(graphics,
            screen.getGuiLeft() + slot.x - 1 + RELATIVE_ICON_POSITION,
            screen.getGuiTop() + slot.y - 1 + RELATIVE_ICON_POSITION));
    }

    public static <TContainer extends AbstractContainerMenu> void renderMissingDeviceInfoTooltip(final GuiGraphics graphics, final AbstractContainerScreen<TContainer> screen, final int mouseX, final int mouseY, final DeviceType type) {
        renderMissingDeviceInfoTooltip(graphics, screen, mouseX, mouseY, type, Objects.requireNonNull(WARNING_BY_DEVICE_TYPE.get(type)));
    }

    public static <TContainer extends AbstractContainerMenu> void renderMissingDeviceInfoTooltip(final GuiGraphics graphics, final AbstractContainerScreen<TContainer> screen, final int mouseX, final int mouseY, final DeviceType type, final Component tooltip) {
        final Minecraft minecraft = screen.getMinecraft();
        if (minecraft.player == null) {
            return;
        }

        final boolean isCursorHoldingStack = !minecraft.player.inventoryMenu.getCarried().isEmpty();
        if (isCursorHoldingStack) {
            return;
        }

        final Slot hoveredSlot = screen.getSlotUnderMouse();
        if (hoveredSlot != null && hoveredSlot.hasItem()) {
            return;
        }

        findFirstSlotOfTypeIfAllSlotsOfTypeEmpty(screen.getMenu(), type).ifPresent(slot -> {
            if (slot == hoveredSlot) {
                TooltipUtils.drawTooltip(graphics, Collections.singletonList(tooltip), mouseX, mouseY);
            }
        });
    }

    ///////////////////////////////////////////////////////////////////

    private static Optional<DeviceTypeSlotItemHandler> findFirstSlotOfTypeIfAllSlotsOfTypeEmpty(final AbstractContainerMenu container, final DeviceType type) {
        DeviceTypeSlotItemHandler firstSlot = null;
        for (final Slot slot : container.slots) {
            if (slot instanceof final DeviceTypeSlotItemHandler typedSlot) {
                final DeviceType slotType = typedSlot.getDeviceType();
                if (slotType == type) {
                    if (slot.hasItem()) {
                        return Optional.empty();
                    } else if (firstSlot == null) {
                        firstSlot = typedSlot;
                    }
                }
            }
        }
        return Optional.ofNullable(firstSlot);
    }
}
