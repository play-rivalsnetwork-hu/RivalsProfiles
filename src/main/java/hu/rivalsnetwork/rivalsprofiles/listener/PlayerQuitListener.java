package hu.rivalsnetwork.rivalsprofiles.listener;

import hu.rivalsnetwork.rivalsprofiles.database.Executor;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerQuitListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerQuitEvent(@NotNull final PlayerQuitEvent event) {
        Executor.serializeInventory(event.getPlayer());
        event.quitMessage(Component.empty());
    }
}
