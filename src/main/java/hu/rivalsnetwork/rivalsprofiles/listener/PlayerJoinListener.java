package hu.rivalsnetwork.rivalsprofiles.listener;

import hu.rivalsnetwork.rivalsprofiles.database.Executor;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoinEvent(@NotNull final PlayerJoinEvent event) {
        Executor.deserializeInventory(event.getPlayer());
        event.joinMessage(Component.empty());
    }
}
