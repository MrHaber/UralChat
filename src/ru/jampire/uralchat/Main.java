//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ru.jampire.uralchat;

import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public Logger log = this.getLogger();
    public Main plugin;

    public Main() {
    }

    public void onEnable() {
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new ChatListener(this), this);
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.getLogger().info("UralChat 3 Has Been Enabled!");
    }

    public void onDisable() {
        this.getLogger().info("UralChat 3 Has Been Disabled.");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            return false;
        } else if (args[0].equalsIgnoreCase("reload")) {
            this.reloadConfig();
            this.saveConfig();
            sender.sendMessage(ChatColor.GREEN + "[UralChat] Config reloaded");
            return true;
        } else {
            return args[0].equalsIgnoreCase("help");
        }
    }
}
