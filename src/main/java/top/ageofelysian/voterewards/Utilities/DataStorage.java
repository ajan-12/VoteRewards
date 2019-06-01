package top.ageofelysian.voterewards.Utilities;

import org.bukkit.ChatColor;
import top.ageofelysian.voterewards.Objects.Bonus;
import top.ageofelysian.voterewards.Objects.UserEntry;
import top.ageofelysian.voterewards.Objects.VoteKey;

import java.util.HashMap;
import java.util.HashSet;

public class DataStorage {
    private final String tag = ChatColor.YELLOW + "[" + ChatColor.DARK_AQUA + "VoteRewards" + ChatColor.YELLOW + "]";
    private double baseReward;
    private HashMap<String, VoteKey> voteKeys;
    private HashMap<String, Bonus> bonuses;
    private HashSet<UserEntry> userData;
    public DataStorage(double baseReward, HashMap<String, VoteKey> voteKeys, HashMap<String, Bonus> bonuses, HashSet<UserEntry> userData) {
        this.baseReward = baseReward;
        this.voteKeys = voteKeys;
        this.bonuses = bonuses;
        this.userData = userData;
    }

    public String getTag() {
        return tag;
    }

    public double getBaseReward() {
        return baseReward;
    }

    public HashMap<String, VoteKey> getVoteKeys() {
        return voteKeys;
    }

    public HashMap<String, Bonus> getBonuses() {
        return bonuses;
    }

    public HashSet<UserEntry> getUserData() {
        return userData;
    }

    public void addUserData(UserEntry entry) {
        userData.forEach(entry1 -> {
            if (entry1.getUuid().equals(entry.getUuid())) userData.remove(entry1);
        });
        userData.add(entry);
    }
}
