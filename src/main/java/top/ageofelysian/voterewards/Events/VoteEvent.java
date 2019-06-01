package top.ageofelysian.voterewards.Events;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import top.ageofelysian.voterewards.Objects.UserEntry;
import top.ageofelysian.voterewards.Utilities.GeneralUtils;
import top.ageofelysian.voterewards.VoteRewards;

public class VoteEvent implements Listener {
    private final GeneralUtils utils = new GeneralUtils();

    @EventHandler
    public void onVote(VotifierEvent event) {
        final Vote vote = event.getVote();

        if (vote.getUsername() == null) return;

        Player p = null;
        try {

            p = Bukkit.getPlayerExact(vote.getUsername());
            utils.processVote(p, vote.getServiceName());

        } catch (NullPointerException ignored) {

            for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {

                if (offlinePlayer.getName().equals(vote.getUsername())) {

                    utils.addOfflineVote(offlinePlayer.getUniqueId(), vote);
                    break;

                }

            }
        }
        if (p == null) return;

        final UserEntry entry = utils.getEntry(p.getUniqueId());
        if (entry == null) return;

        // 129.600.000‬ equals 1 day and a half
        if (entry.getLastVoteTime().get(vote.getServiceName()) < (System.currentTimeMillis() - 129_600_000)) {
            entry.resetStreak();
        }

        entry.setLastVoteTime(vote.getServiceName(), System.currentTimeMillis());

        entry.incrementStreak();
        entry.incrementTotal();
        entry.save();

        VoteRewards.getStorage().addUserData(entry);

        final String name = p.getName();
        final String address = vote.getServiceName();

        VoteRewards.getStorage().getConsoleCommands().forEach(command -> {
            if (command.charAt(0) == '/') {
                command = command.replaceFirst("/", "");
            }

            command = command.replaceAll("%player%", name);
            command = command.replaceAll("%site%", address);

            ChatColor.translateAlternateColorCodes('&', command);

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        });
    }
}
