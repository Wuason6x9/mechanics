package dev.wuason.mechanics.invmechanic.types.pages.content.multiple.items;

import dev.wuason.mechanics.invmechanic.types.pages.content.multiple.events.PreviousPageMultipleEvent;
import dev.wuason.mechanics.invmechanic.items.ItemInterface;
import dev.wuason.mechanics.invmechanic.types.InvCustom;
import dev.wuason.mechanics.invmechanic.types.pages.content.multiple.PageCustomInfo;
import dev.wuason.mechanics.invmechanic.types.pages.content.multiple.InvCustomPagesContentMultiple;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PreviousPageItem extends ItemInterface {
    public PreviousPageItem(int slot, ItemStack itemStack) {
        super(slot, itemStack);
    }

    @Override
    public void onClick(InventoryClickEvent event, InvCustom inventoryCustom) {
        if(!(inventoryCustom instanceof InvCustomPagesContentMultiple) || getData().size() != 1) return;
        InvCustomPagesContentMultiple invCustom = (InvCustomPagesContentMultiple) inventoryCustom;
        UUID id = UUID.fromString(getData().get(0));
        if(!invCustom.getCustomPagesInfo().containsKey(id)) return;
        PageCustomInfo<?> pageCustomInfo = invCustom.getCustomPagesInfo().get(id);
        if(!pageCustomInfo.isPossiblePreviousPage()) return;
        event.setCancelled(true);
        PreviousPageMultipleEvent previousPageMultipleEvent = new PreviousPageMultipleEvent(pageCustomInfo, event, pageCustomInfo.getActualPage(), pageCustomInfo.getActualPage() - 1, this);
        invCustom.onPreviousPage(previousPageMultipleEvent);
        if(previousPageMultipleEvent.isCancelled()) return;
        pageCustomInfo.previousPage();
        invCustom.setContentPage(pageCustomInfo);
        pageCustomInfo.setButtonsPage(invCustom);
    }


}
