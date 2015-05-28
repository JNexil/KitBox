package su.jfdev.cubes.plugins.kitbox.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import su.jfdev.cubes.plugins.kitbox.cmd.Permission;
import su.jfdev.cubes.plugins.kitbox.yaml.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jamefrus on 13.05.2015.
 */

public class HelpBuilder {

    private HelpBuilder() {
    }

    public static HelpBuilder createHelpBuilder() {
        return new HelpBuilder();
    }

    public static String createHelp(CommandSender cm) {
        StringBuilder sb = new StringBuilder();
        List<String> stringList = new ArrayList<>();
        stringList.add(ChatColor.DARK_GREEN + "[KitBox] Доступны команды:");
        if (Config.HELP_SEPARATION.getBoolean()) {
            constructHelp(cm, stringList, (byte) 0);
            constructHelp(cm, stringList, (byte) 1);
        } else {
            constructHelp(cm, stringList, (byte) 2);
        }
        if (stringList.size() > 1) {
            return Util.buildList(stringList).toString();
        }

        return "Вам не доступны команды KitBox";
    }

    private static void constructHelp(CommandSender cm, List<String> stringList, byte alt) {
        for (Command command : Command.values()) {
            if (Permission.has(cm, command.permission)) {
                StringBuilder commandbuilder = new StringBuilder();
                if (alt != 1)
                    commandbuilder.append(ChatColor.GOLD).append("• /kitbox ").append(command.permission.getCmd());
                else commandbuilder.append(ChatColor.GOLD).append("• /").append(command.permission.getAltCmd());
                if (alt == 2)
                    commandbuilder.append(ChatColor.YELLOW).append(" or /").append(command.permission.getAltCmd());

                if (command.args.length > 0) {
                    commandbuilder.append(ChatColor.GRAY);
                    for (String arg : command.args) {
                        if (arg.contains("duplicate")) {
                            if (!Permission.has(cm, Permission.Duplicate)) {
                                continue;
                            }
                        }
                        commandbuilder.append(" [").append(arg).append("]");
                    }
                }
                commandbuilder.append(ChatColor.WHITE).append(" - " + getLocalizedValue(command.permission.getCmd()));
                stringList.add(commandbuilder.toString());

            }
        }
    }

    public static String getLocalizedValue(String command) {
        return "эта команда имеет значение, однако пока-что вам этого не дано понять...";
    }

    private enum Command {
        Reload(Permission.Reload),
        Create(Permission.Create, "duplicate: true(1) or false(0)", "size: min 9, max 54", "title: String"),
        Save(Permission.Save),
        Remove(Permission.Remove),
        SetOwner(Permission.SetOwner, "name: String"),
        SetName(Permission.SetName, "name:String"),
        SetSize(Permission.SetSize, "size: min 9, max 54", "-cut");

        Permission permission;
        String[] args;

        Command(Permission permission, String... args) {
            this.permission = permission;
            this.args = args;
        }
    }
}
