package dev.thezexquex.buggybedwars.stage.ingame.listener;

import dev.thezexquex.buggybedwars.logic.settings.GameSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class NaturalHealthRegenerationListener implements Listener {
    @EventHandler
    public void onNaturalHealthRegeneration(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player player) {
            event.setAmount(GameSettings.HEALTH_REGENERATION_RATE);
            player.setSaturation(20);
        }
    }
}
