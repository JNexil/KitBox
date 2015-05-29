package su.jfdev.cubes.plugins.kitbox.lang;


import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import su.jfdev.cubes.plugins.kitbox.Main;
import su.jfdev.cubes.plugins.kitbox.cmd.Command;
import su.jfdev.cubes.plugins.kitbox.util.UtilString;
import su.jfdev.cubes.plugins.kitbox.util.UtilWeb;
import su.jfdev.cubes.plugins.kitbox.yaml.Config;

import java.io.File;

/**
 * Created by Jamefrus on 20.05.2015.
 */

public class Localization {

    private static final String LANG_FOLDER = UtilString.buildPath("lang", Main.getInstance().getDescription().getVersion());
    private ConfigurationSection langFile;

    public Localization(String languageFile) {
        File f = new File(Main.getInstance().getDataFolder() + LANG_FOLDER, languageFile);
        if (f.exists() && f.length() > 0) {
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f);
            YamlConfiguration downloadYaml = UtilWeb.downloadYaml(LANG_FOLDER + languageFile);
            yamlConfiguration.setDefaults(downloadYaml);
            langFile = yamlConfiguration;
        } else {
            this.langFile = UtilWeb.downloadYaml(LANG_FOLDER + languageFile);
        }
    }

    public static String getLocalizedCommandText(Command command, String var) {
        return Main.getInstance().getLocalization().getCommandText(command, var);
    }

    public String getCommandText(Command command, String var) {
        String path = UtilString.buildString(Config.LANG_PREFIX.getString(), "commands.", command.getCommand(), ".", var);
        return getLocalizedText(path);
    }

    public String getLocalizedText(String path) {
        String string = this.langFile.getString(path);
        if (string == null) {
            return "null";
        } else {
            return string;
        }
    }
}
