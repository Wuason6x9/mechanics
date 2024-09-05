package dev.wuason.old.types.pages.content.anvil.items;

import dev.wuason.mechanics.inventory.items.ItemInterface;
import dev.wuason.mechanics.inventory.InvCustom;
import dev.wuason.old.types.pages.content.anvil.InvCustomPagesAnvil;
import dev.wuason.old.types.pages.content.anvil.events.PreviousPageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PreviousPageItem extends ItemInterface {
    public PreviousPageItem(int slot, ItemStack itemStack) {
        super(slot, itemStack);
    }

    @Override
    public void onClick(InventoryClickEvent event, InvCustom inventoryCustom) {
        if(inventoryCustom instanceof InvCustomPagesAnvil<?>) {
            InvCustomPagesAnvil<?> inv = (InvCustomPagesAnvil<?>) inventoryCustom;
            if(!inv.isPossiblePreviousPage(inv.getActualPage())) return;
            PreviousPageEvent previousPageEvent = new PreviousPageEvent(inv.getActualPage(), inv.getActualPage() - 1, event, this);
            inv.onPreviousPage(previousPageEvent);
            inv.getPreviousPageListeners().forEach(previousPageEventConsumer -> previousPageEventConsumer.accept(previousPageEvent));
            if(previousPageEvent.isCancelled()) return;
            inv.setActualPage(inv.previousPage(inv.getActualPage()));
            inv.setContent(inv.getActualPage());
            inv.setButtonsPage(inv.getActualPage());
        }
    }
}
