# CommandMapper - Minecraft Command Mapping Plugin / Minecraft 指令映射插件

[中文](#中文) | [English](#english)

<a name="中文"></a>
## 中文文档

### 简介
一个轻量级的 Minecraft 1.20.1 插件，允许通过配置文件自定义指令映射，支持 BungeeCord 服务器切换。

### 功能特性
✅ **指令映射** - 将玩家输入的指令映射为实际执行的指令  
✅ **大小写控制** - 可配置是否区分大小写  
✅ **BungeeCord 支持** - 支持 `/server` 命令映射到其他服务器  
✅ **Tab 补全** - 支持映射指令的 Tab 补全  
✅ **实时重载** - 无需重启服务器即可更新配置  
✅ **权限管理** - 支持插件管理权限  

### 安装
1. 下载最新的 `CommandMapper-1.0.0.jar`
2. 放入服务器的 `plugins` 文件夹
3. 重启服务器

### 配置
编辑 `plugins/CommandMapper/config.yml`：

```yaml
# 是否严格遵循大小写 (默认关闭)
case-sensitive: false

# 调试选项
debug-tab-complete: false
log-mappings: true

# 指令映射配置
command-mappings:
  hub: server Lobby
  spawn: spawn
  warp: warp
  shop: gui shop
  home: home
  h: server Lobby  # 简写

<a name="english"></a>

English Documentation
Introduction
A lightweight Minecraft 1.20.1 plugin that allows custom command mapping through configuration files, with BungeeCord server switching support.

Features
✅ Command Mapping - Map player-input commands to actual executed commands
✅ Case Sensitivity Control - Configurable case sensitivity
✅ BungeeCord Support - Support for /server command mapping to other servers
✅ Tab Completion - Support for tab completion of mapped commands
✅ Live Reload - Update configuration without server restart
✅ Permission Management - Plugin management permissions

Installation
Download the latest CommandMapper-1.0.0.jar
Place it in your server's plugins folder
Restart the server
Configuration
Edit plugins/CommandMapper/config.yml:

yaml
# Case sensitive (default off)
case-sensitive: false

# Debug options
debug-tab-complete: false
log-mappings: true

# Command mappings
command-mappings:
  hub: server Lobby
  spawn: spawn
  warp: warp
  shop: gui shop
  home: home
  h: server Lobby  # Short form
Commands
/commandmapper reload or /cm reload - Reload configuration file
/commandmapper list - View all mappings
/commandmapper find <command> - Find command mappings
/commandmapper test - Test tab completion
/commandmapper debug - Toggle debug mode
Building
bash
mvn clean package
The built plugin will be at target/CommandMapper-1.0.0.jar
Development Environment
Java 21
Maven 3.8+
Minecraft 1.20.1
Paper/Spigot API