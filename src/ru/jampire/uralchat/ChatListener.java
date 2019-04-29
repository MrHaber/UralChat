//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ru.jampire.uralchat;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ChatListener implements Listener {
    private Main plugin;

    public ChatListener(Main instance) {
        this.plugin = instance;
    }

    public static final Pattern PATTERN_ON_SPACE = Pattern.compile(" ", Pattern.LITERAL);

    @EventHandler(ignoreCancelled = true)
    public void on(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("UralChat.ignore")) {
            return;
        }

        String[] args = PATTERN_ON_SPACE.split(event.getMessage());
        if (args.length == 0) {
            return;
        }

        Pair<String, Integer> delay = this.getCommandDelay(args[0]);
        if (delay == null) {
            return;
        }

        String cmd = delay.getKey(); // Получаем ключи из Pair

        if (Cooldown.hasCooldown(player.getName(), commandName)) {
            player.sendMessage(this.plugin.getConfig().getString("DELAY_MESSAGE").replace("&", "§").replace("<delay>", "" + Cooldown.getCooldown(player.getName(), commandName)));
            event.setCancelled(true);
            return;
        }

        Cooldown.setCooldown(player.getName(), delay.getValue() * 1000L, commandName);
    }

    private Pair<String, Integer> getCommandDelay(String commandName) {
        commandName = StringUtils.removeStart(commandName, "/")
                .toLowerCase()
                .trim();
        Command command = Bukkit.getCommandMap().getKnownCommands().get(commandName);
        String resultName = command == null ? commandName : command.getName();

        FileConfiguration config = this.plugin.getConfig(); // быстрый поиск
        int delay = config.getInt("delays./" + commandName, -1);
        if (delay > 0) {
            return new ImmutablePair<>(resultName, delay);
        }

        List<String> aliasesLower = command == null ? Collections.emptyList() : command.getAliases().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList()); // Сортируем и отбираем команды по LowerCase

        for (String key : config.getConfigurationSection("delays").getKeys(false)) {
            String cfgCommand = StringUtils.removeStart(key, "/")
                    .toLowerCase()
                    .trim();

            if (commandName.equals(cfgCommand) || aliasesLower.contains(cfgCommand)) {
                return new ImmutablePair<>(resultName, config.getInt("delays." + key));
            }
        }

        return null;
    }
}
