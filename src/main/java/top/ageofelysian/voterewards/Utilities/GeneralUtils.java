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

    public void processVote(Player p, String address) {
        broadcast("&a" + p.getName() + " &6voted at &a" + address + " &6and got");

        //Giving the base reward
        VoteRewards.getInstance().getEcon().depositPlayer(p, VoteRewards.getStorage().getBaseReward());
        broadcast(" &a- " + VoteRewards.getStorage().getBaseReward() + " &6for voting");

        VoteRewards.getStorage().getBonuses().values().forEach(bonus -> bonus.apply(p));
        VoteRewards.getStorage().getVoteKeys().values().forEach(voteKey -> voteKey.apply(p.getPlayer()));

        VoteRewards.getStorage().getPlayerCommands().forEach(command -> {

            if (command.charAt(0) == '/') {
                command = command.replaceFirst("/", "");
            }

            command = command.replaceAll("%player%", p.getName());
            command = command.replaceAll("%site%", address);

            ChatColor.translateAlternateColorCodes('&', command);

            Bukkit.dispatchCommand(p, command);
        });
    }

    public void addOfflineVote(UUID uuid, Vote vote) {
        UserEntry entry = VoteRewards.getStorage().getUserData(uuid);
        String service = vote.getServiceName();

        if (entry == null) {
            final HashMap<String, Integer> offlineVotes = new HashMap<>();
            offlineVotes.put(service, 1);

            entry = new UserEntry(uuid, 0, 0, new HashMap<>(), offlineVotes);
            entry.save(true);
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
            entry.save(true);
            VoteRewards.getStorage().addUserData(entry);

        }
    }
}
