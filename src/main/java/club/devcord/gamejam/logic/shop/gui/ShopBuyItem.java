package club.devcord.gamejam.logic.shop.gui;

import club.devcord.gamejam.message.Messenger;
import club.devcord.gamejam.utils.InventoryUtil;
import club.devcord.gamejam.utils.KeyValue;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.impl.SimpleItem;

public class ShopBuyItem extends SimpleItem {
    private ItemStack resultItem;
    private KeyValue<ItemStack, Integer> price;
    public ShopBuyItem(@NotNull ItemProvider itemProvider, ItemStack resultItem, KeyValue<ItemStack, Integer> price) {
        super(itemProvider);
        this.resultItem = resultItem;
        this.price = price;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        super.handleClick(clickType, player, event);

        if (InventoryUtil.hasNoSpaceInInventory(player)) {
            player.sendRichMessage(Messenger.PREFIX + "<red>Du hast keinen Platz im Inventar");
            return;
        }

        if (!InventoryUtil.hasEnoughItems(player, price.key(), price.value())) {
            player.sendRichMessage(Messenger.PREFIX + "<red>Du hast nicht genügend <yellow>" + price.key().getType().name());
            return;
        }

        InventoryUtil.removeSpecificItemCount(player, price.key(), price.value());
        player.sendRichMessage(Messenger.PREFIX + "<gray>Du hast " +
                MiniMessage.miniMessage().serialize(resultItem.displayName()) +
                " <gray>für <yellow>" +
                price.value() + "x " +
                price.key().getType().name() +
                " <gray>gekauft"
        );
        player.getInventory().addItem(resultItem);
    }
}
