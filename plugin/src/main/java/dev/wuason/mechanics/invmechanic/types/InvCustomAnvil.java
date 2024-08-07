package dev.wuason.mechanics.invmechanic.types;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.invmechanic.events.CloseEvent;
import dev.wuason.nms.wrappers.VersionWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class InvCustomAnvil extends InvCustom {
    private VersionWrapper.AnvilInventoryCustom anvilInventoryCustom;
    private String renameText = "";
    private BukkitTask bukkitTask = null;
    private List<BiConsumer<String,String>> renameTextAsyncListeners = new ArrayList<>();

    public InvCustomAnvil(String title, Player player) {
        super();
        anvilInventoryCustom = Mechanics.getInstance().getServerNmsVersion().getVersionWrapper().createAnvilInventory(player, title, this);
        setInventory(anvilInventoryCustom.getInventory());
        addCloseEventsListeners(this::handleClose1);
    }

    //events
    public void onRenameTextAsync(String before, String now){ // ONLY WORKS IF YOU USE FUNCTION setRenameTextListener



    }

    //listeners

    public void addRenameTextListener(BiConsumer<String,String> listener){
        renameTextAsyncListeners.add(listener);
    }

    public void removeRenameTextListener(BiConsumer<String,String> listener){
        renameTextAsyncListeners.remove(listener);
    }

    public void clearRenameTextListeners(){
        renameTextAsyncListeners.clear();
    }

    public List<BiConsumer<String,String>> getRenameTextListeners(){
        return renameTextAsyncListeners;
    }

    //methods
    @Deprecated
    @Override
    public void open(Player player) {
        open();
    }

    public void open(){
        anvilInventoryCustom.open();
    }

    public VersionWrapper.AnvilInventoryCustom getAnvilInventoryCustom(){
        return anvilInventoryCustom;
    }

    public AnvilInventory getAnvilInventoryInstance(){
        return anvilInventoryCustom.getInventory();
    }

    public void setMenuAnvilOptions(){
        anvilInventoryCustom.setMaxRepairCost(0);
        anvilInventoryCustom.setRepairItemCountCost(0);
        anvilInventoryCustom.setCheckReachable(false);
    }

    public void setRenameTextListener(Long ticksRefresh){
        if(bukkitTask != null) {
            bukkitTask.cancel();
            bukkitTask = null;
        }
        if(ticksRefresh<1L) return;
        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Mechanics.getInstance(), () -> {
            String renameTextAnvil = getAnvilInventoryInstance().getRenameText() == null ? "" : getAnvilInventoryInstance().getRenameText();
            if(!renameTextAnvil.equals(renameText)){
                onRenameTextAsync(renameText,renameTextAnvil);
                for(BiConsumer<String,String> listener : renameTextAsyncListeners){
                    listener.accept(renameText, renameTextAnvil);
                }
                renameText = renameTextAnvil;
            }
        },0L,ticksRefresh);
    }

    public String getRenameText(){
        return getAnvilInventoryInstance().getRenameText() == null ? "" : getAnvilInventoryInstance().getRenameText();
    }

    public void handleClose1(CloseEvent closeEvent) {
        if(closeEvent.isCancelled()) return;
        if(bukkitTask != null) {
            bukkitTask.cancel();
            bukkitTask = null;
        }
    }
}
