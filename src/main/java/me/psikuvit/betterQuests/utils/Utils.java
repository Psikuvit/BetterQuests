package me.psikuvit.betterQuests.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    private Utils() {}

    public static void log(String message) {
        Bukkit.getLogger().info(message);
    }

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> color(List<String> messages) {
        return messages.stream().map(Utils::color).collect(Collectors.toList());
    }
}
