package top.ageofelysian.voterewards.Events;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import top.ageofelysian.voterewards.Objects.UserEntry;
import top.ageofelysian.voterewards.Utilities.GeneralUtils;

public class JoinEvent implements Listener {
    private final GeneralUtils utils = new GeneralUtils();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final UUID uuid = event.getPlayer().getUniqueId();
        final UserEntry entry = utils.getEntry(uuid);

        if (entry == null) return;
        if (entry.getOfflineVotes() == null || entry.getOfflineVotes().keySet().size() == 0) return;

        entry.getOfflineVotes().forEach((address, amount) -> {
            for (int i = 1; i <= amount; i++) {
                utils.processVote(event.getPlayer(), address);
            }
        });
        
        //Clears all offline votes after they are processed
        entry.getOfflineVotes().clear();
        
        entry.save();
    }
}
