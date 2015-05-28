package su.jfdev.cubes.plugins.kitbox;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import su.jfdev.cubes.plugins.kitbox.cmd.KitBoxAlternativeCommand;
import su.jfdev.cubes.plugins.kitbox.cmd.KitBoxCommand;
import su.jfdev.cubes.plugins.kitbox.cmd.Permission;
import su.jfdev.cubes.plugins.kitbox.listeners.KitBoxListener;
import su.jfdev.cubes.plugins.kitbox.yaml.Config;

import java.io.File;
import java.io.IOException;

/**
 * Created by Jamefrus on 08.05.2015.
 */
public class Main extends JavaPlugin {

    public static final BoxInfo BOX_INFO = new BoxInfo();
    public final static boolean PEX_ENABLED = Bukkit.getPluginManager().getPlugin("PermissionsEx") != null;
    private static Main instance;
    private YamlConfiguration boxYaml;
    private CommandExecutor kitBoxCommandExecutor;

    public static synchronized Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        Main.instance = this;
        Config.verifyConfigurationFile();
        Config.initConfiguration();
        getServer().getPluginManager().registerEvents(new KitBoxListener(this), this);
        this.getCommand("kitbox").setExecutor(new KitBoxCommand(this));
        for (Permission perm : Permission.values()) {
            if (perm.getAltCmd() == null) {
                continue;
            } else {
                this.getCommand(perm.getAltCmd()).setExecutor(new KitBoxAlternativeCommand());
            }
        }
    }

    @Override
    public void onDisable() {
        saveBoxYaml();
    }

    public void reload() {
        this.reloadConfig();
        Config.verifyConfigurationFile();
        System.out.println("Main.reload");
        boxYaml = loadBoxYaml();
    }

    private YamlConfiguration loadBoxYaml() {
        YamlConfiguration yaml = null;
        try {
            File yamlFile = new File(getDataFolder(), Config.YAML_NAME.getString());
            if (!yamlFile.exists()) {
                yamlFile.createNewFile();
            }
            yaml = YamlConfiguration.loadConfiguration(yamlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return yaml;
    }

    public YamlConfiguration getBoxYaml() {
        if (boxYaml == null) boxYaml = loadBoxYaml();
        return boxYaml;
    }

    public boolean saveBoxYaml() {
        try {
            if (boxYaml != null)
                getBoxYaml().save(new File(getDataFolder(), Config.YAML_NAME.getString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


}
