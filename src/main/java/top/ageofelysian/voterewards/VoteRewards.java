package top.ageofelysian.voterewards;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import top.ageofelysian.voterewards.Commands.MainCommand;
import top.ageofelysian.voterewards.Events.*;
import top.ageofelysian.voterewards.Objects.*;
import top.ageofelysian.voterewards.Utilities.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class VoteRewards extends JavaPlugin {
    private static VoteRewards instance = null;
    private static DataStorage storage = null;
    private Economy econ = null;

    public static DataStorage getStorage() {
        return storage;
    }

    public static VoteRewards getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        ///////////////////////////////!!!  DEPENDENCY CHECKS  !!!/////////////////////////////////
        //Note: Did the following thing only for visual.

        boolean vault = false;
        boolean votifier = false;

        //Checking for Vault
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) vault = true;
        //Checking for Votifier
        if (Bukkit.getServer().getPluginManager().getPlugin("Votifier") == null) votifier = true;

        if (vault && votifier) {

            Bukkit.getLogger().warning("The plugins, Votifier and Vault are needed for this plugin!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;

        } else if (vault) {

            Bukkit.getLogger().warning("The plugin, Vault is needed for this plugin!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;

        } else if (votifier) {

            Bukkit.getLogger().warning("The plugin, Votifier is needed for this plugin!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;

        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        econ = rsp.getProvider();

        ///////////////////////////////!!!  DATA FILE CHECKS  !!!/////////////////////////////////

        saveDefaultConfig();
        File playerDataFile = new File(getDataFolder().getAbsolutePath() + File.separator + "PlayerData.yml");
        File dataFile = new File(getDataFolder().getAbsolutePath() + File.separator + "Data.yml");

        try {

            if (!playerDataFile.exists()) playerDataFile.createNewFile();
            if (!dataFile.exists()) dataFile.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }


        ///////////////////////////////!!!  OTHER STUFF  !!!/////////////////////////////////

        initDataStorage();

        //Registering Commands
        getCommand("voterewards").setExecutor(new MainCommand());

        //Registering Events
        getServer().getPluginManager().registerEvents(new VoteEvent(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
    }

    public void initDataStorage() {
        if (storage != null) storage = null;

        final ConfigUtils utils = new ConfigUtils();
        utils.init(getConfig());

        final PlayerDataHandle getter = new PlayerDataHandle(new File(getDataFolder().getAbsolutePath() + File.separator + "PlayerData.yml"));

        final double baseReward = utils.getBaseReward();
        final HashMap<String, VoteKey> voteKeys = utils.getVoteKeys();
        final HashMap<String, Bonus> bonuses = utils.getBonuses();
        final HashSet<UserEntry> userData = getter.getUserData();

        storage = new DataStorage(baseReward, voteKeys, bonuses, userData);
    }

    public Economy getEcon() {
        return econ;
    }
}
