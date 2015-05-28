package su.jfdev.cubes.plugins.kitbox;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Jamefrus on 09.05.2015.
 */

public class YamlMap {

    public static String convertLocationToPath(Location loc) {
        final char P = '.'; // Character Point "."
        final char X = 'x'; // Character small X "x"
        StringBuilder location = new StringBuilder();
        location
                .append(loc.getWorld().getName()) //WorldName
                .append(P) // .
                        /// world.
                .append(loc.getChunk().getX()) // Chunk X
                .append(X) // x
                .append(loc.getChunk().getZ()) // Chunk Z
                .append(P)
                        /// world.5x7.
                .append(loc.getBlockX()) // Block X
                .append(X) // x
                .append(loc.getBlockY()) // Block Y
                .append(X) // x
                .append(loc.getBlockZ()); // Block Z
        /// world.5x7.4x1x2
        return location.toString();
    }

    public static Inventory getInventoryFromYaml(Location loc, String player) {

        YamlConfiguration boxYaml = Main.getInstance().getBoxYaml();
        ConfigurationSection section = boxYaml.getConfigurationSection(YamlMap.convertLocationToPath(loc));
        Inventory inventory = Bukkit.createInventory(null, section.getInt("size", Defaults.INV_SIZE), section.getString("title", Defaults.INV_NAME));
        if (isEmptyInventory(section)) return inventory;
        if (!section.contains("content." + player)) {
            if(section.getBoolean("duplicate", false))
            cloneInventory(section.getString("owner"), player, section, section.getInt("size", Defaults.INV_SIZE));
        }
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack is = section.getItemStack("content." + player + ".slot" + i);
            if (is != null && is instanceof ItemStack) {
                inventory.setItem(i, (ItemStack) is);
            }
        }
        return inventory;
    }

    private static boolean isEmptyInventory(ConfigurationSection section) {
        boolean content = section.getBoolean("content.empty", false);
        return content;
    }

    public static void cloneInventory(String path, String pathTo, ConfigurationSection section, int size) {

        for (int i = 0; i < size; i++) {
            Object is = section.get("content." + path + ".slot" + i);
            if (is != null) {
                section.set("content."  + pathTo + ".slot" + i, is);
            }
        }

    }

    public static void createYamlInventory(Location loc, String owner, Inventory inventory, boolean duplicate) {
        YamlConfiguration boxYaml = Main.getInstance().getBoxYaml();
        ConfigurationSection section = boxYaml.createSection(YamlMap.convertLocationToPath(loc));
        section.createSection("content");
        section.set("owner", owner);
        section.set("size", inventory.getSize());
        section.set("title", inventory.getName());
        section.set("duplicate", duplicate);
        saveYamlInventory(owner, inventory, section);
    }

    public static void saveYamlInventory(String player, Inventory inventory, ConfigurationSection section) {
        boolean empty = true;
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack is = inventory.getItem(i);
            if (is != null) {
                empty = false;
                section.createSection("content." + player + ".slot" + i);
                section.set("content." + player + ".slot" + i, is);
            } else {
                section.createSection("content." + player + ".slot" + i);
                section.set("content." + player + ".slot" + i, "empty");
            }
        }
        section.set("content.empty", empty);
    }

    public static void removeYamlInventory(Location loc){
        YamlConfiguration boxYaml = Main.getInstance().getBoxYaml();
        boxYaml.createSection(convertLocationToPath(loc));
    }

    public static void renameOwner(String newName,Location loc){
        YamlConfiguration yamlConfiguration = Main.getInstance().getBoxYaml();
        ConfigurationSection cfgSection = yamlConfiguration.getConfigurationSection(convertLocationToPath(loc));
        String oldName = cfgSection.getString("owner");
        cfgSection.set("owner",newName);
        cloneInventory(oldName,newName,cfgSection,cfgSection.getInt("size"));
    }
}
