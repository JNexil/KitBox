package su.jfdev.cubes.plugins.kitbox.yaml;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import su.jfdev.cubes.plugins.kitbox.Main;

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
    INV_DUPLICATE("defaults.inventory.duplicate", false),
    HELP_SEPARATION("defaults.help.separation", false),
    LANG_PREFIX("defaults.lang.prefix", "kitbox."),
    WEB_SERVER("defaults.web.server", "http://jf.zz.vc/kitbox/"),
    YA_DISK("defaults.web.yadisk", true),
    YA_DISK_DIR("defaults.web.yadiskdir", "https://yadi.sk/d/0bNoens5gjmnH"),
    LANGUAGE("defaults.lang.langfile", "ru_RU.lang");


    private boolean empty;
    private Object value;
    private String path;
    private Object def;
    Config(String path, Object def) {
        this.path = path;
        this.def = def;
    }

    public static void saveConfiguration() {
        FileConfiguration conf = Main.getInstance().getConfig();
        for (Config path : Config.values()) {
            if ((path.getValue()) != null) {
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
        if (!file.exists() || file.length() == 0) {
            try {
                file.createNewFile();
                initConfiguration();
                saveConfiguration();
                Main.getInstance().getConfig().save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Main.getInstance().reloadConfig();
        } else {
            Config.initConfiguration();
        }
    }

    public String getPath() {
        return path;
    }

    public Object getDef() {
        return def;
    }

    public Object getValue() {
        if (value == null) return getDef();
        else return value;
    }

    public void setValue(Object value) {
        this.value = value;
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

    public boolean isEmpty() {
        return empty;
    }
}
