package top.ageofelysian.voterewards.Events;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

import top.ageofelysian.voterewards.VoteRewards;
import top.ageofelysian.voterewards.Objects.UserEntry;
import top.ageofelysian.voterewards.Utilities.GeneralUtils;

public class VoteEvent implements Listener {
    private final GeneralUtils utils = new GeneralUtils();

    @EventHandler
    public void onVote(VotifierEvent event) {
        final Vote vote = event.getVote();

        if (vote.getUsername() == null) return;
        
        UUID playerUUID = null;
        if (Bukkit.getPlayerExact(vote.getUsername()) != null) {
        	
        	playerUUID = Bukkit.getPlayerExact(vote.getUsername()).getUniqueId();
        	utils.processVote(Bukkit.getPlayerExact(vote.getUsername()), vote.getServiceName());
        	
        } else {
        	for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {

                if (offlinePlayer.getName().equals(vote.getUsername())) {
                	
                	playerUUID = offlinePlayer.getUniqueId();
                    utils.addOfflineVote(offlinePlayer.getUniqueId(), vote);
                    break;

                }

            }
        }
        
        if (playerUUID == null) return; //If playerUUID is null, that player does not exist
        
        UserEntry entry = VoteRewards.getStorage().getUserData(playerUUID);
        if (entry == null) entry = new UserEntry(playerUUID, 0, 0, new HashMap<>(), new HashMap<>());
        	
        // 129.600.000‬ equals 1 day and a half
        if (entry.getLastVoteTime().containsKey(vote.getServiceName()) && (entry.getLastVoteTime().get(vote.getServiceName()) < (System.currentTimeMillis() - 129_600_000))) {
            entry.resetStreak();
        }

        entry.incrementStreak();
        entry.incrementTotal();
        
        entry.setLastVoteTime(vote.getServiceName(), System.currentTimeMillis());
        entry.save(true);
        VoteRewards.getStorage().addUserData(entry);
        
        final String name = vote.getUsername();
        final String address = vote.getServiceName();

        VoteRewards.getStorage().getConsoleCommands().forEach(command -> {
            if (command.charAt(0) == '/') {
                command = command.replaceFirst("/", "");
            }

            command = command.replaceAll("%player%", name);
            command = command.replaceAll("%site%", address);

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', command));
        });
    }
}
