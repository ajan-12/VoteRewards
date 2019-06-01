package top.ageofelysian.voterewards.Utilities;

import com.vexsoftware.votifier.model.Vote;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import top.ageofelysian.voterewards.Objects.UserEntry;
import top.ageofelysian.voterewards.VoteRewards;

import java.util.HashMap;
import java.util.UUID;

public class GeneralUtils {

    public void broadcast(String message) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public UserEntry getEntry(UUID uuid) {
        UserEntry entry = null;
        for (UserEntry entry1 : VoteRewards.getStorage().getUserData()) {
            if (entry1.getUuid().equals(uuid)) {
                entry = entry1;
                break;
            }
        }
        return entry;
    }

    public void processVote(Player p, String address) {
        broadcast("&a" + p.getName() + " &6voted at &a" + address + " &6and got");

        //Giving the base reward
        VoteRewards.getInstance().getEcon().depositPlayer(p, VoteRewards.getStorage().getBaseReward());
        broadcast(" &a- " + VoteRewards.getStorage().getBaseReward() + " &6for voting");

        VoteRewards.getStorage().getBonuses().values().forEach(bonus -> bonus.apply(p));
        VoteRewards.getStorage().getVoteKeys().values().forEach(voteKey -> voteKey.apply(p.getPlayer()));
    }

    public void addOfflineVote(UUID uuid, Vote vote) {
        UserEntry entry = getEntry(uuid);
        String service = vote.getServiceName();

        if (entry == null) {
            final HashMap<String, Integer> offlineVotes = new HashMap<>();
            offlineVotes.put(service, 1);

            entry = new UserEntry(uuid, 1, 1, (int) System.currentTimeMillis(), offlineVotes);
            entry.save();
            VoteRewards.getStorage().addUserData(entry);

        } else {

            final HashMap<String, Integer> offlineVotes = entry.getOfflineVotes();

            if (offlineVotes.containsKey(service)) {
                int amount = offlineVotes.get(service);
                amount++;
                offlineVotes.replace(service, amount);
            } else {
                offlineVotes.put(service, 1);
            }

            entry.updateOfflineVotes(offlineVotes);
            entry.save();
            VoteRewards.getStorage().addUserData(entry);

        }
    }
}