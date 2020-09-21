package es.jotero.skybags.utils;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;

public class ConfigurationManager {

    private static ConfigurationManager instance = new ConfigurationManager();

    public static ConfigurationManager getInstance() {
        return instance;
    }

    private ConfigurationLoader<CommentedConfigurationNode> configLoader;
    private CommentedConfigurationNode config;

    public void setup(File configFile, ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        this.configLoader = configLoader;
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                loadConfig();
                config.getNode("bag_open_message").setComment("The message when a bag is opened.").setValue("&9[SC] The bag was opened.");
                config.getNode("bag_spectate_message").setComment("The message when a bag is spected.").setValue("&9[SC] The bag was opened.");
                saveConfig();
            }

            catch (Exception e) {
                e.printStackTrace();
            }
        }

        else {
            loadConfig();
        }
    }

    public CommentedConfigurationNode getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            configLoader.save(config);
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        try {
            config = configLoader.load();
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
