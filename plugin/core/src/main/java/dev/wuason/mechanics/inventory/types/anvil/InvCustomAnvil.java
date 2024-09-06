package dev.wuason.mechanics.inventory.types.anvil;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.inventory.InvCustom;
import dev.wuason.mechanics.utils.AdventureUtils;
import dev.wuason.mechanics.nms.others.AnvilInventoryHolder;
import dev.wuason.mechanics.nms.wrappers.VersionWrapper;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class InvCustomAnvil extends InvCustom implements AnvilInventoryHolder {
    private VersionWrapper.AnvilInventoryCustom anvilInventoryCustom;
    private String renameText = "";
    private BukkitTask bukkitTask = null;
    private List<BiConsumer<String, String>> renameTextAsyncListeners = new ArrayList<>();

    public InvCustomAnvil(String title, Player player) {
        super();
        anvilInventoryCustom = Mechanics.getInstance().getServerNmsVersion().getVersionWrapper().createAnvilInventory(player, title, this);
        setInventory(anvilInventoryCustom.getInventory());
    }

    public InvCustomAnvil(Component title, Player player) {
        super();
        String titleString = title == null ? "" : AdventureUtils.serialize(title);
        anvilInventoryCustom = Mechanics.getInstance().getServerNmsVersion().getVersionWrapper().createAnvilInventory(player, titleString, this);
        setInventory(anvilInventoryCustom.getInventory());
    }

    public void onRenameTextAsync(String before, String now) {
    }

    public void addRenameTextListener(BiConsumer<String, String> listener) {
        renameTextAsyncListeners.add(listener);
    }

    public List<BiConsumer<String, String>> getRenameTextListeners() {
        return renameTextAsyncListeners;
    }

    //methods

    @Deprecated
    @Override
    public void open(Player player) {
        anvilInventoryCustom.open(player);
    }

    public void open() {
        anvilInventoryCustom.open();
    }

    public VersionWrapper.AnvilInventoryCustom getAnvilInventoryCustom() {
        return anvilInventoryCustom;
    }

    public AnvilInventory getAnvilInventoryInstance() {
        return anvilInventoryCustom.getInventory();
    }

    public void setMenuAnvilOptions() {
        anvilInventoryCustom.setMaxRepairCost(0);
        anvilInventoryCustom.setRepairItemCountCost(0);
        anvilInventoryCustom.setCheckReachable(false);
    }

    public String getRenameText() {
        return getAnvilInventoryInstance().getRenameText() == null ? "" : getAnvilInventoryInstance().getRenameText();
    }


    @Override
    public final void handleRenameTextAsync(String before, String now) {
        if (before == null) before = "";
        onRenameTextAsync(before, now);
        for (BiConsumer<String, String> listener : renameTextAsyncListeners) {
            listener.accept(before, now);
        }
    }
}
