package su.jfdev.cubes.plugins.kitbox;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jamefrus on 08.05.2015.
 */

public class BoxInfo {

    private Map<String, Inventory> boxMap;
    private Map<String, Location> locationMap;

    public BoxInfo() {
    }

    public Inventory getInventory(String player) {
        return boxMap.get(player);
    }

    public Location getLocation(String player) {
        return locationMap.get(player);
    }

    public Map<String, Inventory> getBoxMap() {
        if (boxMap == null) {
            boxMap = new HashMap<>();
        }
        return boxMap;
    }

    public void setBoxMap(Map<String, Inventory> boxMap) {
        this.boxMap = boxMap;
    }

    public Map<String, Location> getLocationMap() {
        if (locationMap == null) {
            locationMap = new HashMap<>();
        }
        return locationMap;
    }

    public void setLocationMap(Map<String, Location> locationMap) {
        this.locationMap = locationMap;
    }

    public void putLocationAndInventory(String player, Location location, Inventory inventory) {
        this.getLocationMap().put(player, location);
        this.getBoxMap().put(player, inventory);
    }

}
