package top.ageofelysian.voterewards.Commands;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import top.ageofelysian.voterewards.VoteRewards;
import top.ageofelysian.voterewards.Objects.UserEntry;

public class MainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	if(args.length > 0) {
    		if (args[0].equals("reload")) {
                VoteRewards.getInstance().initDataStorage();

                if (sender instanceof Player) {
                    sender.sendMessage(VoteRewards.getStorage().getTag() + ChatColor.GREEN + " Successfully reloaded the plugin.");
                    Bukkit.getLogger().info("Successfully reloaded the plugin by the command of " + sender.getName() + "/" + ((Player) sender).getUniqueId().toString() + ".");
                } else {
                    sender.sendMessage("Successfully reloaded the plugin.");
                }
                return true;
            } else if (args[0].equals("stats")) {
            	
            	if (args.length != 2) {
            		sender.sendMessage(VoteRewards.getStorage().getTag() + "Correct Usage: /voterewards stats <playerName>");
            		return true;
            	}
            	
            	UUID uuid = null;
            	for(OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            		if (player.getName().equals(args[1])) {
            			uuid = player.getUniqueId();
            			break;
            		}
            	}
            	
            	if (uuid == null) {
            		sender.sendMessage(VoteRewards.getStorage().getTag() + " The player does not exist.");
            		return true;
            	}
            	
            	UserEntry entry = VoteRewards.getStorage().getUserData(uuid);
            	if (entry == null) {
            		sender.sendMessage(VoteRewards.getStorage().getTag() + " No vote data exist for this player.");
            		return true;
            	}
            	
            	sender.sendMessage(ChatColor.GREEN + "--- " + args[1] + "'s vote statistics ---");
            	sender.sendMessage("Total votes: " + entry.getTotal());
            	sender.sendMessage("Current streak: " + entry.getStreak());
            	
            	sender.sendMessage("");
            	if (entry.getLastVoteTime().isEmpty()) {
            		sender.sendMessage("Last vote times: None");
            	} else {
            		sender.sendMessage("Last vote times:");
            		DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm");
            		entry.getLastVoteTime().forEach((site, lastVoteTime) -> {
            			sender.sendMessage(site + " - " + LocalDateTime.ofInstant(Instant.ofEpochMilli(lastVoteTime), ZoneId.systemDefault()).format(dateTimeFormat));
            		});
            	}
            	
            	sender.sendMessage("");
            	if (entry.getOfflineVotes().isEmpty()) {
            		sender.sendMessage("Offline votes: None");
            	} else {
            		sender.sendMessage("Offline votes:");
            		entry.getOfflineVotes().forEach((site, offlineVoteAmount) -> {
            			sender.sendMessage(site + " - " + offlineVoteAmount);
            		});
            	}
            	
            	return true;
            	
            } else return false;
    	} else return false;
    }
}
