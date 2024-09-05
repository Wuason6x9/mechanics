package dev.wuason.old.types.pages.content.anvil.items;

import dev.wuason.mechanics.inventory.items.ItemInterface;
import dev.wuason.mechanics.inventory.InvCustom;
import dev.wuason.old.types.pages.content.anvil.InvCustomPagesAnvil;
import dev.wuason.old.types.pages.content.anvil.events.NextPageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class NextPageItem extends ItemInterface {
    public NextPageItem(int slot, ItemStack itemStack) {
        super(slot, itemStack);
    }

    @Override
    public void onClick(InventoryClickEvent event, InvCustom inventoryCustom) {
        if(inventoryCustom instanceof InvCustomPagesAnvil<?>) {
            InvCustomPagesAnvil<?> inv = (InvCustomPagesAnvil<?>) inventoryCustom;
            if(!inv.isPossibleNextPage(inv.getActualPage())) return;
            NextPageEvent nextPageEvent = new NextPageEvent(inv.getActualPage(), inv.getActualPage() + 1, event, this);
            inv.onNextPage(nextPageEvent);
            inv.getNextPageListeners().forEach(nextPageEventConsumer -> nextPageEventConsumer.accept(nextPageEvent));
            if(nextPageEvent.isCancelled()) return;
            inv.setActualPage(inv.nextPage(inv.getActualPage()));
            inv.setContent(inv.getActualPage());
            inv.setButtonsPage(inv.getActualPage());
        }
    }
}
