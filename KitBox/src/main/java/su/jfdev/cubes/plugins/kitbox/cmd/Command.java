package su.jfdev.cubes.plugins.kitbox.cmd;

import com.sun.istack.internal.NotNull;
import org.bukkit.command.CommandSender;
import su.jfdev.cubes.plugins.kitbox.Main;
import su.jfdev.cubes.plugins.kitbox.OPermission;
import su.jfdev.cubes.plugins.kitbox.lang.Localization;

import static su.jfdev.cubes.plugins.kitbox.OPermission.hasPermission;

/**
 * Created by Jamefrus on 10.05.2015.
 */

public enum Command {
    Reload("kbreload"),
    Help("kbhelp"),
    Create("kbcreate"),
    Save("kbsave"),
    Remove("kbremove"),
    SetOwner("kbreown"),
    SetName("kbrename"),
    SetSize("kbresize");

    private String altCMD;

    Command(String altCMD) {
        this.altCMD = altCMD;
    }

    public static Command search(@NotNull String altCMD) {
        for (Command command : Command.values()) {
            if (command.getAltCMD().equalsIgnoreCase(altCMD)) {
                return command;
            }
        }
        return null;
    }

    public static boolean has(CommandSender player, Command command) {
        return command.has(player);
    }

    public String getCommand() {
        return this.name().toLowerCase();
    }

    public String getAltCMD() {
        return altCMD;
    }

    public String getPermission() {
        return OPermission.KitBox.getPermission() + this.name().toLowerCase();
    }

    public boolean has(CommandSender player) {
        return hasPermission(player, this.getPermission());
    }

    public String getLocalizedDescription(Localization localization) {
        return localization.getCommandText(this, "help");
    }

    public String getLocalizedDescription() {
        return getLocalizedDescription(Main.getInstance().getLocalization());
    }
}
