package su.jfdev.cubes.plugins.kitbox;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 * Created by Jamefrus on 10.05.2015.
 */

public enum Permissions {
    Reload("jf.kitbox.reload"),
    Help("jf.kitbox.help"),
    Create("jf.kitbox.create"),
    Duplicate("jf.kitbox.create.duplicate"),
    Save("jf.kitbox.save"),
    Remove("jf.kitbox.remove"),
    SetOwner("jf.kitbox.setowner"),
    KitBox("jf.kitbox");

    private String permission;

    Permissions(String perm) {
        this.permission = perm;
    }

    public static boolean has(CommandSender player, Permissions perm) {
        if (player instanceof ConsoleCommandSender) {
            return true;
        } else if (player.isOp()) {
            return true;
        } else if (Main.PEX_ENABLED) {
            return PermissionsEx.getUser((Player)player).has(perm.permission);
        } else {
            return player.hasPermission(perm.permission);
        }
    }
}
