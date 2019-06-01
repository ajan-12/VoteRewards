package top.ageofelysian.voterewards.Objects;

import com.vexsoftware.votifier.model.Vote;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import top.ageofelysian.voterewards.VoteRewards;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class UserEntry {
    private UUID uuid;
    private int streak;
    private int total;
    private long lastVoteTime;
    private HashMap<String, Integer> offlineVotes;

    public UserEntry(UUID uuid, int streak, int total, long lastVoteTime, HashMap<String, Integer> offlineVotes) {
        this.uuid = uuid;
        this.streak = streak;
        this.total = total;
        this.lastVoteTime = lastVoteTime;
        this.offlineVotes = offlineVotes;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getStreak() {
        return streak;
    }

    public int getTotal() {
        return total;
    }

    public long getLastVoteTime() {
        return lastVoteTime;
    }

    public void setLastVoteTime(long lastVoteTime) {
        this.lastVoteTime = lastVoteTime;
    }

    public HashMap<String, Integer> getOfflineVotes() {
        return offlineVotes;
    }

    public void incrementStreak() {
        this.streak++;
    }

    public void incrementTotal() {
        this.total++;
    }

    public void resetStreak() {
        this.streak = 0;
    }

    public void removeOfflineVote(Vote vote) {
        offlineVotes.remove(vote.getAddress());
    }

    public void updateOfflineVotes(HashMap<String, Integer> offlineVotes) {
        this.offlineVotes = offlineVotes;
    }

    public void save() {
        File file = new File(VoteRewards.getInstance().getDataFolder().getAbsolutePath() + File.separator + "PlayerData.yml");
        FileConfiguration fileCfg = YamlConfiguration.loadConfiguration(file);

        if (!fileCfg.contains(uuid.toString())) fileCfg.createSection(uuid.toString());

        ConfigurationSection data = fileCfg.getConfigurationSection(uuid.toString());

        data.set("streak", streak);
        data.set("total", total);
        data.set("lastVoteTime", lastVoteTime);

        data.set("offlineVotes", null);
        data.createSection("offlineVotes", offlineVotes);
    }
}
