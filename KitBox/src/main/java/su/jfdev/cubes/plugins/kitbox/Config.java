package su.jfdev.cubes.plugins.kitbox;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

/**
 * Created by Jamefrus on 11.05.2015.
 */

public enum Config {

    YAML_NAME("defaults.boxYaml.name", "boxdb.yml"),
    INV_NAME("defaults.inventory.name", "Box"),
    INV_OWNER("defaults.inventory.owner", "SERVER"),
    INV_SIZE("defaults.inventory.size", 9),
    INV_DUPLICATE("defaults.inventory.duplicate", false);


    Config(String path, Object def) {
        this.path = path;
        this.def = def;
    }

    private boolean empty;
    private Object value;
    private String path;
    private Object def;


    public String getPath() {
        return path;
    }

    public Object getDef() {
        return def;
    }

    public Object getValue() {
        if(value == null) return getDef();
        else return value;
    }

    public int getInteger() {
        return (Integer) this.getValue();
    }

    public String getString() {
        return (String) this.getValue();
    }

    public boolean getBoolean() {
        return (Boolean) this.getValue();
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public static void saveConfiguration() {
        FileConfiguration conf = Main.getInstance().getConfig();
        for (Config path : Config.values()) {
            if((path.getValue()) != null){
                conf.set(path.getPath(), path.getValue());

            } else {
                conf.set(path.getPath(), path.getDef());
            }
        }
    }

    public static void initConfiguration() {
        FileConfiguration conf = Main.getInstance().getConfig();
        for (Config path : Config.values()) {
            path.setValue(conf.get(path.getPath(), path.getDef()));
        }
    }

    public static void verifyConfigurationFile() {
        Plugin plugin = Main.getInstance();
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        File file = new File(plugin.getDataFolder(), "config.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                initConfiguration();
                Main.getInstance().getConfig().save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Config.initConfiguration();
        }
    }

    public boolean isEmpty() {
        return empty;
    }
}
