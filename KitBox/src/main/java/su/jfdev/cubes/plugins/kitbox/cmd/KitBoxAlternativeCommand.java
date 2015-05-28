package su.jfdev.cubes.plugins.kitbox.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jamefrus on 19.05.2015.
 */

public class KitBoxAlternativeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        String cmd = Permission.search(command.getName()).getCmd();
        List<String> stringList = new ArrayList<>();
        stringList.add(cmd);
        Collections.addAll(stringList, strings);
        return KitBoxCommand.executeCommand(commandSender, stringList.toArray(new String[strings.length + 1]));
    }
}