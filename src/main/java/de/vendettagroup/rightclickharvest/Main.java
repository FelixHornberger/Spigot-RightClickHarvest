package de.vendettagroup.rightclickharvest;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main plugin;

    @Override
    public void onEnable() {
        plugin = this;
        plugin.getServer().getPluginManager().registerEvents(new RightClickHarvest(), this);
        this.getLogger().info("Hopefully my worst plugin, RightClickHarvest, is loaded");
    }

    @Override
    public void onDisable() {
        getLogger().info("RightClickHarvest gets disabeld! Huh?");
    }

}
