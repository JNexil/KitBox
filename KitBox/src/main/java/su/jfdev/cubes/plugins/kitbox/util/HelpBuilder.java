package su.jfdev.cubes.plugins.kitbox.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import su.jfdev.cubes.plugins.kitbox.Permissions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jamefrus on 13.05.2015.
 */

public class HelpBuilder {

    private enum Command {
        Reload(Permissions.Reload, "reload"),
        Create(Permissions.Create, "create", "duplicate: true(1) or false(0)", "size: min 9, max 54", "title: String"),
        Save(Permissions.Save, "save"),
        Remove(Permissions.Remove, "remove"),
        SetOwner(Permissions.SetOwner, "setowner", "name: String"),
        SetName(Permissions.SetName, "setname", "name:String"),
        SetSize(Permissions.SetSize, "setsize", "size: min 9, max 54", "-cut");

        Permissions permission;
        String command;
        String[] args;

        Command(Permissions permission, String command, String... args) {
            this.permission = permission;
            this.command = command;
            this.args = args;
        }
    }

    private HelpBuilder() {
    }

    public static HelpBuilder createHelpBuilder() {
        return new HelpBuilder();
    }

    public static String createHelp(CommandSender cm) {
        StringBuilder sb = new StringBuilder();
        List<String> stringList = new ArrayList<>();
        stringList.add(ChatColor.DARK_GREEN + "[KitBox] Доступны команды:");
        for (Command command : Command.values()) {
            if (Permissions.has(cm, command.permission)) {
                StringBuilder commandbuilder = new StringBuilder();
                commandbuilder.append(ChatColor.GOLD).append("• /kitbox ").append(command.command);
                if (command.args.length > 0) {
                    commandbuilder.append(ChatColor.GRAY);
                    for (String arg : command.args) {
                        if (arg.contains("duplicate")) {
                            if (!Permissions.has(cm, Permissions.Duplicate)) {
                                continue;
                            }
                        }
                        commandbuilder.append(" [").append(arg).append("]");
                    }
                }
                commandbuilder.append(ChatColor.WHITE).append(" - " + getLocalizedValue(command.command));
                stringList.add(commandbuilder.toString());

            }
        }
        if (stringList.size() > 1) {
            return Util.buildList(stringList).toString();
        }

        return "Вам не доступны команды KitBox";
    }

    public static String getLocalizedValue(String command) {
        return "no translated value";
    }
}
