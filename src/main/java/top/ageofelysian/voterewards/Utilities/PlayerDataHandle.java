package top.ageofelysian.voterewards.Utilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import top.ageofelysian.voterewards.Objects.UserEntry;
import top.ageofelysian.voterewards.VoteRewards;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class PlayerDataHandle {

    public HashSet<UserEntry> getUserData(File dataFile) {
        HashSet<UserEntry> userData = new HashSet<>();

        FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

        for (String key : data.getKeys(false)) {

            HashMap<String, Integer> offlineVotes = new HashMap<>();
            data.getConfigurationSection("offlineVotes").getKeys(false).forEach(address -> offlineVotes.put(address, data.getInt("offlineVotes." + address)));

            HashMap<String, Long> lastVoteTimes = new HashMap<>();
            data.getConfigurationSection("lastVoteTimes").getKeys(false).forEach(address -> lastVoteTimes.put(address, data.getLong("lastVoteTimes." + address)));

            UserEntry entry = new UserEntry(UUID.fromString(key), data.getInt(key + ".streak", 0), data.getInt(key + ".total", 0), lastVoteTimes, offlineVotes);
            userData.add(entry);
        }

        return userData;
    }

    public void saveUserData(File dataFile) {
        FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

        for (UserEntry entry : VoteRewards.getStorage().getUserData()) {
            if (data.contains(entry.getUuid().toString())) {

                ConfigurationSection section = data.getConfigurationSection(entry.getUuid().toString());

                section.set("offlineVotes", entry.getOfflineVotes());
                section.set("streak", entry.getStreak());
                section.set("total", entry.getTotal());
                section.set("lastVoteTime", entry.getLastVoteTime());

            } else {

                data.createSection(entry.getUuid().toString());

                ConfigurationSection section = data.getConfigurationSection(entry.getUuid().toString());

                section.set("offlineVotes", entry.getOfflineVotes());
                section.set("streak", entry.getStreak());
                section.set("total", entry.getTotal());
                section.set("lastVoteTime", entry.getLastVoteTime());
            }
        }

        try {
            data.save(dataFile);
            Bukkit.getLogger().info("Saved the PlayerData.yml file.");
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(VoteRewards.getStorage().getTag() + ChatColor.DARK_RED + " Cannot save the PlayerData.yml");
            e.printStackTrace();
        }
    }
}
