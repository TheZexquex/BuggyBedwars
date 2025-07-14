package dev.thezexquex.buggybedwars.logic.shop;

import de.oliver.fancynpcs.api.actions.types.ConsoleCommandAction;
import dev.thezexquex.buggybedwars.logic.Game;
import dev.thezexquex.buggybedwars.logic.settings.GameSettings;
import dev.thezexquex.buggybedwars.utils.RelativeLocation;
import de.oliver.fancynpcs.api.FancyNpcsPlugin;
import de.oliver.fancynpcs.api.NpcData;
import de.oliver.fancynpcs.api.actions.ActionTrigger;
import de.oliver.fancynpcs.api.actions.types.PlayerCommandAction;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ShopNPC {
    public void create(Game game) {
        if (!FancyNpcsPlugin.get().getNpcManager().getAllNpcs().isEmpty()) {
            return;
        }

        int i = GameSettings.SHOP_LOCATIONS.size();
        for (RelativeLocation position : GameSettings.SHOP_LOCATIONS) {
            var location = position.toBukkitLocation(game.gameMap().bukkitWorld());

            var id = "shop-" + i;

            var data = npcData(game, id, location);

            var npc = FancyNpcsPlugin.get().getNpcAdapter().apply(data);
            npc.setSaveToFile(false);
            FancyNpcsPlugin.get().getNpcManager().registerNpc(npc);
            npc.create();
            i--;
        }
    }

    private static @NotNull NpcData npcData(Game game, String id, Location location) {
        var data = new NpcData(id, UUID.randomUUID(), location);
        data.setSkin("https://cloud.thezexquex.dev/s/q7AstaB2nCeHacd/download/merchant-skin.png");
        data.setType(EntityType.PLAYER);
        data.setDisplayName("<gray>⚖ <dark_gray>| <#eba834>Shop");
        data.setTurnToPlayer(true);
        data.addAction(ActionTrigger.ANY_CLICK, 0, new ConsoleCommandAction(), "shop {player}");
        return data;
    }

    public void clear() {
        FancyNpcsPlugin.get().getNpcManager().getAllNpcs().forEach(npc -> {
            npc.removeForAll();
            FancyNpcsPlugin.get().getNpcManager().removeNpc(npc);
            FancyNpcsPlugin.get().getNpcManager().reloadNpcs();
        });
    }
}