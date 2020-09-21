package es.jotero.skybags;

import com.google.inject.Inject;
import es.jotero.skybags.command.CommandManager;
import es.jotero.skybags.utils.ConfigurationManager;
import es.jotero.skybags.utils.SkybagsLogger;
import es.jotero.skybags.utils.TextColors;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Plugin(
        id = SkybagsPluginData.PLUGIN_ID,
        name = SkybagsPluginData.PLUGIN_NAME,
        description = SkybagsPluginData.PLUGIN_DESCRIPTION,
        url = SkybagsPluginData.PLUGIN_URL,
        authors = {SkybagsPluginData.PLUGIN_AUTHOR}
)
public class Skybags {

    private static Skybags skybags;
    private Path configDirPath;
    private Path bagsDirPath;
    private SkybagsLogger skybagsLogger;

    @Inject
    private Game game;

    @Inject
    public Skybags(@ConfigDir(sharedRoot = true) Path path) {
        configDirPath = path.resolve("skybags");
        bagsDirPath = Paths.get(getConfigPath().toString(), "bags");
        this.skybagsLogger = new SkybagsLogger(TextColors.get(TextColors.Colors.GREEN, 1, "Skybags"));
    }

    @Listener
    public void onGamePreInit(final GamePreInitializationEvent event) {
        skybagsLogger.info("Initializing plugin...");
        skybags = this;
    }

    @Listener
    public void onGameInit(final GameInitializationEvent event) {

        try {
            if (!Files.exists(configDirPath)) {
                Files.createDirectories(configDirPath);
            }
        } catch (IOException e) {
            skybagsLogger.error("Error on creating plugin directory: {0}", e);
        }

        skybagsLogger.info("Plugin initialized successfully!");
    }

    @Listener
    public void onServerStarting(GameStartingServerEvent event) {
        getLogger().info("Plugin commands being registered...");
        CommandManager cmdManager = new CommandManager(game, this);
    }

    public Path getConfigPath() {
        return configDirPath;
    }

    public SkybagsLogger getLogger() {
        return skybagsLogger;
    }

    public static Skybags getInstance() {
        return skybags;
    }

    public Path getBagsDirPath() {
        return bagsDirPath;
    }
}
