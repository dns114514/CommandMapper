# CommandMapper - Minecraft Command Mapping Plugin / Minecraft 指令映射插件
 
[中文](#中文) | [English](#english)

<a name="中文"></a>
## 中文文档

### 简介
一个轻量级的 Minecraft 1.20.1 插件，允许通过配置文件自定义指令映射，支持 BungeeCord 服务器切换。

### 功能特性
✅ **指令映射** - 将玩家输入的指令映射为实际执行的指令  
✅ **大小写控制** - 可配置是否区分大小写    
✅ **Tab 补全** - 支持映射指令的 Tab 补全（可显示映射后的指令补全）  
✅ **实时重载** - 无需重启服务器即可更新配置（使用 `/cm reload`）  
✅ **权限管理** - 精细的权限控制，支持管理员操作  

### 安装
1. 下载最新的 `CommandMapper-1.0.0.jar`
2. 放入服务器的 `plugins` 文件夹
3. 重启服务器 **或** 使用 `/cm reload` 热加载配置（启动后）

### 配置
编辑 `plugins/CommandMapper/config.yml`：

```yaml
# 是否严格遵循大小写（默认关闭）
case-sensitive: false

# 调试选项
debug-tab-complete: false   # 是否在控制台输出 Tab 补全调试信息
log-mappings: true          # 是否在日志中记录映射执行情况

# 指令映射配置（注意：目标指令请勿加前导斜杠 '/'）
command-mappings:
  hub: server Lobby          # 玩家输入 /hub  → 实际执行 /server Lobby
  spawn: spawn               # 玩家输入 /spawn → 实际执行 /spawn
  warp: warp                 # 玩家输入 /warp  → 实际执行 /warp
  shop: gui shop             # 玩家输入 /shop  → 实际执行 /gui shop
  home: home                 # 玩家输入 /home  → 实际执行 /home
  h: server Lobby            # 玩家输入 /h     → 实际执行 /server Lobby（简写）
```

### 命令（管理员）
所有命令需拥有对应权限，可使用简写 `/cm` 代替 `/commandmapper`。

| 命令 | 说明 | 所需权限 |
|------|------|----------|
| `/commandmapper reload` 或 `/cm reload` | 重新加载配置文件 | `commandmapper.reload` |
| `/commandmapper list`                   | 列出当前所有映射 | `commandmapper.list` |
| `/commandmapper find <指令>`            | 查找某个指令的映射结果 | `commandmapper.find` |
| `/commandmapper test`                   | 测试 Tab 补全功能（调试用） | `commandmapper.test` |
| `/commandmapper debug`                  | 切换调试模式（开关） | `commandmapper.debug` |

### 权限节点
| 权限节点 | 默认拥有者 | 说明 |
|----------|-----------|------|
| `commandmapper.reload` | OP | 允许重载配置 |
| `commandmapper.list`   | OP | 允许查看映射列表 |
| `commandmapper.find`   | OP | 允许查找指令映射 |
| `commandmapper.test`   | OP | 允许执行 Tab 补全测试 |
| `commandmapper.debug`  | OP | 允许切换调试模式 |
| `commandmapper.use.*`  | 所有玩家 | 允许使用所有映射指令（默认所有玩家均可使用映射） |

> 若需限制玩家使用特定映射，可通过其它权限插件配合拦截，插件本身不提供细粒度指令级权限。

### 构建
`bash
mvn clean package
```
构建产物位于 `target/CommandMapper-1.0.0.jar`。

### 开发环境
- Java 21
- Maven 3.8+
- Minecraft 1.20.1
- Paper / Spigot API

```

<a name="english"></a>
## English Documentation

### Introduction
A lightweight Minecraft 1.20.1 plugin that allows custom command mapping through configuration files, with BungeeCord server switching support.

### Features
✅ **Command Mapping** - Map player-input commands to actual executed commands  
✅ **Case Sensitivity Control** - Configurable case sensitivity  
✅ **Tab Completion** - Support for tab completion of mapped commands (shows completions for the target command)  
✅ **Live Reload** - Update configuration without server restart (use `/cm reload`)  
✅ **Permission Management** - Fine-grained permission control for admin operations  

### Installation
1. Download the latest `CommandMapper-1.0.0.jar`
2. Place it in your server's `plugins` folder
3. Restart the server **or** use `/cm reload` to hot-load the configuration after startup

### Configuration
Edit `plugins/CommandMapper/config.yml`:

```yaml
# Case sensitive (default off)
case-sensitive: false

# Debug options
debug-tab-complete: false   # Print tab completion debug info to console
log-mappings: true          # Log mapping executions

# Command mappings (do NOT include leading slash '/' in target commands)
command-mappings:
  hub: server Lobby          # /hub  → /server Lobby
  spawn: spawn               # /spawn → /spawn
  warp: warp                 # /warp  → /warp
  shop: gui shop             # /shop  → /gui shop
  home: home                 # /home  → /home
  h: server Lobby            # /h     → /server Lobby (shortcut)
```

### Commands (Admin)
All commands require corresponding permissions. Short form `/cm` can be used instead of `/commandmapper`.

| Command | Description | Required Permission |
|---------|-------------|----------------------|
| `/commandmapper reload` or `/cm reload` | Reload configuration file | `commandmapper.reload` |
| `/commandmapper list`                   | List all current mappings | `commandmapper.list` |
| `/commandmapper find <command>`         | Find mapping result for a command | `commandmapper.find` |
| `/commandmapper test`                   | Test tab completion (debug) | `commandmapper.test` |
| `/commandmapper debug`                  | Toggle debug mode | `commandmapper.debug` |

### Permission Nodes
| Permission Node | Default Holder | Description |
|-----------------|----------------|-------------|
| `commandmapper.reload` | OP | Allow reloading config |
| `commandmapper.list`   | OP | Allow viewing mapping list |
| `commandmapper.find`   | OP | Allow finding command mappings |
| `commandmapper.test`   | OP | Allow testing tab completion |
| `commandmapper.debug`  | OP | Allow toggling debug mode |
| `commandmapper.use.*`  | All players | Allow using all mapped commands (enabled by default for everyone) |

> To restrict specific players from using certain mapped commands, use other permission plugins to intercept; this plugin does not provide per-command granularity.

### Building
```bash
mvn clean package
```
The built plugin will be at `target/CommandMapper-1.0.0.jar`.

### Development Environment
- Java 21
- Maven 3.8+
- Minecraft 1.20.1
- Paper / Spigot API
