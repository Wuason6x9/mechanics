package dev.wuason.mechanics.invmechanic.types.pages.content.normal.items;

import dev.wuason.mechanics.invmechanic.types.pages.content.normal.events.NextPageEvent;
import dev.wuason.mechanics.invmechanic.items.ItemInterface;
import dev.wuason.mechanics.invmechanic.types.InvCustom;
import dev.wuason.mechanics.invmechanic.types.pages.content.normal.InvCustomPagesContent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public class NextPageItem extends ItemInterface {

    public NextPageItem(int slot, ItemStack itemStack) {
        super(slot, itemStack);
    }

    @Override
    public void onClick(InventoryClickEvent event, InvCustom inventoryCustom) {
        if (inventoryCustom instanceof InvCustomPagesContent) {
            Player player = (Player) event.getWhoClicked();
            InvCustomPagesContent inv = (InvCustomPagesContent) inventoryCustom;
            if (!inv.getManager().isPossibleNextPage(inv.getPage())) return;
            NextPageEvent nextPageEvent = new NextPageEvent(inv, event, inv.getPage(), inv.getPage() + 1, this);
            inv.getManager().onNextPage(nextPageEvent);
            List<Consumer<NextPageEvent>> nextPageEvents = inv.getManager().getNextPageListeners();
            nextPageEvents.forEach(nextPageEventConsumer -> nextPageEventConsumer.accept(nextPageEvent));
            if (nextPageEvent.isCancelled()) return;
            inv.getManager().open(player, inv.getPage() + 1);
        }
    }
}
