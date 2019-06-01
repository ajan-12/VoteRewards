package top.ageofelysian.voterewards.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.ageofelysian.voterewards.VoteRewards;

public class MainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args[0].equals("reload") && args.length == 1) {
            VoteRewards.getInstance().initDataStorage();

            if (sender instanceof Player) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', VoteRewards.getStorage().getTag() + " &aSuccessfully reloaded the plugin."));
                Bukkit.getLogger().info("Successfully reloaded the plugin by the command of " + sender.getName() + "/" + ((Player) sender).getUniqueId().toString() + ".");
            } else {
                sender.sendMessage("Successfully reloaded the plugin.");
            }
            return true;

        } else return false;
    }
}
