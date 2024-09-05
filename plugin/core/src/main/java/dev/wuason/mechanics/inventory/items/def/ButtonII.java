package dev.wuason.mechanics.inventory.items.def;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.inventory.items.ItemInterface;
import dev.wuason.mechanics.inventory.InvCustom;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.UUID;

public class ButtonII extends ItemInterface {

    private ItemStack itemOn;
    private ItemStack itemOff;
    private boolean state = false;

    public ButtonII(int slot, ItemStack itemOn, ItemStack itemOff, String id, String name, boolean state) {
        super(slot, null, id, name);
        this.itemOn = itemOn;
        this.itemOff = itemOff;
    }

    public ButtonII(int slot, ItemStack itemOn, ItemStack itemOff, String id, String name) {
        this(slot, itemOn, itemOff, id, name, false);
    }

    public ButtonII(int slot, ItemStack itemOn, ItemStack itemOff, String id) {
        this(slot, itemOn, itemOff, id, "", false);
    }

    public ButtonII(int slot, ItemStack itemOn, ItemStack itemOff) {
        this(slot, itemOn, itemOff, UUID.randomUUID().toString(), "", false);
    }

    public ButtonII(int slot, ItemStack itemOn, ItemStack itemOff, boolean state) {
        this(slot, itemOn, itemOff, UUID.randomUUID().toString(), "", state);
    }

    public ButtonII(int slot, ItemStack itemOn, ItemStack itemOff, boolean state, String name) {
        this(slot, itemOn, itemOff, UUID.randomUUID().toString(), name, state);
    }

    //************ GETTERS ************//

    public ItemStack getItemOn() {
        return itemOn;
    }

    public ItemStack getItemOff() {
        return itemOff;
    }

    public boolean isState() {
        return state;
    }

    private ItemStack getItemModified(ItemStack i){
        ItemStack itemStack = i.clone();
        return ItemInterface.apply(itemStack, this.getId());
    }

    @Override
    public ItemStack getItemModified() {
        return getItemModified(isState() ? getItemOn() : getItemOff());
    }

    @Override
    public ItemStack getItemStack() {
        return isState() ? getItemOn() : getItemOff();
    }

    //************ SETTERS ************//

    public void setItemOn(ItemStack itemOn) {
        this.itemOn = itemOn;
    }

    public void setItemOff(ItemStack itemOff) {
        this.itemOff = itemOff;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    //************ METHODS ************//

    public void changeState(){
        setState(!isState());
    }

    public void changeState(boolean state){
        setState(state);
    }

    //************ EVENT ************//

    @Override
    public final void onClick(InventoryClickEvent event, InvCustom invCustom) {
        if(!onButtonClick(event, invCustom)) return;
        changeState();
        invCustom.updateItemInterface(this);
    }

    protected boolean onButtonClick(InventoryClickEvent event, InvCustom inventoryCustom) {
        return true;
    }
}
