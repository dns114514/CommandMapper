package com.example.commandmapper;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class CommandMapper extends JavaPlugin implements Listener {

    private FileConfiguration config;
    private Map<String, String> commandMappings = new HashMap<>();
    private boolean caseSensitive = false;

    @Override
    public void onEnable() {
        // 创建配置文件夹
        saveDefaultConfig();
        reloadConfig();

        // 注册BungeeCord通道（关键！）
        if (Bukkit.getMessenger().isOutgoingChannelRegistered(this, "BungeeCord")) {
            getLogger().info("BungeeCord通道已注册");
        } else {
            Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
            getLogger().info("已注册BungeeCord通道");
        }

        // 注册事件
        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("§a指令映射插件已启用！");
        getLogger().info("§a配置文件: " + getDataFolder().getPath() + "/config.yml");
        getLogger().info("§a使用 /cm reload 重载配置");
    }

    @Override
    public void onDisable() {
        getLogger().info("§c指令映射插件已禁用！");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("commandmapper") || cmd.getName().equalsIgnoreCase("cm")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("commandmapper.reload")) {
                    reloadConfig();
                    sender.sendMessage("§a✅ 配置文件已重载!");
                    return true;
                }
                sender.sendMessage("§c❌ 你没有权限执行此命令!");
                return true;
            } else if (args.length > 0 && args[0].equalsIgnoreCase("list")) {
                // 显示当前所有映射
                sender.sendMessage("§6========== §e当前指令映射 §6==========");
                for (Map.Entry<String, String> entry : commandMappings.entrySet()) {
                    sender.sendMessage("§e/" + entry.getKey() + " §7-> §a/" + entry.getValue());
                }
                sender.sendMessage("§6总映射数量: §e" + commandMappings.size());
                return true;
            }

            // 显示帮助
            sender.sendMessage("§6========== §e指令映射插件 §6==========");
            sender.sendMessage("§e/commandmapper reload §7- 重载配置");
            sender.sendMessage("§e/commandmapper list §7- 查看所有映射");
            sender.sendMessage("§e/cm §7- 简写");
            sender.sendMessage("§7配置文件位置: " + getDataFolder().getPath() + "/config.yml");
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (cmd.getName().equalsIgnoreCase("commandmapper") || cmd.getName().equalsIgnoreCase("cm")) {
            if (args.length == 1) {
                completions.add("reload");
                completions.add("list");
            }
            return completions;
        }

        return null;
    }

    @EventHandler
    public void onTabComplete(TabCompleteEvent event) {
        String buffer = event.getBuffer();

        if (!buffer.startsWith("/") || buffer.contains(" ")) {
            return;
        }

        String prefix = buffer.substring(1).toLowerCase();
        List<String> matches = new ArrayList<>();

        for (String command : commandMappings.keySet()) {
            if (command.toLowerCase().startsWith(prefix)) {
                matches.add("/" + command);
            }
        }

        if (!matches.isEmpty()) {
            event.setCompletions(matches);
        }
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage();

        // 获取映射后的命令
        String mapped = getMappedCommand(command);

        if (mapped != null) {
            // 记录日志
            if (config.getBoolean("log-mappings", true)) {
                getLogger().info(String.format(
                        "指令映射: %s -> /%s (玩家: %s)",
                        command, mapped, player.getName()
                ));
            }

            // 检查是否是BungeeCord的server命令
            if (isServerCommand(mapped)) {
                // 使用BungeeCord插件消息发送玩家
                event.setCancelled(true);
                sendToBungeeServer(player, extractServerName(mapped));
            } else {
                // 对于非server命令，直接修改事件
                event.setMessage("/" + mapped);
            }
        }
    }

    private String getMappedCommand(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        String command = input.trim();
        if (!command.startsWith("/")) {
            command = "/" + command;
        }

        // 移除斜杠
        String cmdWithoutSlash = command.substring(1);

        // 分割命令和参数
        String[] parts = cmdWithoutSlash.split(" ", 2);
        String commandName = parts[0];
        String arguments = parts.length > 1 ? " " + parts[1] : "";

        // 查找映射
        String key = caseSensitive ? commandName : commandName.toLowerCase();
        String mappedCommand = commandMappings.get(key);

        if (mappedCommand != null) {
            return mappedCommand + arguments;
        }

        return null;
    }

    private boolean isServerCommand(String command) {
        // 检查命令是否是server命令
        return command.toLowerCase().startsWith("server ");
    }

    private String extractServerName(String command) {
        // 从"server Lobby"中提取"Lobby"
        String[] parts = command.split(" ", 2);
        return parts.length > 1 ? parts[1] : "";
    }

    private void sendToBungeeServer(Player player, String serverName) {
        if (serverName == null || serverName.trim().isEmpty()) {
            player.sendMessage("§c服务器名称不能为空");
            return;
        }

        try {
            // 发送BungeeCord插件消息
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);

            // 写入"Connect"和服务器名称
            out.writeUTF("Connect");
            out.writeUTF(serverName.trim());

            // 发送消息到BungeeCord
            player.sendPluginMessage(this, "BungeeCord", b.toByteArray());

            getLogger().info("通过BungeeCord发送玩家 " + player.getName() + " 到服务器: " + serverName);

        } catch (IOException e) {
            getLogger().warning("无法发送BungeeCord消息: " + e.getMessage());
            player.sendMessage("§c无法连接到服务器，请稍后再试");

            // 回退：尝试使用玩家的聊天方式
            try {
                player.chat("/server " + serverName);
            } catch (Exception ex) {
                getLogger().warning("回退方法也失败: " + ex.getMessage());
            }
        }
    }

    @Override
    public void saveDefaultConfig() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }
    }

    @Override
    public void reloadConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);

        // 加载配置
        caseSensitive = config.getBoolean("case-sensitive", false);
        commandMappings.clear();

        ConfigurationSection mappings = config.getConfigurationSection("command-mappings");
        if (mappings != null) {
            for (String key : mappings.getKeys(false)) {
                String value = mappings.getString(key);
                if (value != null && !value.trim().isEmpty()) {
                    // 移除斜杠
                    String cleanKey = key.trim();
                    if (cleanKey.startsWith("/")) {
                        cleanKey = cleanKey.substring(1);
                    }

                    String mapKey = caseSensitive ? cleanKey : cleanKey.toLowerCase();
                    commandMappings.put(mapKey, value.trim());
                }
            }
        }

        getLogger().info("已加载 " + commandMappings.size() + " 个指令映射");
    }

    @Override
    public FileConfiguration getConfig() {
        return config;
    }
}