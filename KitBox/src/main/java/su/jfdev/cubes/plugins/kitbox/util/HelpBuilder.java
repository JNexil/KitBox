package su.jfdev.cubes.plugins.kitbox.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import su.jfdev.cubes.plugins.kitbox.OPermission;
import su.jfdev.cubes.plugins.kitbox.cmd.Command;
import su.jfdev.cubes.plugins.kitbox.lang.Localization;
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
        stringList.add(ChatColor.DARK_GREEN + "[KitBox] " + Localization.getLocalizedCommandText(Command.Help, "title"));
        if (Config.HELP_SEPARATION.getBoolean()) {
            constructHelp(cm, stringList, (byte) 0);
            constructHelp(cm, stringList, (byte) 1);
        } else {
            constructHelp(cm, stringList, (byte) 2);
        }
        if (stringList.size() > 1) {
            return UtilString.buildList(stringList).toString();
        }

        return Localization.getLocalizedCommandText(Command.Help, "nocmds");
    }

    private static void constructHelp(CommandSender cm, List<String> stringList, byte alt) {
        for (CommandHelper commandHelper : CommandHelper.values()) {
            if (su.jfdev.cubes.plugins.kitbox.cmd.Command.has(cm, commandHelper.getCommand())) {
                StringBuilder commandbuilder = new StringBuilder();
                if (alt != 1)
                    commandbuilder.append(ChatColor.GOLD).append("• /kitbox ").append(commandHelper.getCommand().getCommand());
                else commandbuilder.append(ChatColor.GOLD).append("• /").append(commandHelper.getCommand().getAltCMD());
                if (alt == 2)
                    commandbuilder.append(ChatColor.YELLOW).append(" or /").append(commandHelper.getCommand().getAltCMD());
                if (commandHelper.args != null)
                    if (commandHelper.args.length > 0) {
                        commandbuilder.append(ChatColor.GRAY);
                        for (String arg : commandHelper.args) {
                            if (arg.contains("duplicate")) {
                                if (!OPermission.Duplicate.has(cm)) {
                                    continue;
                                }
                            }
                            commandbuilder.append(" [").append(arg).append("]");
                        }
                    }
                commandbuilder.append(ChatColor.WHITE).append(" - " + commandHelper.getCommand().getLocalizedDescription());
                stringList.add(commandbuilder.toString());

            }
        }
    }

    private enum CommandHelper {
        Reload,
        Create("duplicate: true(1) or false(0)", "size: min 9, max 54", "title: String"),
        Save,
        Remove,
        SetOwner("name: String"),
        SetName("name: String"),
        SetSize("size: min 9, max 54", "-cut");

        private String[] args;

        CommandHelper() {
        }

        CommandHelper(String... args) {
            this.args = args;
        }

        public Command getCommand() {
            return Command.valueOf(this.name());
        }

        public String[] getArgs() {
            return args;
        }
    }
}
