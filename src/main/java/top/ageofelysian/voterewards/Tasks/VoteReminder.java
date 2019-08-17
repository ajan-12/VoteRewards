package top.ageofelysian.voterewards.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import top.ageofelysian.voterewards.VoteRewards;
import top.ageofelysian.voterewards.Objects.UserEntry;

public class VoteReminder implements Runnable {

    @Override
    public void run() {

        for (final Player p : Bukkit.getOnlinePlayers()) {

            final UserEntry entry = VoteRewards.getStorage().getUserData(p.getUniqueId());
            if (entry == null) {

                p.sendMessage(VoteRewards.getStorage().getTag() + " &bYou can vote now on every website supported!");

            } else {

                final StringBuilder notification = new StringBuilder(VoteRewards.getStorage().getTag() + " &bYou can vote now on");

                final boolean[] boo = {false};
                entry.getLastVoteTime().forEach((address, lastVoteTime) -> {

                    //86_400_000 equals 24 hours
                    if (lastVoteTime < (System.currentTimeMillis() - 86_400_000)) {
                        notification.append(" ").append(address);
                        boo[0] = true;
                    }

                });

                if (boo[0]) {

                    notification.append(".");
                    p.sendMessage(notification.toString());

                }
            }
        }

    }
}
