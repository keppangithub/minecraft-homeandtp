# HomeAndTP

A lightweight and feature-rich Minecraft plugin for managing home locations and teleportation.

## Features

- **Multiple Homes**: Set up to 5 home locations per player
- **Smart Home Management**: Automatically updates existing homes when using the same name
- **Persistent Storage**: Homes are saved in YAML format and persist across server restarts
- **World Validation**: Checks if worlds exist before teleporting
- **User-Friendly Messages**: Clear feedback for all actions
- **Teleport Commands**: Quick teleportation to saved homes
- **Configurable**: Easy to extend and customize

## Requirements

- **Minecraft Version**: 1.20.4 (Paper/Spigot)
- **Java Version**: 21 or higher
- **Server Software**: Paper, Spigot, or compatible forks

## Installation

1. Download the latest `HomeAndTP-x.x.x.jar` from the [Releases](../../releases) page
2. Place the JAR file in your server's `plugins/` folder
3. Restart your server or use `/reload` (restart recommended)
4. The plugin will automatically create its data folder

## Commands

### Home Commands

| Command | Description | Usage | Permission |
|---------|-------------|-------|------------|
| `/sethome` | Set your default home location | `/sethome` | `homeandtp.sethome` |
| `/sethome <name>` | Set a named home location | `/sethome myhouse` | `homeandtp.sethome` |
| `/home` | Teleport to your default home | `/home` | `homeandtp.home` |
| `/home <name>` | Teleport to a named home | `/home myhouse` | `homeandtp.home` |
| `/delhome <name>` | Delete a home location | `/delhome myhouse` | `homeandtp.delhome` |
| `/homes` | List all your homes | `/homes` | `homeandtp.homes` |

### Admin Commands (Planned)

| Command | Description | Usage | Permission |
|---------|-------------|-------|------------|
| `/tp <player>` | Teleport to a player | `/tp Steve` | `homeandtp.tp` |
| `/tphere <player>` | Teleport a player to you | `/tphere Steve` | `homeandtp.tphere` |

## Permissions

Default permissions are configured in `plugin.yml`:

```yaml
homeandtp.sethome    # Set home locations (default: true)
homeandtp.home       # Teleport to homes (default: true)
homeandtp.delhome    # Delete homes (default: true)
homeandtp.homes      # List homes (default: true)
homeandtp.tp         # Teleport to players (default: op)
homeandtp.tphere     # Teleport players to you (default: op)
```

## Features in Detail

### Multiple Homes System
- Players can set up to **5 homes** by default
- Each home has a unique name (3-16 characters, letters, numbers, and underscores only)
- Homes can be updated by using `/sethome` with an existing home name
- Default home name is "home" when no name is specified

### Smart Validation
- **Home name validation**: Ensures names are valid before creating
- **World validation**: Checks if the world exists before teleporting
- **Duplicate handling**: Updates existing homes instead of creating duplicates
- **Limit checking**: Prevents players from exceeding the home limit

### Data Storage
- Homes are stored in YAML files per player: `plugins/HomeAndTP/<player-uuid>.yml`
- Each home stores:
  - World name
  - X, Y, Z coordinates
  - Yaw and pitch (player rotation)
- Data persists across server restarts

## Configuration

Currently, the plugin uses default settings. Future versions will include a `config.yml` for:
- Maximum homes per player
- Cooldown timers
- Teleportation delays
- Custom messages

## Building from Source

### Prerequisites
- Java 21 or higher
- Maven 3.6+
- Git

### Build Steps

```bash
# Clone the repository
git clone https://github.com/yourusername/minecraft-homeandtp.git
cd minecraft-homeandtp

# Build with Maven
mvn clean package

# The compiled JAR will be in target/HomeAndTP-1.0.0.jar
```

### Development Setup

```bash
# Install dependencies
mvn clean install

# Run tests (when available)
mvn test

# Build without tests
mvn package -DskipTests
```

## Development

### Project Structure

```
minecraft-homeandtp/
├── src/main/java/com/homeandtp/
│   ├── HomeAndTPPlugin.java           # Main plugin class
│   └── plugin/
│       ├── command/                   # Command executors
│       │   ├── HomeCommand.java
│       │   ├── setHomeCommand.java
│       │   └── CommandRegistrar.java
│       ├── service/                   # Business logic
│       │   ├── HomeService.java
│       │   └── HomeResult.java
│       ├── data/                      # Data persistence
│       │   ├── HomeRepository.java
│       │   └── YamlHomeRepository.java
│       ├── model/                     # Data models
│       │   └── Home.java
│       └── util/                      # Utilities
│           └── LocationUnit.java
├── src/main/resources/
│   └── plugin.yml                     # Plugin configuration
├── pom.xml                            # Maven configuration
└── README.md                          # This file
```

### Code Architecture

The plugin follows a **layered architecture**:

1. **Command Layer**: Handles user input and validation
2. **Service Layer**: Contains business logic and rules
3. **Repository Layer**: Manages data persistence
4. **Model Layer**: Defines data structures

### Adding New Features

Example: Adding a cooldown system

```java
// 1. Add to HomeService.java
private final Map<UUID, Long> cooldowns = new HashMap<>();

public HomeResult teleportHome(UUID playerUuid, String homeName) {
    // Check cooldown
    if (cooldowns.containsKey(playerUuid)) {
        long timeLeft = cooldowns.get(playerUuid) - System.currentTimeMillis();
        if (timeLeft > 0) {
            return HomeResult.failure("Cooldown: " + (timeLeft / 1000) + "s");
        }
    }

    // Set cooldown
    cooldowns.put(playerUuid, System.currentTimeMillis() + 5000); // 5 seconds

    // Rest of teleport logic...
}
```

## API Usage

Other plugins can use HomeAndTP's API:

```java
// Get the plugin instance
HomeAndTPPlugin plugin = (HomeAndTPPlugin) Bukkit.getPluginManager().getPlugin("HomeAndTP");

// Access the service (you'll need to expose this via a public getter)
HomeService homeService = plugin.getHomeService();

// Get a player's home
Optional<Home> home = homeService.getHome(playerUuid, "home");

// List all homes
List<Home> homes = homeService.listHomes(playerUuid);
```

## Testing

### Manual Testing Setup

A test server is included for development:

```bash
# Create test server (automated script available)
./setup-test-server.sh

# Or manually:
mkdir test-server
cd test-server
wget https://api.papermc.io/v2/projects/paper/versions/1.20.4/builds/497/downloads/paper-1.20.4-497.jar -O paper.jar
echo "eula=true" > eula.txt
mkdir plugins
cp ../target/HomeAndTP-1.0.0.jar plugins/
java -Xmx2G -Xms2G -jar paper.jar --nogui
```

### Test Scenarios

1. **Basic Functionality**
   - Set a home: `/sethome`
   - Teleport: `/home`
   - Verify coordinates match

2. **Multiple Homes**
   - Create 5 different homes
   - Try to create a 6th (should fail)
   - Teleport to each one

3. **Home Updates**
   - Create a home at location A
   - Move to location B
   - Use same home name
   - Verify location updated

4. **Error Handling**
   - Try to teleport to non-existent home
   - Use invalid home names
   - Delete a home and try to teleport to it

## Roadmap

### Version 1.1.0
- [ ] Delete home command (`/delhome`)
- [ ] List homes command (`/homes`)
- [ ] Configuration file support
- [ ] Teleportation cooldowns

### Version 1.2.0
- [ ] Player-to-player teleport commands
- [ ] Teleport requests and acceptance
- [ ] Teleportation costs (economy integration)

### Version 2.0.0
- [ ] GUI for home management
- [ ] Home icons and descriptions
- [ ] Shared homes (for teams/friends)
- [ ] Warp points (server-wide teleports)

## Contributing

Contributions are welcome! Please follow these guidelines:

1. **Fork the repository**
2. **Create a feature branch**: `git checkout -b feature/amazing-feature`
3. **Commit your changes**: `git commit -m 'Add amazing feature'`
4. **Push to the branch**: `git push origin feature/amazing-feature`
5. **Open a Pull Request**

### Code Style
- Follow Java naming conventions
- Use meaningful variable names
- Add comments for complex logic
- Keep methods focused and small
- Write clean, readable code

## Troubleshooting

### Plugin doesn't load
- Check server logs for errors
- Verify Java version (must be 21+)
- Ensure plugin.yml main class is correct
- Check for conflicting plugins

### Homes not saving
- Check folder permissions for `plugins/HomeAndTP/`
- Look for errors in server logs
- Verify the data folder was created

### Teleportation not working
- Ensure the world exists
- Check player has permission
- Verify home was actually saved

## Support

- **Issues**: [GitHub Issues](../../issues)
- **Discussions**: [GitHub Discussions](../../discussions)
- **Documentation**: [Wiki](../../wiki)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Credits

**Author**: xk3ppan
**Version**: 1.0.0
**Minecraft Version**: 1.20.4
**API**: Paper/Spigot

## Changelog

### [1.0.0] - 2025-10-26
#### Added
- Initial release
- Set home command with multiple homes support (up to 5)
- Teleport to home command
- YAML-based data persistence
- Smart home name validation
- World existence checking
- Home update functionality (overwrite existing homes)
- Detailed user feedback messages

---

**Made with dedication for the Minecraft community**
