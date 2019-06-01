package top.ageofelysian.voterewards.Objects;

import com.vexsoftware.votifier.model.Vote;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import top.ageofelysian.voterewards.VoteRewards;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class UserEntry {

    private UUID uuid;

    private int streak;
    private int total;

    private HashMap<String, Long> lastVoteTimes;
    private HashMap<String, Integer> offlineVotes;

    public UserEntry(UUID uuid, int streak, int total, HashMap<String, Long> lastVoteTimes, HashMap<String, Integer> offlineVotes) {

        this.uuid = uuid;

        this.streak = streak;
        this.total = total;

        this.lastVoteTimes = lastVoteTimes;
        this.offlineVotes = offlineVotes;

    }

    public UUID getUuid() { return uuid; }

    public int getStreak() { return streak; }
    public void incrementStreak() { this.streak++; }
    public void resetStreak() { this.streak = 0; }

    public int getTotal() { return total; }
    public void incrementTotal() { this.total++; }

    public HashMap<String, Long> getLastVoteTime() { return lastVoteTimes; }
    public void setLastVoteTime(String address, Long lastVoteTime) {
        if (lastVoteTimes.containsKey(address)) {
            lastVoteTimes.replace(address, lastVoteTime);
        } else {
            lastVoteTimes.put(address, lastVoteTime);
        }
    }
    public void updateLastVoteTime(HashMap<String, Long> lastVoteTimes) { this.lastVoteTimes = lastVoteTimes; }

    public HashMap<String, Integer> getOfflineVotes() { return offlineVotes; }
    public void removeOfflineVote(String address) { offlineVotes.remove(address); }
    public void updateOfflineVotes(HashMap<String, Integer> offlineVotes) { this.offlineVotes = offlineVotes; }

    public void save() {
        File file = new File(VoteRewards.getInstance().getDataFolder().getAbsolutePath() + File.separator + "PlayerData.yml");
        FileConfiguration fileCfg = YamlConfiguration.loadConfiguration(file);

        if (!fileCfg.contains(uuid.toString())) fileCfg.createSection(uuid.toString());

        ConfigurationSection data = fileCfg.getConfigurationSection(uuid.toString());

        data.set("streak", streak);
        data.set("total", total);

        data.set("lastVoteTimes", null);
        data.set("lastVoteTimes", lastVoteTimes);

        data.set("offlineVotes", null);
        data.createSection("offlineVotes", offlineVotes);

        try {
            fileCfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
