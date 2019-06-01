package top.ageofelysian.voterewards.Utilities;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import top.ageofelysian.voterewards.Objects.UserEntry;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class PlayerDataHandle {
    HashSet<UserEntry> userData;

    public PlayerDataHandle(File dataFile) {
        userData = new HashSet<>();

        FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

        for (String key : data.getKeys(false)) {

            HashMap<String, Integer> offlineVotes = new HashMap<>();
            data.getConfigurationSection("offlineVotes").getKeys(false).forEach(address -> offlineVotes.put(address, data.getInt("offlineVotes." + address)));

            UserEntry entry = new UserEntry(UUID.fromString(key), data.getInt(key + ".streak", 0), data.getInt(key + ".total", 0), data.getInt(key + ".lastVoteTime", 0), offlineVotes);
            userData.add(entry);
        }
    }

    public HashSet<UserEntry> getUserData() {
        HashSet<UserEntry> temp = userData;

        userData = null;

        return temp;
    }
}
