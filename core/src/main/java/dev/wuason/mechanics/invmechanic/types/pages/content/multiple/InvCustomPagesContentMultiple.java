package dev.wuason.mechanics.invmechanic.types.pages.content.multiple;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.invmechanic.types.InvCustom;
import dev.wuason.mechanics.invmechanic.types.pages.content.multiple.events.ContentMultipleClickEvent;
import dev.wuason.mechanics.invmechanic.types.pages.content.multiple.events.NextPageMultipleEvent;
import dev.wuason.mechanics.invmechanic.types.pages.content.multiple.events.PreviousPageMultipleEvent;
import dev.wuason.mechanics.items.ItemBuilderMechanic;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.function.Function;

public class InvCustomPagesContentMultiple extends InvCustom {


    private List<PageCustomInfo<?>> customPages = new ArrayList<>();
    private HashMap<UUID, PageCustomInfo<?>> customPagesInfo = new HashMap<>();
    public final static String NAMESPACED_CONTENT_KEY = "icm_content";



    public InvCustomPagesContentMultiple(String title, int size) {
        super(title, size);
        addClickEventsListeners(this::handleClick1);
    }

    public InvCustomPagesContentMultiple(String title, InventoryType type) {
        super(title, type);
        addClickEventsListeners(this::handleClick1);
    }

    public InvCustomPagesContentMultiple(String title, int size, Function<InvCustom, Inventory> function) {
        super(title, size, function);
        addClickEventsListeners(this::handleClick1);
    }

    public InvCustomPagesContentMultiple(Function<InvCustom, Inventory> function) {
        super(function);
        addClickEventsListeners(this::handleClick1);
    }



    //******************** CUSTOM PAGES ********************

    public List<PageCustomInfo<?>> getCustomPages() {
        return customPages;
    }

    public HashMap<UUID, PageCustomInfo<?>> getCustomPagesInfo() {
        return customPagesInfo;
    }

    public void setCustomPages(List<PageCustomInfo<?>> customPages, HashMap<UUID, PageCustomInfo<?>> customPagesInfo) {
        this.customPages = customPages;
        this.customPagesInfo = customPagesInfo;
    }

    public void addCustomPage(PageCustomInfo<?> customPage) {
        this.customPages.add(customPage);
        this.customPagesInfo.put(customPage.getId(), customPage);
    }

    public void removeCustomPage(UUID id) {
        PageCustomInfo<?> customPage = this.customPagesInfo.get(id);
        this.customPages.remove(customPage);
        this.customPagesInfo.remove(id);
    }

    public void clearCustomPages() {
        this.customPages.clear();
        this.customPagesInfo.clear();
    }

    //******************** CONTENT ********************

    public void setContentPage(PageCustomInfo<?> page) {
        page.clearDataSlots(this);
        for(Map.Entry<Integer, ?> entry : page.getActualPageContent().entrySet()){
            Content content = new Content(entry.getValue(), page);
            ItemStack item = onContentPage(content);
            if(item == null) item = new ItemBuilderMechanic(Material.BOOK).setName(entry.getValue().toString()).build();
            ItemMeta itemMeta = item.getItemMeta();
            String data = page.getContentList().indexOf(entry.getValue()) + ":" + page.getId().toString() + ":" + page.getActualPage() + ":" + entry.getKey();
            itemMeta.getPersistentDataContainer().set(new NamespacedKey(Mechanics.getInstance(), NAMESPACED_CONTENT_KEY), PersistentDataType.STRING, data);
            item.setItemMeta(itemMeta);
            super.getInventory().setItem(entry.getKey(), item);
        }
    }

    public void setContentAndButtons(PageCustomInfo<?> page) {
        setContentPage(page);
        page.setButtonsPage(this);
    }

    //******************** EVENTS ********************

    public ItemStack onContentPage(Content content){
        return null;
    }
    public void onNextPage(NextPageMultipleEvent event) {
    }
    public void onPreviousPage(PreviousPageMultipleEvent event) {
    }
    public void onContentClick(ContentMultipleClickEvent event){
    }

    //******************** HANDLE CLICK ********************

    public void handleClick1(InventoryClickEvent event){
        if(event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null) return;
        if(event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Mechanics.getInstance(), NAMESPACED_CONTENT_KEY), PersistentDataType.STRING)){
            String data = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Mechanics.getInstance(), NAMESPACED_CONTENT_KEY), PersistentDataType.STRING);
            String[] args = data.split(":");
            int slot = Integer.parseInt(args[3]);
            UUID id = UUID.fromString(args[1]);
            int page = Integer.parseInt(args[2]);
            PageCustomInfo<?> pageCustomInfo = this.customPagesInfo.get(id);
            Object content = pageCustomInfo.getContentList().get(Integer.parseInt(args[0]));
            ContentMultipleClickEvent contentClickEvent = new ContentMultipleClickEvent(slot, page, pageCustomInfo, content, event);
            onContentClick(contentClickEvent);
        }
    }
}
