package su.jfdev.cubes.plugins.kitbox;

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
import org.yaml.snakeyaml.Yaml;

import static su.jfdev.cubes.plugins.kitbox.Permissions.*;

import java.util.HashSet;

/**
 * Created by Jamefrus on 08.05.2015.
 */

public class KitBoxCMDs implements CommandExecutor {

    public KitBoxCMDs(Plugin plugin) {
        this.plugin = plugin;
    };
    private static final String PREFIX = ChatColor.DARK_GREEN + "[KitBox]" + ChatColor.AQUA;

    private Plugin plugin;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        boolean isPlayer = false;
        if (commandSender instanceof Player) isPlayer = true;
        String metaCommand = null;
        if(strings.length > 0) metaCommand = strings[0];
        Block targetBlock = null;
        if(isPlayer) targetBlock = ((Player) commandSender).getPlayer().getTargetBlock((HashSet<Material>) null, 6);
        if (strings.length == 0 && has(commandSender,KitBox)) {
            commandSender.sendMessage("Используйте /kitbox help для получения информации");
        } else if (metaCommand.equalsIgnoreCase("create") && has(commandSender,Create)&& isPlayer) {
            return onCreateCommand(commandSender, strings,targetBlock);
        } else if (metaCommand.equalsIgnoreCase("save") && has(commandSender,Save)) {
            Main.getInstance().saveBoxYaml();
            commandSender.sendMessage(PREFIX + " База боксов сохранена");
            return true;
        } else if (metaCommand.equalsIgnoreCase("reload") && has(commandSender,Reload)) {
            Main.getInstance().reload();
            commandSender.sendMessage(PREFIX + " База боксов перезагружена");
            return true;
        } else if (metaCommand.equalsIgnoreCase("remove") && has(commandSender,Remove)&& isPlayer) {
            YamlMap.removeYamlInventory(targetBlock.getLocation());
            commandSender.sendMessage(PREFIX + " KitBox с цели удален");
            return true;
        } else if (metaCommand.equalsIgnoreCase("setowner") && has(commandSender, SetOwner) && isPlayer){
            String newName;
            if (strings.length > 1 && !strings[1].isEmpty()) {
                newName = strings[1];
            } else newName = Defaults.INV_OWNER;
            YamlMap.renameOwner(newName,targetBlock.getLocation());
            commandSender.sendMessage(PREFIX + " KitBox перепривязан к имени: " + newName);
        } else if(metaCommand.equalsIgnoreCase("help") && has(commandSender,Help) ) {
            StringBuilder msg = new StringBuilder();
            commandSender.sendMessage(PREFIX + Main.getInstance().getHelpText()
//                    + " Доступны команды:\n" +
//                    " /kitbox create [duplicate: true|false or 1|0] [size: min 8, max 56] [title: String] - создание бокса с размером size, названием title, дублирующегося(true) или недублирующегося(false) \n" +
//                    " /kitbox reload - перезагрузка плагина\n"+" /kitbox save - сохранение базы боксов\n" +" /kitbox remove - удаление целевого бокса\n" +
//            " /kitbox setowner [name: String] - перепривязка к имени (смена владельца инвентаря по умолчанию)"
            );
            return true;
        }
        return false;

    }

    private boolean onCreateCommand(CommandSender commandSender, String[] strings, Block targetBlock) {
        int inventorySize;
        String title;
        boolean duplicate = Defaults.INV_DUPLICATE;
        if (strings.length > 1 && !strings[1].isEmpty()) {
            duplicate = strings[1].equalsIgnoreCase("true") || strings[3].equals("1");
        }
        if (strings.length > 2 && !strings[2].isEmpty()) {
            try {
                inventorySize = Integer.parseInt(strings[2]);
            } catch (NumberFormatException e) {
                inventorySize = Defaults.INV_SIZE;
            }
        } else {
            inventorySize = Defaults.INV_SIZE;
        }

        if (strings.length > 3 && !strings[3].isEmpty()) {
            title = strings[3];
            if(strings.length > 3) {
                StringBuilder sb = new StringBuilder(title);
                for (int i = 4; i < strings.length; i++) {
                    sb.append(' ').append(strings[i]);
                }
                title = sb.toString();
            }
        } else title = Defaults.INV_NAME;

        if(!has(commandSender,Duplicate)) duplicate = false;

        inventorySize = Math.round((inventorySize / 9.0F));
        inventorySize *= 9;

        Inventory inventory = Bukkit.createInventory(null, inventorySize, title);
        YamlMap.createYamlInventory(targetBlock.getLocation(), commandSender.getName(), inventory,duplicate);
        commandSender.sendMessage(PREFIX + " KitBox создан. Размер: " + inventorySize + ". Название: " + title + ". Владелец: " + commandSender.getName());
        return true;
    }

}
