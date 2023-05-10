package hu.rivalsnetwork.rivalsprofiles;

import hu.rivalsnetwork.rivalsapi.RivalsAPI;
import hu.rivalsnetwork.rivalsapi.RivalsAPIPlugin;
import hu.rivalsnetwork.rivalsapi.utils.Scheduler;
import hu.rivalsnetwork.rivalsprofiles.listener.PlayerDeathListener;
import hu.rivalsnetwork.rivalsprofiles.listener.PlayerJoinListener;
import hu.rivalsnetwork.rivalsprofiles.listener.PlayerQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class RivalsProfilesPlugin extends JavaPlugin {
    private static RivalsProfilesPlugin plugin;
    private static RivalsAPI api;
    private static Scheduler scheduler;

    @Override
    public void onEnable() {
        plugin = this;
        api = RivalsAPIPlugin.getApi();
        scheduler = new Scheduler(this);

        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
    }

    public static RivalsAPI getApi() {
        return api;
    }

    public static Scheduler scheduler() {
        return scheduler;
    }

    public static RivalsProfilesPlugin getInstance() {
        return plugin;
    }
}