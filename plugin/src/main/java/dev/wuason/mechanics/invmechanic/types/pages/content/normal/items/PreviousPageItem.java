package dev.wuason.mechanics.invmechanic.types.pages.content.normal.items;

import dev.wuason.mechanics.invmechanic.types.pages.content.normal.events.PreviousPageEvent;
import dev.wuason.mechanics.invmechanic.items.ItemInterface;
import dev.wuason.mechanics.invmechanic.types.InvCustom;
import dev.wuason.mechanics.invmechanic.types.pages.content.normal.InvCustomPagesContent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public class PreviousPageItem extends ItemInterface {
    public PreviousPageItem(int slot, ItemStack itemStack) {
        super(slot, itemStack);
    }

    @Override
    public void onClick(InventoryClickEvent event, InvCustom inventoryCustom) {
        if(inventoryCustom instanceof InvCustomPagesContent) {
            Player player = (Player) event.getWhoClicked();
            InvCustomPagesContent inv = (InvCustomPagesContent) inventoryCustom;
            if(!inv.getManager().isPossiblePreviousPage(inv.getPage())) return;
            PreviousPageEvent previousPageEvent = new PreviousPageEvent(inv, event, inv.getPage(), inv.getPage() - 1, this);
            inv.getManager().onPreviousPage(previousPageEvent);;
            List<Consumer<PreviousPageEvent>> previousPageEvents = inv.getManager().getPreviousPageListeners();
            previousPageEvents.forEach(previousPageEventConsumer -> previousPageEventConsumer.accept(previousPageEvent));
            if(previousPageEvent.isCancelled()) return;
            inv.getManager().open(player, inv.getPage() - 1);
        }
    }
}
