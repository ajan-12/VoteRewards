package top.ageofelysian.voterewards.Objects.BonusTypes;

import org.bukkit.OfflinePlayer;

import top.ageofelysian.voterewards.Objects.Bonus;
import top.ageofelysian.voterewards.Objects.UserEntry;
import top.ageofelysian.voterewards.Utilities.GeneralUtils;
import top.ageofelysian.voterewards.VoteRewards;

import java.util.concurrent.ThreadLocalRandom;

public class LuckBonus extends Bonus {

    private random random;

    public LuckBonus(double multiplier, double minimum, double maximum, int limit, double limitMultiplier, int maxLimit, int minimumValue, int maximumValue) {
        super(multiplier, minimum, maximum, limit, limitMultiplier, maxLimit);
        this.random = new random(minimumValue, maximumValue);
    }

    @Override
    public void apply(OfflinePlayer p) {
        GeneralUtils utils = new GeneralUtils();

        UserEntry entry = VoteRewards.getStorage().getUserData(p.getUniqueId());
        if (entry == null) return;

        ThreadLocalRandom random = ThreadLocalRandom.current();
        double mean = (this.random.maximumValue + this.random.minimumValue) / 2.0;
        double variance = this.random.maximumValue - mean;

        double value = random.nextGaussian();
        while (value < -1 || value > 1) {
            value = random.nextGaussian();
        }

        double reward = mean + (value * variance);
        reward = multiplier * reward;

        reward = handleReward(entry, reward);
        if (reward == 0.0) return;

        VoteRewards.getInstance().getEcon().depositPlayer(p, reward);
        utils.broadcast(" &a- " + reward + "$ &6for being lucky!");
    }

    private class random {
        int minimumValue;
        int maximumValue;

        random(int minimumValue, int maximumValue) {
            this.minimumValue = minimumValue;
            this.maximumValue = maximumValue;
        }
    }
}
