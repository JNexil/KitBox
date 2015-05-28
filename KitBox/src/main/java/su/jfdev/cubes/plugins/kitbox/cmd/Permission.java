package su.jfdev.cubes.plugins.kitbox.cmd;

import com.sun.istack.internal.NotNull;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import su.jfdev.cubes.plugins.kitbox.Main;

/**
 * Created by Jamefrus on 10.05.2015.
 */

public enum Permission {
    Reload("jf.kitbox.reload", "reload", "kbreload"),
    Help("jf.kitbox.help", "help", "kbhelp"),
    Create("jf.kitbox.create", "create", "kbcreate"),
    Duplicate("jf.kitbox.create.duplicate"),
    Save("jf.kitbox.save", "save", "kbsave"),
    Remove("jf.kitbox.remove", "remove", "kbremove"),
    SetOwner("jf.kitbox.setowner", "setowner", "kbreown"),
    SetName("jf.kitbox.setname", "setname", "kbrename"),
    KitBox("jf.kitbox"),
    SetSize("jf.kitbox.setSize", "setsize", "kbresize");

    private String permission;
    private String altCMD;
    private String cmd;

    Permission(String perm) {
        this.permission = perm;
    }

    Permission(String perm, String cmd) {
        this.permission = perm;
        this.cmd = cmd;
    }

    Permission(String permission, String cmd, String altCMD) {
        this.permission = permission;
        this.altCMD = altCMD;
        this.cmd = cmd;
    }

    public static Permission search(@NotNull String altCMD) {
        for (Permission perm : Permission.values()) {
            if (perm.getAltCmd().equalsIgnoreCase(altCMD)) {
                return perm;
            }
        }
        return null;
    }

    public static boolean has(CommandSender player, Permission perm) {
        if (player instanceof ConsoleCommandSender) {
            return true;
        } else if (player.isOp()) {
            return true;
        } else if (Main.PEX_ENABLED) {
            return PermissionsEx.getUser((Player) player).has(perm.permission);
        } else {
            return player.hasPermission(perm.permission);
        }
    }

    public String getCmd() {
        return cmd;
    }

    public String getAltCmd() {
        return altCMD;
    }
}
