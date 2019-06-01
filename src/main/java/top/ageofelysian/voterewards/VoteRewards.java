package top.ageofelysian.voterewards;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import top.ageofelysian.voterewards.Commands.MainCommand;
import top.ageofelysian.voterewards.Events.*;
import top.ageofelysian.voterewards.Objects.*;
import top.ageofelysian.voterewards.Tasks.VoteReminder;
import top.ageofelysian.voterewards.Utilities.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class VoteRewards extends JavaPlugin {
    private static VoteRewards instance = null;
    private static DataStorage storage = null;
    private Economy econ = null;

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

        //Starting the VoteReminder
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new VoteReminder(), 12_000, 12_000);
    }

    @Override
    public void onDisable() {

        PlayerDataHandle handler = new PlayerDataHandle();
        handler.saveUserData(new File(getDataFolder().getAbsolutePath() + File.separator + "PlayerData.yml"));

        storage = null;
        instance = null;
    }

    public void initDataStorage() {
        if (storage != null) storage = null;

        final ConfigUtils utils = new ConfigUtils();
        utils.init(getConfig());

        final PlayerDataHandle getter = new PlayerDataHandle();

        final double baseReward = utils.baseReward;
        final HashMap<String, VoteKey> voteKeys = utils.voteKeys;
        final HashMap<String, Bonus> bonuses = utils.bonuses;

        final List<String> consoleCommands = utils.consoleCommands;
        final List<String> playerCommands = utils.playerCommands;

        final HashSet<UserEntry> userData = getter.getUserData(new File(getDataFolder().getAbsolutePath() + File.separator + "PlayerData.yml"));

        storage = new DataStorage(baseReward, voteKeys, bonuses, userData, consoleCommands, playerCommands);
    }

    public static DataStorage getStorage() {
        return storage;
    }

    public static VoteRewards getInstance() {
        return instance;
    }

    public Economy getEcon() {
        return econ;
    }
}
