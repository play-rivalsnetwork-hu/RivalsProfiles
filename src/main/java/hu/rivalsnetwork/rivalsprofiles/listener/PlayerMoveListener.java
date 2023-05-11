package hu.rivalsnetwork.rivalsprofiles.listener;

import hu.rivalsnetwork.rivalsprofiles.jumppad.JumpPad;
import hu.rivalsnetwork.rivalsprofiles.jumppad.JumpPads;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;
import org.spigotmc.event.entity.EntityDismountEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMoveEvent(@NotNull final PlayerMoveEvent event) {
        for (JumpPad pad : JumpPads.getPads()) {
            pad.check(event);
        }
    }

    @EventHandler
    public void onPlayerDismountEvent(@NotNull final EntityDismountEvent event) {
        if (event.getEntity() instanceof Player p) {
            if (!JumpPad.riding.contains(p)) return;

            event.setCancelled(true);
        }
    }
}
