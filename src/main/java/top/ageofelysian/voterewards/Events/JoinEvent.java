package top.ageofelysian.voterewards.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import top.ageofelysian.voterewards.Objects.UserEntry;
import top.ageofelysian.voterewards.Utilities.GeneralUtils;
import top.ageofelysian.voterewards.VoteRewards;

import java.util.UUID;

public class JoinEvent implements Listener {
    private final GeneralUtils utils = new GeneralUtils();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getLogger().info("debug");

        final UUID uuid = event.getPlayer().getUniqueId();
        final UserEntry entry = utils.getEntry(uuid);

        if (entry == null) return;
        if (entry.getOfflineVotes() == null || entry.getOfflineVotes().keySet().size() == 0) return;

        entry.getOfflineVotes().forEach((address, amount) -> {
            for (int i = 1; i <= amount; i++) {
                utils.processVote(event.getPlayer(), address);
            }
        });

        // 86.400.000 equals 1 whole day
        if (entry.getLastVoteTime() < (System.currentTimeMillis() - 86_400_000)) {
            Bukkit.getScheduler().runTaskLater(VoteRewards.getInstance(), () -> event.getPlayer().sendMessage(VoteRewards.getStorage().getTag() + ChatColor.AQUA + " You can now vote!"), 3000);
        }
    }
}
