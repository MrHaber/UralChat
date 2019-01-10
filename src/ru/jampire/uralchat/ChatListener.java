//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ru.jampire.uralchat;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class ChatListener implements Listener {
    public Main plugin;

    public ChatListener(Main instance) {
        this.plugin = instance;
    }

    @EventHandler
    public void PlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        Iterator var4 = this.plugin.getConfig().getConfigurationSection("delays").getValues(false).keySet().iterator();

        while(true) {
            String s;
            do {
                if (!var4.hasNext()) {
                    if (!player.hasPermission("UralChat.ignore")) {
                        boolean b = true;
                        Iterator var5 = this.plugin.getConfig().getStringList("commands").iterator();

                        label73:
                        while(true) {
                            do {
                                if (!var5.hasNext()) {
                                    if (b) {
                                        return;
                                    }

                                    List<String> list = this.plugin.getConfig().getStringList("list");
                                    String msg = event.getMessage().toLowerCase();

                                    for (Player pl : Bukkit.getOnlinePlayers()) {
                                        msg = msg.replaceAll(pl.getName().toLowerCase(), "");
                                    }

                                    for(int x = 0; x < list.toArray().length; ++x) {
                                        String word = (String)list.toArray()[x];
                                        word = word.toLowerCase();
                                        if (msg.contains(word)) {
                                            if (this.plugin.getConfig().getBoolean("BEEP_WORD")) {
                                                event.setMessage(event.getMessage().toLowerCase().replaceAll(word.toLowerCase(), this.plugin.getConfig().getString("NEW_MESSAGE")));
                                            }

                                            if (this.plugin.getConfig().getBoolean("REPLACE_MESSAGE")) {
                                                event.setMessage(this.plugin.getConfig().getString("NEW_MESSAGE"));
                                            } else if (!this.plugin.getConfig().getBoolean("BEEP_WORD")) {
                                                event.setCancelled(true);
                                            }

                                            if (this.plugin.getConfig().getBoolean("EXPLOSION_ON_SWEAR")) {
                                                event.getPlayer().getWorld().createExplosion(event.getPlayer().getLocation(), 0.0F);
                                            }

                                            this.plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), this.plugin.getConfig().getString("COMMAND_ON_SWEAR").replace("<player>", event.getPlayer().getName()));
                                            if (Boolean.parseBoolean(this.plugin.getConfig().getString("KICK_ON_SWEAR"))) {
                                                player.kickPlayer(this.plugin.getConfig().getString("KICK_MESSAGE").replaceAll("&", "§"));
                                            } else {
                                                player.sendMessage(this.plugin.getConfig().getString("KICK_MESSAGE").replaceAll("&", "§"));
                                            }
                                        }
                                    }
                                    break label73;
                                }

                                s = (String)var5.next();
                            } while(!event.getMessage().toLowerCase().startsWith(s.toLowerCase() + " ") && !event.getMessage().equalsIgnoreCase(s));

                            b = false;
                        }
                    }

                    return;
                }

                s = (String)var4.next();
            } while(!event.getMessage().toLowerCase().startsWith(s.toLowerCase() + " ") && !event.getMessage().equalsIgnoreCase(s));

            if (Cooldown.hasCooldown(player.getName(), s)) {
                player.sendMessage(this.plugin.getConfig().getString("DELAY_MESSAGE").replaceAll("&", "§").replaceAll("<delay>", "" + Cooldown.getCooldown(player.getName(), s)));
                event.setCancelled(true);
                return;
            }

            Cooldown.setCooldown(player.getName(), this.plugin.getConfig().getLong("delays." + s + ".delay") * 1000L, s);
        }
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        if (Cooldown.hasCooldown(player.getName(), "chat")) {
            player.sendMessage(this.plugin.getConfig().getString("DELAY_MESSAGE_CHAT").replaceAll("&", "§").replaceAll("<delay>", "" + Cooldown.getCooldown(player.getName(), "chat")));
            event.setCancelled(true);
        } else {
            Cooldown.setCooldown(player.getName(), this.plugin.getConfig().getLong("delays.chat.delay") * 1000L, "chat");
            if (!player.hasPermission("UralChat.ignore")) {
                List<String> list = this.plugin.getConfig().getStringList("list");
                String msg = event.getMessage().toLowerCase();

                for (Player pl : Bukkit.getOnlinePlayers()) {
                    msg = msg.replaceAll(pl.getName().toLowerCase(), "");
                }

                for(int x = 0; x < list.toArray().length; ++x) {
                    String word = (String)list.toArray()[x];
                    word = word.toLowerCase();
                    if (msg.contains(word)) {
                        if (this.plugin.getConfig().getBoolean("BEEP_WORD")) {
                            event.setMessage(event.getMessage().toLowerCase().replaceAll(word.toLowerCase(), this.plugin.getConfig().getString("NEW_MESSAGE")));
                        }

                        if (this.plugin.getConfig().getBoolean("REPLACE_MESSAGE")) {
                            event.setMessage(this.plugin.getConfig().getString("NEW_MESSAGE"));
                        } else if (!this.plugin.getConfig().getBoolean("BEEP_WORD")) {
                            event.setCancelled(true);
                        }

                        if (this.plugin.getConfig().getBoolean("EXPLOSION_ON_SWEAR")) {
                            event.getPlayer().getWorld().createExplosion(event.getPlayer().getLocation(), 0.0F);
                        }

                        this.plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), this.plugin.getConfig().getString("COMMAND_ON_SWEAR").replace("<player>", event.getPlayer().getName()));
                        if (Boolean.parseBoolean(this.plugin.getConfig().getString("KICK_ON_SWEAR"))) {
                            player.kickPlayer(this.plugin.getConfig().getString("KICK_MESSAGE").replaceAll("&", "§"));
                        } else {
                            player.sendMessage(this.plugin.getConfig().getString("KICK_MESSAGE").replaceAll("&", "§"));
                        }
                    }
                }
            }

        }
    }
}
