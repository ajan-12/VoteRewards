package top.ageofelysian.voterewards.Objects.BonusTypes;

import org.bukkit.OfflinePlayer;

import top.ageofelysian.voterewards.Objects.Bonus;
import top.ageofelysian.voterewards.Objects.UserEntry;
import top.ageofelysian.voterewards.Utilities.GeneralUtils;
import top.ageofelysian.voterewards.VoteRewards;

public class VotecountBonus extends Bonus {

    public VotecountBonus(double multiplier, double minimum, double maximum, int limit, double limitMultiplier, int maxLimit) {
        super(multiplier, minimum, maximum, limit, limitMultiplier, maxLimit);
    }

    @Override
    public void apply(OfflinePlayer p) {
        GeneralUtils utils = new GeneralUtils();

        UserEntry entry = utils.getEntry(p.getUniqueId());
        if (entry == null) return;

        int total = entry.getTotal();
        double reward = multiplier * total;

        reward = handleReward(entry, reward);
        if (reward == 0.0) return;

        VoteRewards.getInstance().getEcon().depositPlayer(p, reward);
        utils.broadcast(" &a- " + reward + "$ &6as total vote-count bonus");
    }
}
