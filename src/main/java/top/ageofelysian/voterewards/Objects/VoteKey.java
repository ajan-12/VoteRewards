package top.ageofelysian.voterewards.Objects;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import top.ageofelysian.voterewards.Utilities.GeneralUtils;

import java.util.concurrent.ThreadLocalRandom;

public class VoteKey {
    private ItemStack key;
    private int rarityModifier;

    public VoteKey(ItemStack key, int rarityModifier) {
        this.key = key;
        this.rarityModifier = rarityModifier;
    }

    public void apply(Player p) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int amount = random.nextInt(0, 6) + rarityModifier;

        if (amount == 0) return;

        key.setAmount(amount);
        p.getInventory().addItem(key);

        GeneralUtils utils = new GeneralUtils();

        if (amount > 1) {

            utils.broadcast(" &e- &b" + amount + " " + key.getItemMeta().getDisplayName() + "&6's for being lucky!");

        } else {

            utils.broadcast(" &e- &b1 " + key.getItemMeta().getDisplayName() + " &6for being lucky!");

        }
    }
}
