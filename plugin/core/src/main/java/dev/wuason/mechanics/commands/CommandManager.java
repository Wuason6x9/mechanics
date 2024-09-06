package dev.wuason.mechanics.commands;

import de.tr7zw.changeme.nbtapi.NBT;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.compatibilities.adapter.Adapter;
import dev.wuason.mechanics.compatibilities.adapter.plugins.ImplAdapter;
import dev.wuason.mechanics.inventory.addons.pages.BackPageButton;
import dev.wuason.mechanics.inventory.addons.pages.ContentItem;
import dev.wuason.mechanics.inventory.addons.pages.NextPageButton;
import dev.wuason.mechanics.inventory.types.anvil.multipage.InvCustomAnvilPages;
import dev.wuason.mechanics.items.ItemBuilder;
import dev.wuason.mechanics.mechanics.MechanicAddon;
import dev.wuason.mechanics.utils.AdventureUtils;
import dev.wuason.mechanics.utils.StorageUtils;
import dev.wuason.mechanics.nms.NMSManager;
import dev.wuason.mechanics.nms.wrappers.VersionWrapper;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;

public class CommandManager {

    private Mechanics core;
    private CommandAPICommand command;

    public CommandManager(Mechanics core) {
        this.core = core;
        loadCommand();
    }

    public void loadCommand() {

        command = new CommandAPICommand("mechanics");
        command.withPermission("mechanics.command.main");
        command.withSubcommands(new CommandAPICommand("manager")

                .withSubcommands(new CommandAPICommand("getAll")
                        .executes((sender, args) -> {
                            sender.sendMessage(Arrays.stream(Bukkit.getPluginManager().getPlugins()).filter(plugin -> plugin instanceof MechanicAddon).map(plugin -> plugin.getName()).toList().toString());
                        })
                )
        );
        command.withSubcommands(new CommandAPICommand("debug")
                .withPermission("mechanics.command.debug")
                .withSubcommands(new CommandAPICommand("sendToastPlayer")
                        .withArguments(new PlayerArgument("player"))
                        .withArguments(new GreedyStringArgument("message"))
                        .executes((sender, args) -> {
                            Player player = (Player) args.get(0);
                            String message = (String) args.get(1);

                            NMSManager.getVersionWrapper().sendToast(player, new ItemStack(Material.DIAMOND), AdventureUtils.deserializeJson(message, player), VersionWrapper.ToastType.TASK);
                        })
                )
                .withSubcommands(new CommandAPICommand("getNBT")
                        .executesPlayer((player, commandArguments) -> {
                            ItemStack itemStack = player.getInventory().getItemInMainHand();
                            if (itemStack == null || itemStack.getType().isAir()) return;

                            player.sendMessage(NBT.itemStackToNBT(itemStack).toString());

                        })
                )
                .withSubcommands(new CommandAPICommand("test")
                        .executesPlayer((player, args) -> {

                            InvCustomAnvilPages<Material> invCustomAnvilPages = new InvCustomAnvilPages<>("Test", player) {

                                @Override
                                public void onContentClick(InventoryClickEvent event, ContentItem<Material> content) {
                                    player.sendMessage(content.getData().name());
                                    StorageUtils.addItemToInventoryOrDrop(player, new ItemStack(content.getData()));
                                }

                                @Override
                                public void onUpdate(boolean hasNext, boolean hasPrevious) {
                                    setPlayerItem(player, 0, null);
                                    setPlayerItem(player, 8, null);
                                    if (hasPrevious) {
                                        setPlayerItemInterface(player, new BackPageButton(0, ItemBuilder.of(Material.ARROW).setNameWithMiniMessage("<gold>Back page").build()));
                                    }
                                    if (hasNext) {
                                        setPlayerItemInterface(player, new NextPageButton(8, ItemBuilder.of(Material.ARROW).setNameWithMiniMessage("<gold>Next page").build()));
                                    }
                                }

                                @Override
                                public ItemStack onContentPage(int slot, int page, Material content) {
                                    return ItemBuilder.of(content).build();
                                }
                            };
                            invCustomAnvilPages.setContent(Arrays.stream(Material.values()).filter(material -> !material.isLegacy() && material.isBlock() && !material.isAir() && material.toString().contains("STAIRS")).toList());
                            invCustomAnvilPages.saveItems();
                            invCustomAnvilPages.updatePage();
                            invCustomAnvilPages.setRenameItem(ItemBuilder.of(Material.SPONGE).build(), true);
                            invCustomAnvilPages.open(false);

                        })
                )
                .withSubcommands(new CommandAPICommand("miniMessageFormatToJson")
                        .withArguments(new GreedyStringArgument("format"))
                        .executes((sender, args) -> {
                            String format = (String) args.get(0);
                            sender.sendMessage("(" + AdventureUtils.deserializeJson(format, null) + ")");
                            System.out.println(AdventureUtils.deserializeJson(format, null));
                        })
                )
                .withSubcommands(new CommandAPICommand("openInventoryTestMiniMessage")
                        .withArguments(new GreedyStringArgument("format"))
                        .executes((sender, args) -> {
                            Player player = (Player) sender;
                            String format = (String) args.get(0);
                            System.out.println(AdventureUtils.deserializeLegacy(format, player));
                            Inventory inventory = Bukkit.createInventory(player, 54, AdventureUtils.deserializeLegacy(format, player));
                            player.openInventory(inventory);
                        })
                )
        );
        command.withSubcommands(new CommandAPICommand("adapter")
                .withSubcommands(new CommandAPICommand("get")
                        .withArguments(new TextArgument("id"))
                        .withArguments(new IntegerArgument("amount").setOptional(true))
                        .executes((sender, args) -> {
                            String id = (String) args.get(0);
                            if (!Adapter.isValid(id)) return;
                            ItemStack itemStack = Adapter.getItemStack(id);
                            if (itemStack == null) {
                                sender.sendMessage("Invalid adapter ID");
                                return;
                            }
                            int amount = (int) args.getOrDefault(1, 1);
                            if (amount > 64 || amount < 1) amount = 64;
                            itemStack.setAmount(amount);
                            if (itemStack == null) return;
                            StorageUtils.addItemToInventoryOrDrop((Player) sender, itemStack);
                        })
                )
                .withSubcommands(new CommandAPICommand("give")
                        .withArguments(new EntitySelectorArgument.ManyPlayers("player"))
                        .withArguments(new TextArgument("id"))
                        .withArguments(new IntegerArgument("amount").setOptional(true))
                        .executes((sender, args) -> {
                            Collection<Player> players = (Collection<Player>) args.get(0);
                            if (players.isEmpty()) return;
                            String id = (String) args.get(1);
                            if (!Adapter.isValid(id)) return;
                            ItemStack itemStack = Adapter.getItemStack(id);
                            if (itemStack == null) {
                                sender.sendMessage("Invalid adapter ID");
                                return;
                            }
                            int amount = (int) args.getOrDefault(2, 1);
                            if (amount > 64 || amount < 1) amount = 64;
                            itemStack.setAmount(amount);
                            if (itemStack == null) return;
                            for (Player player : players) {
                                StorageUtils.addItemToInventoryOrDrop(player, itemStack);
                            }
                        })
                )
                .withSubcommands(new CommandAPICommand("getAdapterId")
                        .withArguments(new BooleanArgument("sendToConsole").setOptional(true))
                        .executes((sender, args) -> {
                            Player player = (Player) sender;
                            if (player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType().equals(Material.AIR))
                                return;
                            ItemStack itemStack = player.getInventory().getItemInMainHand();
                            String adapterId = Adapter.getAdapterId(itemStack);

                            AdventureUtils.sendMessage(player, String.format("<gold>id: <aqua> <hover:show_text:'<red>click to copy the clipboard'><click:copy_to_clipboard:'%s'>%s</click>", adapterId, adapterId));
                            if ((boolean) args.getOrDefault(0, false)) {
                                AdventureUtils.sendMessagePluginConsole("<gold>id: <aqua>" + adapterId);
                            }
                        })
                )
                .withSubcommands(new CommandAPICommand("isSimilar")
                        .withArguments(new TextArgument("id"))
                        .executesPlayer((player, args) -> {
                            String id = (String) args.get(0);
                            ItemStack itemStack = player.getInventory().getItemInMainHand();
                            if (itemStack == null || itemStack.getType().isAir()) return;
                            if (!Adapter.isValid(id)) {
                                player.sendMessage("Invalid adapter ID");
                                return;
                            }
                            if (Adapter.compareItems(id, itemStack)) {
                                player.sendMessage("The item is similar");
                            } else {
                                player.sendMessage("The item is not similar");
                            }
                        })
                )
                .withSubcommands(new CommandAPICommand("getAdapterIdBlock")
                        .executesPlayer((player, args) -> {
                            Block block = player.getTargetBlockExact(10, FluidCollisionMode.NEVER);
                            if (block == null) {
                                player.sendMessage("No block found in 10 blocks");
                                return;
                            }
                            String adapterId = Adapter.getAdapterId(block);
                            if (adapterId == null) {
                                player.sendMessage("No adapter ID found for the block");
                                return;
                            }

                            AdventureUtils.sendMessage(player, String.format("<gold>id: <aqua> <hover:show_text:'<red>click to copy the clipboard'><click:copy_to_clipboard:'%s'>%s</click>", adapterId, adapterId));
                        })
                )
                .withSubcommands(new CommandAPICommand("getAdapterIdBlockByType")
                        .withArguments(new TextArgument("type").replaceSuggestions(ArgumentSuggestions.strings(cSSI -> Adapter.getAdapters().stream().filter(adapter -> adapter.getPluginCompatibility().isEnabled()).map(adapter -> adapter.getType()).toArray(String[]::new))))
                        .executesPlayer((player, args) -> {
                            String type = (String) args.get(0);
                            if (!Adapter.existAdapterByType(type)) {
                                player.sendMessage("Invalid adapter type");
                                return;
                            }
                            Block block = player.getTargetBlockExact(10, FluidCollisionMode.NEVER);
                            if (block == null) {
                                player.sendMessage("No block found in 10 blocks");
                                return;
                            }
                            ImplAdapter implAdapter = Adapter.getAdapterByType(type);
                            String adapterId = implAdapter.getAdapterId(block);
                            if (adapterId == null) {
                                player.sendMessage("No adapter ID found for the block");
                                return;
                            }
                            AdventureUtils.sendMessage(player, String.format("<gold>id: <aqua> <hover:show_text:'<red>click to copy the clipboard'><click:copy_to_clipboard:'%s'>%s</click>", adapterId, adapterId));
                        })
                )
                .withSubcommands(new CommandAPICommand("getAdapterIdByType")
                        .withArguments(new TextArgument("type").replaceSuggestions(ArgumentSuggestions.strings(cSSI -> Adapter.getAdapters().stream().filter(adapter -> adapter.getPluginCompatibility().isEnabled()).map(adapter -> adapter.getType()).toArray(String[]::new))))
                        .executesPlayer((player, args) -> {
                            String type = (String) args.get(0);
                            if (!Adapter.existAdapterByType(type)) {
                                player.sendMessage("Invalid adapter type");
                                return;
                            }
                            ItemStack itemStack = player.getInventory().getItemInMainHand();
                            if (itemStack == null || itemStack.getType().isAir()) return;
                            ImplAdapter implAdapter = Adapter.getAdapterByType(type);
                            String adapterId = implAdapter.getAdapterId(itemStack);
                            if (adapterId == null) {
                                player.sendMessage("No adapter ID found for the item");
                                return;
                            }
                            AdventureUtils.sendMessage(player, String.format("<gold>id: <aqua> <hover:show_text:'<red>click to copy the clipboard'><click:copy_to_clipboard:'%s'>%s</click>", adapterId, adapterId));
                        })
                )
        );
        command.register();

    }
}
