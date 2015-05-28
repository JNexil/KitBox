package su.jfdev.cubes.plugins.kitbox.cmd;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import su.jfdev.cubes.plugins.kitbox.Main;
import su.jfdev.cubes.plugins.kitbox.util.HelpBuilder;
import su.jfdev.cubes.plugins.kitbox.yaml.Config;
import su.jfdev.cubes.plugins.kitbox.yaml.YamlControl;

import java.util.HashSet;

import static su.jfdev.cubes.plugins.kitbox.cmd.Permission.*;

/**
 * Created by Jamefrus on 08.05.2015.
 */

public class KitBoxCommand implements CommandExecutor {

    private static final String PREFIX = ChatColor.DARK_GREEN + "[KitBox] " + ChatColor.DARK_AQUA;
    private Plugin plugin;

    public KitBoxCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    public static boolean executeCommand(CommandSender commandSender, String[] strings) {
        boolean isPlayer = false;
        if (commandSender instanceof Player) isPlayer = true;
        String metaCommand = null;
        if (strings.length > 0) metaCommand = strings[0];
        Block targetBlock = null;
        if (isPlayer) targetBlock = ((Player) commandSender).getPlayer().getTargetBlock((HashSet<Material>) null, 6);
        if (strings.length == 0 && has(commandSender, KitBox)) {
            commandSender.sendMessage("Используйте /kitbox help для получения информации");
        } else if (metaCommand.equalsIgnoreCase("create") && has(commandSender, Create) && isPlayer) {
            return onCreateCommand(commandSender, strings, targetBlock);
        } else if (metaCommand.equalsIgnoreCase("save") && has(commandSender, Save)) {
            Main.getInstance().saveBoxYaml();
            commandSender.sendMessage(PREFIX + "База боксов сохранена");
            return true;
        } else if (metaCommand.equalsIgnoreCase("reload") && has(commandSender, Reload)) {
            Main.getInstance().reload();
            commandSender.sendMessage(PREFIX + "База боксов перезагружена");
            return true;
        } else if (metaCommand.equalsIgnoreCase("remove") && has(commandSender, Remove) && isPlayer) {
            YamlControl.removeYamlInventory(targetBlock.getLocation());
            commandSender.sendMessage(PREFIX + "KitBox с цели удален");
            return true;
        } else if (metaCommand.equalsIgnoreCase("setowner") && has(commandSender, SetOwner) && isPlayer) {
            String newName;
            if (strings.length > 1 && !strings[1].isEmpty()) {
                newName = strings[1];
            } else newName = Config.INV_OWNER.getString();
            YamlControl.renameOwner(newName, targetBlock.getLocation());
            commandSender.sendMessage(PREFIX + "KitBox перепривязан к имени: " + newName);
        } else if (metaCommand.equalsIgnoreCase("help") && has(commandSender, Help)) {
            commandSender.sendMessage(HelpBuilder.createHelp(commandSender));
            return true;
        } else if (metaCommand.equalsIgnoreCase("setname") && has(commandSender, SetName) && isPlayer) {
            String title = null;
            if (strings.length > 1) {
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < strings.length; i++) {
                    if (i > 1) sb.append(' ');
                    sb.append(strings[i]);
                }
                title = sb.toString();
                YamlControl.rename(title, targetBlock.getLocation());
                commandSender.sendMessage(PREFIX + "KitBox переименован: " + title);
                return true;
            } else {
                commandSender.sendMessage(PREFIX + ChatColor.DARK_RED + "Не указано имя, используйте /setname [name]");
                return true;
            }
        } else if (metaCommand.equalsIgnoreCase("setsize") && has(commandSender, SetSize) && isPlayer) {
            if (strings.length > 1) {
                if (strings[1].equalsIgnoreCase("-cut")) {
                    try {
                        YamlControl.setSize(-1, targetBlock.getLocation());
                    } catch (IllegalArgumentException e) {
                        if (e.getMessage().equals("error_no_changes")) {
                            commandSender.sendMessage(PREFIX + ChatColor.DARK_RED + "Невозможно сжать более");
                        }
                    }

                } else {
                    try {
                        return setSizeCommand(commandSender, strings, targetBlock);
                    } catch (IllegalArgumentException e) {
                        if (e.getMessage().equals("error_no_changes")) {
                            commandSender.sendMessage(PREFIX + ChatColor.DARK_RED + "Вы указали размер, совпадающий с прежним");
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    private static boolean setSizeCommand(CommandSender commandSender, String[] strings, Block targetBlock) throws IllegalArgumentException {
        int size = -1;
        try {
            size = Integer.parseInt(strings[1]);
            if (size % 9 != 0) {
                size /= 9;
                size++;
                size *= 9;
            }

            if (strings.length > 2 && strings[2].equalsIgnoreCase("-cut")) {
                YamlControl.setSize(size, targetBlock.getLocation(), true);
                return true;
            }
            try {
                YamlControl.setSize(size, targetBlock.getLocation());
            } catch (IllegalArgumentException e) {
                if (e.getMessage().startsWith("error_size")) {
                    int minsize = Integer.parseInt(e.getMessage().substring(11, e.getMessage().length()));
                    commandSender.sendMessage(PREFIX + ChatColor.DARK_RED + "Минимальный размер для этого бокса: " + minsize + ". Увеличьте размер или используйте /setsize [size] -cut");
                } else throw e;
            }
        } catch (NumberFormatException e) {
            commandSender.sendMessage(PREFIX + ChatColor.DARK_RED + "Не указан размер, используйте /setsize [size]");
        }
        return true;
    }

    private static boolean onCreateCommand(CommandSender commandSender, String[] strings, Block targetBlock) {
        int inventorySize;
        String title;
        boolean duplicate = Config.INV_DUPLICATE.getBoolean();
        if (strings.length > 1 && !strings[1].isEmpty()) {
            duplicate = strings[1].equalsIgnoreCase("true") || strings[3].equals("1");
        }
        if (strings.length > 2 && !strings[2].isEmpty()) {
            try {
                inventorySize = Integer.parseInt(strings[2]);
            } catch (NumberFormatException e) {
                inventorySize = Config.INV_SIZE.getInteger();
            }
        } else {
            inventorySize = Config.INV_SIZE.getInteger();
        }

        if (strings.length > 3 && !strings[3].isEmpty()) {
            title = strings[3];
            if (strings.length > 3) {
                StringBuilder sb = new StringBuilder(title);
                for (int i = 4; i < strings.length; i++) {
                    sb.append(' ').append(strings[i]);
                }
                title = sb.toString();
            }
        } else title = Config.INV_NAME.getString();

        if (!has(commandSender, Duplicate)) duplicate = false;

        if (inventorySize % 9 != 0) {
            inventorySize /= 9;
            inventorySize++;
            inventorySize *= 9;
        }

        Inventory inventory = Bukkit.createInventory(null, inventorySize, title);
        YamlControl.createYamlInventory(targetBlock.getLocation(), commandSender.getName(), inventory, duplicate);
        commandSender.sendMessage(PREFIX + "KitBox создан. Размер: " + inventorySize + ". Название: " + title + ". Владелец: " + commandSender.getName());
        return true;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return executeCommand(commandSender, strings);
    }

}
