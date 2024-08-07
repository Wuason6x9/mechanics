package dev.wuason.mechanics.invmechanic.items.def;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.invmechanic.items.ItemInterface;
import dev.wuason.mechanics.invmechanic.types.InvCustom;
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

    public ButtonII(int slot, ItemStack itemOn, ItemStack itemOff, String id, String name, boolean state, ArrayList<Object> data) {
        super(slot, null, id, name, data);
        this.itemOn = itemOn;
        this.itemOff = itemOff;
    }

    public ButtonII(int slot, ItemStack itemOn, ItemStack itemOff, String id, String name, boolean state) {
        this(slot, itemOn, itemOff, id, name, state, new ArrayList<>());
    }

    public ButtonII(int slot, ItemStack itemOn, ItemStack itemOff, String id, String name) {
        this(slot, itemOn, itemOff, id, name, false, new ArrayList<>());
    }

    public ButtonII(int slot, ItemStack itemOn, ItemStack itemOff, String id) {
        this(slot, itemOn, itemOff, id, "", false, new ArrayList<>());
    }

    public ButtonII(int slot, ItemStack itemOn, ItemStack itemOff) {
        this(slot, itemOn, itemOff, UUID.randomUUID().toString(), "", false, new ArrayList<>());
    }

    public ButtonII(int slot, ItemStack itemOn, ItemStack itemOff, boolean state) {
        this(slot, itemOn, itemOff, UUID.randomUUID().toString(), "", state, new ArrayList<>());
    }

    public ButtonII(int slot, ItemStack itemOn, ItemStack itemOff, boolean state, ArrayList<Object> data) {
        this(slot, itemOn, itemOff, UUID.randomUUID().toString(), "", state, data);
    }

    public ButtonII(int slot, ItemStack itemOn, ItemStack itemOff, boolean state, String name) {
        this(slot, itemOn, itemOff, UUID.randomUUID().toString(), name, state, new ArrayList<>());
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

    //************ ITEM GETTER ************//

    private ItemStack getItemModified(ItemStack i){
        ItemStack itemStack = i.clone();
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(Mechanics.getInstance(), InvCustom.NAMESPACE_ITEM_INTERFACE_PREFIX), PersistentDataType.STRING, getId());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public ItemStack getItemModified() {
        return getItemModified(isState() ? getItemOn() : getItemOff());
    }

    @Override
    public ItemStack getItemStack() {
        return isState() ? getItemOn() : getItemOff();
    }

    //************ EVENT ************//

    @Override //DON'T OVERRIDE THIS METHOD
    public void onClick(InventoryClickEvent event, InvCustom inventoryCustom) {
        if(onButtonClick(event, inventoryCustom)) return;
        changeState();
        inventoryCustom.setItemInterfaceInv(this);
    }

    public boolean onButtonClick(InventoryClickEvent event, InvCustom inventoryCustom) {
        return false;
    }
}
