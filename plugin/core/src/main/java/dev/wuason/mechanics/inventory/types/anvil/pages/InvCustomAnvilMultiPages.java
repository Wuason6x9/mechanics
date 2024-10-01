package dev.wuason.mechanics.inventory.types.anvil.pages;

import dev.wuason.mechanics.inventory.pages.MultiPages;
import dev.wuason.mechanics.inventory.pages.Page;
import dev.wuason.mechanics.inventory.types.anvil.InvCustomAnvil;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class InvCustomAnvilMultiPages extends InvCustomAnvil implements MultiPages {

    private final List<Page<?>> pages = new ArrayList<>();


    public InvCustomAnvilMultiPages(String title, Player player) {
        super(title, player);
    }

    public InvCustomAnvilMultiPages(Component title, Player player) {
        super(title, player);
    }

    @Override
    public List<Page<?>> getPages() {
        return pages;
    }

}
