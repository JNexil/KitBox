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

        ConfigurationSection section = getContentSection(loc);
        Inventory inventory = Bukkit.createInventory(null, section.getInt("size", Config.INV_SIZE.getInteger()), section.getString("title", Config.INV_NAME.getString()));
        if (isEmptyInventory(section)) return inventory;
        if (!section.contains("content." + player)) {
            if (section.getBoolean("duplicate", false))
                cloneInventory(section.getString("owner"), player, section, section.getInt("size", Config.INV_SIZE.getInteger()));
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
                section.set("content." + pathTo + ".slot" + i, is);
            }
        }

    }

    public static void createYamlInventory(Location loc, String owner, Inventory inventory, boolean duplicate) {
        ConfigurationSection section = getContentSection(loc);
        section.createSection("content");
        section.set("owner", owner);
        section.set("size", inventory.getSize());
        section.set("title", inventory.getName());
        section.set("duplicate", duplicate);
        saveYamlInventory(owner, inventory, section);
    }

    public static void saveYamlInventory(String player, Inventory inventory, ConfigurationSection section) {
        if (player == null) {
            player = section.getString("owner");
        }
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

    public static void removeYamlInventory(Location loc) {
        YamlConfiguration boxYaml = Main.getInstance().getBoxYaml();
        boxYaml.createSection(convertLocationToPath(loc));
    }

    public static void renameOwner(String newName, Location loc) {
        ConfigurationSection cfgSection = getContentSection(loc);
        String oldName = cfgSection.getString("owner");
        cfgSection.set("owner", newName);
        cloneInventory(oldName, newName, cfgSection, cfgSection.getInt("size"));
    }

    private static ConfigurationSection getContentSection(Location loc) {
        YamlConfiguration yamlConfiguration = Main.getInstance().getBoxYaml();
        String s = convertLocationToPath(loc);
        ConfigurationSection configurationSection = yamlConfiguration.getConfigurationSection(s);
        if (configurationSection == null) configurationSection = yamlConfiguration.createSection(s);
        return configurationSection;
    }

    public static void rename(String newName, Location loc) {
        ConfigurationSection contentSection = getContentSection(loc);
        contentSection.set("title", newName);
    }

    public static int getPossibleSize(Inventory inv) {
        for (int i = 1; i <= (inv.getSize() / 9); i++) {
            int lastSlotInLine = i * 9;
            if (lastSlotInLine <= inv.getSize()) {
                try {
                    if (inv.getItem(lastSlotInLine) == null){
                        return lastSlotInLine;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    return lastSlotInLine;
                }
            }
        }
        return 54;
    }

    public static void setSize(int size, Location loc) throws IllegalArgumentException {
        ConfigurationSection section = getContentSection(loc);
        int boxSize = section.getInt("size");
        if (boxSize == size) {
            throw new IllegalArgumentException("error_no_changes");
        } else if (boxSize > size && size != -1) {
            setSize(size, loc, false);
            return;
        }
        if (isEmptyInventory(section)) {
            setSize(size, loc, false);
            return;
        }
        Inventory sortedInventory = sortInventory(getInventoryFromYaml(loc, section.getString("owner")));
        saveYamlInventory(section.getString("owner"), sortedInventory, section);
        if (size == -1) {
            size = getPossibleSize(sortedInventory);
            if (boxSize == size) {
                throw new IllegalArgumentException("error_no_changes");
            }
            setSize(size, loc, false);
            return;
        }
        int minSize = getPossibleSize(sortedInventory);
        if (size < minSize) {
            throw new IllegalArgumentException("error_size_" + minSize);
        } else setSize(size, loc, false);
    }


    public static void setSize(int size, Location loc, boolean cut) throws IllegalArgumentException {
        ConfigurationSection section = getContentSection(loc);
        int oldSize = section.getInt("size");
        if(oldSize == size) throw new IllegalArgumentException("error_no_changes");
        Inventory inventoryFromYaml = getInventoryFromYaml(loc, section.getString("owner"));
        if (cut) sortInventory(inventoryFromYaml);
        Inventory inventory = Bukkit.createInventory(null, size, inventoryFromYaml.getTitle());
        for (int i = 0; i < size; i++) {
            if (i < oldSize) inventory.setItem(i, inventoryFromYaml.getItem(i));
        }
        createYamlInventory(loc, section.getString("owner", Config.INV_OWNER.getString()), inventory, section.getBoolean("duplicate", Config.INV_DUPLICATE.getBoolean()));
    }

    public static Inventory sortInventory(Inventory inventory) {
        Inventory inv = Bukkit.createInventory(null, inventory.getSize(), inventory.getTitle());
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null) inv.addItem(itemStack);
        }
        return inv;
    }
}
