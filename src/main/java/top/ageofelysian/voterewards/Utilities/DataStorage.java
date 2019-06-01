package top.ageofelysian.voterewards.Utilities;

import org.bukkit.ChatColor;
import top.ageofelysian.voterewards.Objects.Bonus;
import top.ageofelysian.voterewards.Objects.UserEntry;
import top.ageofelysian.voterewards.Objects.VoteKey;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class DataStorage {

    private final String tag = ChatColor.YELLOW + "[" + ChatColor.DARK_AQUA + "VoteRewards" + ChatColor.YELLOW + "]";

    private double baseReward;
    private HashMap<String, VoteKey> voteKeys;
    private HashMap<String, Bonus> bonuses;

    private HashSet<UserEntry> userData;

    private List<String> consoleCommands;
    private List<String> playerCommands;

    public DataStorage(double baseReward, HashMap<String, VoteKey> voteKeys, HashMap<String, Bonus> bonuses, HashSet<UserEntry> userData, List<String> consoleCommands, List<String> playerCommands) {
        this.baseReward = baseReward;
        this.voteKeys = voteKeys;
        this.bonuses = bonuses;
        this.userData = userData;
        this.consoleCommands = consoleCommands;
        this.playerCommands = playerCommands;
    }

    public String getTag() { return tag; }

    public double getBaseReward() { return baseReward; }
    public HashMap<String, VoteKey> getVoteKeys() { return voteKeys; }
    public HashMap<String, Bonus> getBonuses() { return bonuses; }

    public HashSet<UserEntry> getUserData() { return userData; }
    public UserEntry getUserData(UUID uuid) {
        for (UserEntry entry : userData) {
            if (entry.getUuid().equals(uuid)) return entry;
        }
        return null;
    }

    public List<String> getConsoleCommands() { return consoleCommands; }
    public List<String> getPlayerCommands() { return playerCommands; }

    public void addUserData(UserEntry entry) {
        userData.forEach(entry1 -> {
            if (entry1.getUuid().equals(entry.getUuid())) userData.remove(entry1);
        });
        userData.add(entry);
    }
}
