package top.ageofelysian.voterewards.Utilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import top.ageofelysian.voterewards.Objects.Bonus;
import top.ageofelysian.voterewards.Objects.BonusTypes.LuckBonus;
import top.ageofelysian.voterewards.Objects.BonusTypes.StreakBonus;
import top.ageofelysian.voterewards.Objects.BonusTypes.VotecountBonus;
import top.ageofelysian.voterewards.Objects.VoteKey;

import java.util.HashMap;
import java.util.List;

public class ConfigUtils {
    public double baseReward = 0.0;
    public HashMap<String, VoteKey> voteKeys;
    public HashMap<String, Bonus> bonuses;
    public List<String> consoleCommands;
    public List<String> playerCommands;

    public ConfigUtils() { }

    public void init(FileConfiguration config) {
        baseReward = config.getDouble("base-reward", 200.0);

        if (config.getBoolean("enable-vote-keys")) {
            voteKeys = getKeys(config);
        }

        if (config.getBoolean("enable-bonus")) {
            bonuses = getBonuses(config);
        }

        if (config.getStringList("commands") != null) {
            consoleCommands = config.getStringList("commands");
        }

        if (config.getStringList("sudo-commands") != null) {
            playerCommands = config.getStringList("sudo-commands");
        }
    }

    private HashMap<String, VoteKey> getKeys(FileConfiguration config) {
        HashMap<String, VoteKey> voteKeys = new HashMap<>();

        try {
            if (config.getConfigurationSection("vote-keys").getKeys(false).size() == 0) return null;
        } catch (NullPointerException e) {
            return null;
        }

        for (String key : config.getConfigurationSection("vote-keys").getKeys(false)) {

            Material type = Material.getMaterial(config.getString("vote-keys." + key + ".Type"));
            ItemStack stack = new ItemStack(type);

            ItemMeta meta = stack.getItemMeta();

            String displayName = ChatColor.translateAlternateColorCodes('&', config.getString("vote-keys." + key + ".Display-name"));
            meta.setDisplayName(displayName);

            List<String> lores = config.getStringList("vote-keys." + key + ".Lores");
            for (int i = 0; i < lores.size(); i++) {
                lores.set(i, ChatColor.translateAlternateColorCodes('&', lores.get(i)));
            }
            meta.setLore(lores);

            for (String enchant : config.getConfigurationSection("vote-keys." + key + ".Enchantments").getKeys(false)) {
				if(Enchantment.getByKey(NamespacedKey.minecraft(enchant)) != null) {
					meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft(enchant)), config.getInt("vote-keys." + key + ".Enchantments." + enchant + ".level"), true);
				}
            }

            boolean hideEnchs = config.getBoolean("vote-keys." + key + ".hide-enchantments", false);
            if (hideEnchs) meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            stack.setItemMeta(meta);

            VoteKey voteKey = new VoteKey(stack, config.getInt("vote-keys." + key + ".rarity", 0));

            voteKeys.put(key, voteKey);
        }

        return voteKeys;
    }

    private HashMap<String, Bonus> getBonuses(FileConfiguration config) {
        HashMap<String, Bonus> bonuses = new HashMap<>();

        try {
            if (config.getConfigurationSection("bonuses").getKeys(false).size() == 0) return null;
        } catch (NullPointerException e) {
            return null;
        }

        for (String key : config.getConfigurationSection("bonuses").getKeys(false)) {

            int mode = 3;
            switch (key) {
                case "vote-streak":
                    mode = 0;
                    break;
                case "total-vote-count":
                    mode = 1;
                    break;
                case "luck-bonus":
                    mode = 2;
                    break;
            }

            if (mode == 3) continue;

            double multiplier = config.getDouble("bonuses." + key + ".multiplier", 50.0);
            double minimum = config.getDouble("bonuses." + key + ".minimum", 250.0);
            double maximum = config.getDouble("bonuses." + key + ".maximum", 1000.0);

            int limit = 0;
            double limitMultiplier = 0.0;
            int maxLimit = 0;

            int maxValue = 0;
            int minValue = 0;

            if (config.getConfigurationSection("bonuses." + key).getKeys(false).contains("vote-limit")) {

                if (config.getConfigurationSection("bonuses." + key).getKeys(false).size() == 4) {

                    limit = config.getInt("bonuses." + key + ".vote-limit.limit", 10);
                    limitMultiplier = config.getDouble("bonuses." + key + ".vote-limit.multiplier", 0.5);
                    maxLimit = config.getInt("bonuses." + key + ".vote-limit.max-limit", 50);

                } else if (config.getConfigurationSection("bonuses." + key).getKeys(false).size() == 5 && config.getConfigurationSection("bonuses." + key).getKeys(false).contains("random-number-generator") && mode == 2) {

                    maxValue = config.getInt("bonuses." + key + ".random-number-generator.max-value", 200);
                    minValue = config.getInt("bonuses." + key + ".random-number-generator.min-value", 100);

                }
            }

            Bonus bonus;
            if (mode == 2) {

                bonus = new LuckBonus(multiplier, minimum, maximum, limit, limitMultiplier, maxLimit, minValue, maxValue);

            } else if (mode == 1) {

                bonus = new VotecountBonus(multiplier, minimum, maximum, limit, limitMultiplier, maxLimit);

            } else {

                bonus = new StreakBonus(multiplier, minimum, maximum, limit, limitMultiplier, maxLimit);

            }

            bonuses.put(key, bonus);

        }

        return bonuses;
    }
}
