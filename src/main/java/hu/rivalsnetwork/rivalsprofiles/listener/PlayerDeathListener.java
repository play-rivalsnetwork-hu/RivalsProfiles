package hu.rivalsnetwork.rivalsprofiles.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeathEvent(@NotNull final PlayerDeathEvent event) {
        event.setKeepInventory(true);
        event.deathMessage(Component.empty());
        event.setKeepLevel(true);
        event.setShouldDropExperience(false);
    }
}
