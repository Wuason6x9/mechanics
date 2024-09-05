package dev.wuason.old.types.pages.content.normal.events;

import dev.wuason.old.types.events.CustomEvent;
import dev.wuason.old.types.pages.content.normal.InvCustomPagesContent;
import org.bukkit.entity.Player;

public class OpenPageEvent extends CustomEvent {
    private final Player player;
    private final InvCustomPagesContent inventoryCustomPagesContent;



    public OpenPageEvent(Player player, InvCustomPagesContent inventoryCustomPagesContent) {
        super("OPEN_PAGE_EVENT");
        this.player = player;
        this.inventoryCustomPagesContent = inventoryCustomPagesContent;
    }

    //getters
    public Player getPlayer() {
        return player;
    }
    public InvCustomPagesContent getInventoryCustomPagesContent() {
        return inventoryCustomPagesContent;
    }
}
