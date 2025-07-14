package dev.thezexquex.buggybedwars.stage.ingame.listener;

import dev.thezexquex.buggybedwars.BuggyBedwarsPlugin;
import dev.thezexquex.buggybedwars.event.TeamBedDestroyEvent;
import dev.thezexquex.buggybedwars.event.TeamBedRebuildEvent;
import dev.thezexquex.buggybedwars.message.Messenger;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TeamBedStateChangeListener implements Listener {
    private BuggyBedwarsPlugin plugin;

    public TeamBedStateChangeListener(BuggyBedwarsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTeamBedDestroy(TeamBedDestroyEvent event) {
        var team = event.team();

        plugin.getServer().getOnlinePlayers().forEach(player -> {
            player.sendRichMessage(Messenger.PREFIX + "<dark_red>Das Bett von Team " + team.getFormattedName() + " <dark_red>wurde zerstört");
            player.playSound(Sound.sound(Key.key("entity.ender_dragon.growl"), Sound.Source.MASTER, 1.0F, 1.0F));
        });
    }

    @EventHandler
    public void onTeamBedRebuild(TeamBedRebuildEvent event) {
        var team = event.team();

        plugin.getServer().getOnlinePlayers().forEach(player -> {
            player.sendRichMessage(Messenger.PREFIX + "<green>Das Bett von Team " + team.getFormattedName() + " <green>wurde wieder aufgebaut");
            player.playSound(Sound.sound(Key.key("entity.breeze.idle_air"), Sound.Source.MASTER, 1.0F, 1.0F));
        });
    }
}
