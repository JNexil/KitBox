package su.jfdev.cubes.plugins.kitbox;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 * Created by Jamefrus on 20.05.2015.
 */

public enum OPermission {

    Duplicate,
    Use,
    KitBox("jf.kitbox");

    private String permission;

    OPermission() {
    }

    OPermission(String permission) {
        this.permission = permission;
    }

    public static boolean hasPermission(Permissible player, String permission) {
        if (player instanceof ConsoleCommandSender) {
            return true;
        } else if (player.isOp()) {
            return true;
        } else if (Main.PEX_ENABLED) {
            return PermissionsEx.getUser((Player) player).has(permission);
        } else {
            return player.hasPermission(permission);
        }
    }

    public String getPermission() {
        if (permission != null) return permission;
        return KitBox.getPermission() + this.name().toLowerCase();
    }

    public boolean has(CommandSender player) {
        return hasPermission(player, this.getPermission());
    }
}
