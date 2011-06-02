package com.mcbouncer.util;

import com.mcbouncer.plugin.MCBouncer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class MCBouncerUtil {

    public static void addBan(String playerName, String admin, String reason) {
        MCBouncerAPI.addBan(playerName, MCBouncerConfig.getApiKey(), admin, reason);
    }
    public static void removeBan(String playerName) {
        MCBouncerAPI.removeBan(playerName, MCBouncerConfig.getApiKey());
    }

    public static int getBanCount(String playerName, String IP) {
        return (int) (MCBouncerAPI.getBanCount(playerName, MCBouncerConfig.getApiKey()) + MCBouncerAPI.getIPBanCount(IP, MCBouncerConfig.getApiKey()));
    }

    public static int getNoteCount(String playerName) {
        return (int) MCBouncerAPI.getNoteCount(playerName, MCBouncerConfig.getApiKey());
    }

    public static boolean isAllowedToJoin(String playerName, String IP) {
        MCBouncerAPI.updateUser(playerName, MCBouncerConfig.getApiKey(), IP);
        return MCBouncerAPI.isBanned(playerName, MCBouncerConfig.getApiKey()) || MCBouncerAPI.isBanned(playerName, MCBouncerConfig.getApiKey()); // Second one should be isIpBanned()
    }

    public static String getBanReason(String playerName) {
        return MCBouncerAPI.getBanReason(playerName, MCBouncerConfig.getApiKey());
    }

    public static boolean isLocallyBanned(String playerName) {
        return true;
    }

    public static void modMessage(String message, Plugin plugin) {
        final Player[] p = plugin.getServer().getOnlinePlayers();
        for (int z = 0; z < p.length; z++) {
            if (MCBouncer.permissionHandler.has(p[z], "mcbouncer.mod")) {
                p[z].sendMessage(message);
            }
        }
    }
}