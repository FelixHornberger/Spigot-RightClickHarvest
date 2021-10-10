package de.vendettagroup.rightclickharvest;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new RightClickHarvest(), this);
        getLogger().info("Hopefully my worst plugin, RightClickHarvest, is loaded");
    }

    @Override
    public void onDisable() {
        getLogger().info("RightClickHarvest gets disabeld! Huh?");
    }

}
