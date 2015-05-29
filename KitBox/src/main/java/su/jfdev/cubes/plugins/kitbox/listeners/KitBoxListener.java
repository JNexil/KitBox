package su.jfdev.cubes.plugins.kitbox.listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import su.jfdev.cubes.plugins.kitbox.Main;
import su.jfdev.cubes.plugins.kitbox.OPermission;
import su.jfdev.cubes.plugins.kitbox.yaml.YamlControl;

/**
 * Created by Jamefrus on 08.05.2015.
 */

public class KitBoxListener implements Listener {

    private Plugin plugin;

    public KitBoxListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerAutoSave(WorldSaveEvent ev) {
        Main.getInstance().saveBoxYaml();
    }


    @EventHandler
    public void onBlockDestroy(BlockBreakEvent ev) {
        Location loc = ev.getBlock().getLocation();
        if (!Main.getInstance().getBoxYaml().contains(YamlControl.convertLocationToPath(loc) + ".content")) {
            return;
        } else {
            YamlControl.removeYamlInventory(loc);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent ev) {
        String player = ev.getPlayer().getName();
        if (!Main.BOX_INFO.getBoxMap().containsKey(player) || !Main.BOX_INFO.getLocationMap().containsKey(player))
            return;
        else {
            YamlControl.saveYamlInventory(ev.getPlayer().getName(), Main.BOX_INFO.getInventory(player), Main.getInstance().getBoxYaml().getConfigurationSection(YamlControl.convertLocationToPath(Main.BOX_INFO.getLocation(player))));
            Main.BOX_INFO.getBoxMap().remove(player);
            Main.BOX_INFO.getLocationMap().remove(player);
        }
    }

    @EventHandler
    public void onBlockUsed(PlayerInteractEvent ev) {
        if (!OPermission.Use.has(ev.getPlayer())) return;
        if (ev.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Location loc = ev.getClickedBlock().getLocation();
        if (!Main.getInstance().getBoxYaml().contains(YamlControl.convertLocationToPath(loc) + ".content")) return;
        Inventory inventory = YamlControl.getInventoryFromYaml(loc, ev.getPlayer().getName());
        ev.getPlayer().openInventory(inventory);
        Main.BOX_INFO.putLocationAndInventory(ev.getPlayer().getName(), loc, inventory);
        ev.setCancelled(true);
    }
}
