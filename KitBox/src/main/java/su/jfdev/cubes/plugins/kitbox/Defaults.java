package su.jfdev.cubes.plugins.kitbox;

/**
 * Created by Jamefrus on 11.05.2015.
 */

public class Defaults {

    public static final String YAML_NAME = Main.getInstance().getConfig().getString("defaults.boxYaml.name","boxdb.yml");
    public static final String INV_NAME = Main.getInstance().getConfig().getString("defaults.inventory.name","Box");
    public static final String INV_OWNER = Main.getInstance().getConfig().getString("defaults.inventory.owner","SERVER");
    public static final int INV_SIZE = Main.getInstance().getConfig().getInt("defaults.inventory.size",9);
    public static final boolean INV_DUPLICATE = Main.getInstance().getConfig().getBoolean("defaults.inventory.duplicate",false);

}
