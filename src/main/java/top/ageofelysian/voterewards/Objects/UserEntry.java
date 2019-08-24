package top.ageofelysian.voterewards.Objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import top.ageofelysian.voterewards.VoteRewards;
import top.ageofelysian.voterewards.Utilities.PlayerDataHandle;

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

    public void save(boolean saveToFile) {
        FileConfiguration fileCfg = VoteRewards.getInstance().getPlayerDataConfig();

        if (!fileCfg.contains(uuid.toString())) fileCfg.createSection(uuid.toString());

        ConfigurationSection data = fileCfg.getConfigurationSection(uuid.toString());

        data.set("streak", streak);
        data.set("total", total);
        
        
        List<String> lastVoteTimesList = new ArrayList<String>();
        lastVoteTimes.forEach((address, lastVoteTime) -> lastVoteTimesList.add(address + ":" + lastVoteTime));
        data.set("lastVoteTimes", lastVoteTimesList);

        List<String> offlineVotesList = new ArrayList<String>();
        offlineVotes.forEach((address, offlineVotesAmount) -> offlineVotesList.add(address + ":" + offlineVotesAmount));
        data.set("offlineVotes", offlineVotesList);

        if(saveToFile) PlayerDataHandle.saveDataToFile();
        
    }
}
