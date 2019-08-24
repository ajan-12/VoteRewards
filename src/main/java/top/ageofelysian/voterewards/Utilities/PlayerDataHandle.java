package top.ageofelysian.voterewards.Utilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import top.ageofelysian.voterewards.VoteRewards;
import top.ageofelysian.voterewards.Objects.UserEntry;

public class PlayerDataHandle {

    public static HashSet<UserEntry> getUserData() {
        HashSet<UserEntry> userData = new HashSet<>();

        FileConfiguration data = VoteRewards.getInstance().getPlayerDataConfig();

        for (String key : data.getKeys(false)) {
        	ConfigurationSection section = data.getConfigurationSection(key);
        	
        	//Loads offline votes
        	HashMap<String, Integer> offlineVotes = new HashMap<>();
        	if(section.isList("offlineVotes")) {
	            section.getStringList("offlineVotes").forEach(string -> {
	            	
	            	String[] offlineVoteData = string.split(":");
	            	if(offlineVoteData.length != 2) {
	            		Bukkit.getLogger().info("Offline votes value of player with UUID " + key + " has a invalid format. Ignoring it.");
	            		return;
	            	}
	            	
	            	String address = offlineVoteData[0];
	            	int offlineVoteAmount;
	            	try {
	            		offlineVoteAmount = Integer.parseInt(offlineVoteData[1]);
	            	} catch (NumberFormatException e) {
	            		Bukkit.getLogger().info("Offline votes value for the site " + address + " of player with UUID " + key + " is not an integer. Ignoring it.");
	            		return;
	            	}
	            	
	            	offlineVotes.put(address, offlineVoteAmount);
	            	
	            });
        	} else {
        		Bukkit.getLogger().info("Offline votes section of player with UUID " + key + " is not valid. Ignoring it.");
        	}
        	
        	//Loads last vote times
        	HashMap<String, Long> lastVoteTimes = new HashMap<>();
        	if(section.isList("lastVoteTimes")) {
	            section.getStringList("lastVoteTimes").forEach(string -> {
	            	
	            	String[] lastVoteTimesData = string.split(":");
	            	if(lastVoteTimesData.length != 2) {
	            		Bukkit.getLogger().info("Last vote time value of player with UUID " + key + " has a invalid format. Ignoring it.");
	            		return;
	            	}
	            	
	            	String address = lastVoteTimesData[0];
	            	long lastVoteTime;
	            	try {
	            		lastVoteTime = Long.parseLong(lastVoteTimesData[1]);
	            	} catch (NumberFormatException e) {
	            		Bukkit.getLogger().info("Last vote time value for the site " + address + " of player with UUID " + key + " is not an long. Ignoring it.");
	            		return;
	            	}
	            	
	            	lastVoteTimes.put(address, lastVoteTime);
	            	
	            });
        	} else {
        		Bukkit.getLogger().info("Last vote times section of player with UUID " + key + " is not valid. Ignoring it.");
        	}
        	
        	if(!data.isInt(key + ".streak")) {
        		Bukkit.getLogger().info("Voting streak value for player with UUID " + key + " is not a integer. Using default value.");
        	}
        	
        	if(!data.isInt(key + ".total")) {
        		Bukkit.getLogger().info("Total votes value for player with UUID " + key + " is not a integer. Using default value.");
        	}

            UserEntry entry = new UserEntry(UUID.fromString(key), data.getInt(key + ".streak", 0), data.getInt(key + ".total", 0), lastVoteTimes, offlineVotes);
            userData.add(entry);
        }

        return userData;
    }

    public static void saveUserData() {

        VoteRewards.getStorage().getUserData().forEach(entry -> entry.save(false));
            
        if(saveDataToFile()) Bukkit.getLogger().info(VoteRewards.getStorage().getTag() + " Successfully saved PlayerData.yml.");
    }
    
    public static boolean saveDataToFile() {
        try {
            VoteRewards.getInstance().getPlayerDataConfig().save(VoteRewards.getInstance().getPlayerDataFile());
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(VoteRewards.getStorage().getTag() + ChatColor.DARK_RED + " Cannot save the PlayerData.yml");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
