package com.mcbouncer.bukkit;

import com.mcbouncer.LocalConfiguration;
import com.mcbouncer.LocalServer;
import com.mcbouncer.MCBouncer;
import com.mcbouncer.util.NetUtils;
import java.util.Set;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Implementation of the LocalServer interface. It
 * is the main plugin class for Bukkit. It extends
 * JavaPlugin, and is set as the main class in plugin.yml.
 * 
 * It registers the configuration, sets the main controller,
 * registers the pseudo-listeners, interprets commands,
 * as well as implementing many of the server-specific 
 * methods that need to be called during the execution of
 * this plugin.
 * 
 */
public class BukkitServer extends JavaPlugin implements LocalServer {

    protected MCBouncer controller;

    @Override
    public void onDisable() {
        controller.getLogger().info("Plugin disabled (version " + MCBouncer.getVersion() + ")");
    }

    @Override
    public void onEnable() {
        
        LocalConfiguration config = new BukkitConfiguration(this.getDataFolder());
        config.load();

        controller = new MCBouncer(this, config);
        
        controller.getLogger().info("Plugin enabled. (version " + MCBouncer.getVersion() + ")");
        controller.getLogger().debug("Debug mode enabled!");

        setupListeners();

    }

    protected void setupListeners() {
        BukkitPlayerListener pl = new BukkitPlayerListener(controller);
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(pl, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        BukkitCommandThread r = new BukkitCommandThread(controller, sender, command, label, args);
        r.start();
        return true;
    }

    public boolean isPlayerOnline(String name) {
        return this.getServer().getPlayer(name) != null;
    }

    public String getPlayerName(String name) {
        Player player = this.getServer().getPlayer(name);
        if (player == null) {
            return name;
        }
        return player.getName();
    }

    public String getIPAddress(String ipOrName) {
        if (NetUtils.isIPAddress(ipOrName)) {
            return ipOrName;
        }

        Player player = this.getServer().getPlayer(ipOrName);
        if (player == null) {
            return "";
        }
        return player.getAddress().getAddress().getHostAddress();
    }

    public void kickPlayer(String name, String reason) {
        final Player player = this.getServer().getPlayer(name);
        kickPlayer(player, reason);
    }
    
    public void kickPlayer(final Player player, final String reason) {
        if (player != null) {
            this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

                public void run() {
                    player.kickPlayer(reason);
                }
            }, 1);
            
        }
    }

    public void kickPlayerWithIP(String ip, final String reason) {
        for (Player player : this.getServer().getOnlinePlayers()) {
            if (player.getAddress().getAddress().getHostAddress().equals(ip)) {
                kickPlayer(player, reason);
            }
        }
    }

    public void messageMods(String message) {
        String permission = "mcbouncer.mod";
        this.getServer().broadcast(message, permission);

        Set<Permissible> subs = getServer().getPluginManager().getPermissionSubscriptions(permission);
        for (Player player : getServer().getOnlinePlayers()) {
            if (player.hasPermission(permission) && !subs.contains(player)) {
                player.sendMessage(message);
            }
        }
    }

    public void broadcastMessage(String message) {
        this.getServer().broadcastMessage(message);
    }
}
