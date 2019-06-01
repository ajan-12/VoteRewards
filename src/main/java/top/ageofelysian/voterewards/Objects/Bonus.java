package top.ageofelysian.voterewards.Objects;

import org.bukkit.OfflinePlayer;

public abstract class Bonus {
    private voteLimit limit;

    protected double multiplier;
    private double minimum;
    private double maximum;

    public Bonus(double multiplier, double minimum, double maximum, int limit, double limitMultiplier, int maxLimit) {
        this.multiplier = multiplier;
        this.minimum = minimum;
        this.maximum = maximum;

        if (limit == 0 || limitMultiplier == 0 || maxLimit == 0) return;
        this.limit = new voteLimit(limit, limitMultiplier, maxLimit);
    }

    public abstract void apply(OfflinePlayer p);

    private boolean limitExceeds(UserEntry entry) {
        return (limit.limit < entry.getTotal());
    }

    private boolean maxLimitExceeds(UserEntry entry) {
        return (limit.maxLimit < entry.getTotal());
    }

    protected double handleReward(UserEntry entry, double reward) {
        if (reward < minimum) {
            return 0.0;
        }
        if (reward > maximum) {
            reward = maximum;
        }

        if (limit != null) {
            if (maxLimitExceeds(entry)) return 0.0;
            if (limitExceeds(entry)) {
                reward = limit.multiplier * reward;
            }
        }
        return reward;
    }

    private class voteLimit {
        int limit;
        double multiplier;
        int maxLimit;

        voteLimit(int limit, double multiplier, int maxLimit) {
            this.limit = limit;
            this.multiplier = multiplier;
            this.maxLimit = maxLimit;
        }
    }
}
