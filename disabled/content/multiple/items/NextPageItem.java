package dev.wuason.old.types.pages.content.multiple.items;

import dev.wuason.old.types.pages.content.multiple.events.NextPageMultipleEvent;
import dev.wuason.mechanics.inventory.items.ItemInterface;
import dev.wuason.mechanics.inventory.InvCustom;
import dev.wuason.old.types.pages.content.multiple.PageCustomInfo;
import dev.wuason.old.types.pages.content.multiple.InvCustomPagesContentMultiple;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class NextPageItem extends ItemInterface {
    public NextPageItem(int slot, ItemStack itemStack) {
        super(slot, itemStack);
    }

    @Override
    public void onClick(InventoryClickEvent event, InvCustom inventoryCustom) {
        if(!(inventoryCustom instanceof InvCustomPagesContentMultiple) || getData().size() != 1) return;
        InvCustomPagesContentMultiple invCustom = (InvCustomPagesContentMultiple) inventoryCustom;
        UUID id = UUID.fromString(getData().get(0).toString());
        if(!invCustom.getCustomPagesInfo().containsKey(id)) return;
        PageCustomInfo<?> pageCustomInfo = invCustom.getCustomPagesInfo().get(id);
        if(!pageCustomInfo.isPossibleNextPage()) return;
        event.setCancelled(true);
        NextPageMultipleEvent nextPageMultipleEvent = new NextPageMultipleEvent(pageCustomInfo, event, pageCustomInfo.getActualPage(), pageCustomInfo.getActualPage() + 1, this);
        invCustom.onNextPage(nextPageMultipleEvent);
        if(nextPageMultipleEvent.isCancelled()) return;
        pageCustomInfo.nextPage();
        invCustom.setContentPage(pageCustomInfo);
        pageCustomInfo.setButtonsPage(invCustom);
    }
}
