package me.switching;

import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Main extends JavaPlugin implements TabExecutor, Listener {
    private ArrayList<Player> players;
    boolean switching = false;
    int time = 0;
    BossBar bar;
    final String italic = ChatColor.ITALIC.toString();

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("switching").setExecutor(this);
        getCommand("switching").setTabCompleter(this);
        players = new ArrayList<>();
    }

    public void onDisable() {
        if (bar != null) bar.removeAll();
        if (switching) Bukkit.broadcastMessage("Switching " + ChatColor.GRAY + italic + "disabled");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("start")) {
                if (!switching) {
                    try {
                        int interval = Integer.parseInt(args[1]);
                        time = interval;
                        if (interval >= 5) {
                            switching = true;
                            Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
                                public void run() {
                                    switchPlayers();
                                }
                            },  interval * 20L, interval * 20L);
                            Bukkit.broadcastMessage("Switching " + ChatColor.GREEN + "started!");
                            for (Player online : Bukkit.getOnlinePlayers())
                                online.playSound(online, Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 2);
                            bar = Bukkit.createBossBar("Switching in: " + time, BarColor.WHITE, BarStyle.SEGMENTED_10);
                            for (Player online : Bukkit.getOnlinePlayers())
                                bar.addPlayer(online);
                            Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
                                public void run() {
                                    if (time == 0) {
                                        time = interval;
                                        for (Player online : Bukkit.getOnlinePlayers())
                                            online.playSound(online, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 2);
                                    } else if (time <= 5)
                                        for (Player online : Bukkit.getOnlinePlayers())
                                            online.playSound(online, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                                    bar.setTitle("Switching in: " + time);
                                    bar.setProgress(time * 1.0D / interval);
                                    time--;
                                }
                            },  0L, 20L);
                        } else
                            sender.sendMessage("Do at least " + ChatColor.YELLOW + "5 " + ChatColor.WHITE + "seconds");
                    } catch (NumberFormatException ex) {
                        sendInvalid(sender);
                    }
                } else sender.sendMessage("Switching is already " + ChatColor.GREEN + "on");
            } else sendInvalid(sender);
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("stop")) {
                if (switching) {
                    switching = false;
                    Bukkit.getScheduler().cancelTasks(this);
                    if (bar != null) {
                        bar.removeAll();
                        bar = null;
                    }
                    Bukkit.broadcastMessage("Switching " + ChatColor.GRAY + italic + "disabled");
                    for (Player online : Bukkit.getOnlinePlayers())
                        online.playSound(online, Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 0.5F);
                } else sender.sendMessage("Switching is already " + ChatColor.GRAY + italic + "off");
            } else sendInvalid(sender);
        } else sendInvalid(sender);
        return false;
    }

    private void sendInvalid(CommandSender sender)  {
        sender.sendMessage(ChatColor.RED + "Invalid usage! " + ChatColor.WHITE + "Please use:");
        sender.sendMessage("/switching start " + ChatColor.GRAY + italic + "<interval>");
        sender.sendMessage("/switching stop");
    }

    public void switchPlayers() {
        players.clear();
        for (Player online : Bukkit.getOnlinePlayers())
            players.add(online);
        Collections.shuffle(players);
        Player next;
        Player p = players.get(0);
        Location loc = p.getLocation();
        double absAmount = p.getAbsorptionAmount();
        Location bedLoc = p.getBedSpawnLocation();
        float exh = p.getExhaustion();
        float fallDistance = p.getFallDistance();
        int fireTicks = p.getFireTicks();
        int foodLevel = p.getFoodLevel();
        int freezeTicks = p.getFreezeTicks();
        boolean isGliding = p.isGliding();
        boolean isGlowing = p.isGlowing();
        double hp = p.getHealth();
        double hpScale = p.getHealthScale();
        double lastDmg = p.getLastDamage();
        EntityDamageEvent lastDmgCause = p.getLastDamageCause();
        int noDmgTicks = p.getNoDamageTicks();
        int remAir = p.getRemainingAir();
        float saturation = p.getSaturation();
        int starveRate = p.getStarvationRate();
        boolean isSwimming = p.isSwimming();
        int totalXp = p.getTotalExperience();
        int satRegenRate = p.getSaturatedRegenRate();
        int unsatRegenRate = p.getUnsaturatedRegenRate();
        ItemStack[] contents = p.getInventory().getContents();
        Entity vehicle = p.getVehicle();
        ItemStack cursor = p.getItemOnCursor();
        if (cursor.getType() != Material.AIR) p.setItemOnCursor(null);
        Collection<PotionEffect> effects = p.getActivePotionEffects();
        for (int i = 0; i <= players.size() - 1; i++) {
            p = players.get(i);
            if (i == players.size() - 1) {
                //stuff
                p.setAbsorptionAmount(absAmount);
                p.setBedSpawnLocation(bedLoc, true);
                p.setExhaustion(exh);
                p.setFallDistance(fallDistance);
                p.setFireTicks(fireTicks);
                p.setFoodLevel(foodLevel);
                p.setFreezeTicks(freezeTicks);
                p.setGliding(isGliding);
                p.setGlowing(isGlowing);
                p.setHealth(hp);
                p.setHealthScale(hpScale);
                p.setLastDamage(lastDmg);
                p.setLastDamageCause(lastDmgCause);
                p.setNoDamageTicks(noDmgTicks);
                p.setRemainingAir(remAir);
                p.setSaturation(saturation);
                p.setStarvationRate(starveRate);
                p.setSwimming(isSwimming);
                p.setTotalExperience(totalXp);
                p.setSaturatedRegenRate(satRegenRate);
                p.setUnsaturatedRegenRate(unsatRegenRate);
                //loc
                if (vehicle != null) p.teleport(vehicle.getLocation().add(0,1,0));
                else p.teleport(loc);
                //inv
                p.getInventory().setContents(contents);
                if (cursor.getType() != Material.AIR)
                    for (ItemStack item : p.getInventory().addItem(cursor).values())
                        p.getWorld().dropItem(p.getLocation(), item);
                //effects
                for (PotionEffect effect : p.getActivePotionEffects())
                    p.removePotionEffect(effect.getType());
                p.addPotionEffects(effects);
            } else {
                next = players.get(i + 1);
                p.setAbsorptionAmount(next.getAbsorptionAmount());
                p.setBedSpawnLocation(next.getBedSpawnLocation(), true);
                p.setExhaustion(next.getExhaustion());
                p.setFallDistance(next.getFallDistance());
                p.setFireTicks(next.getFireTicks());
                p.setFoodLevel(next.getFoodLevel());
                p.setFreezeTicks(next.getFreezeTicks());
                p.setGliding(next.isGliding());
                p.setGlowing(next.isGlowing());
                p.setHealth(next.getHealth());
                p.setHealthScale(next.getHealthScale());
                p.setLastDamage(next.getLastDamage());
                p.setLastDamageCause(next.getLastDamageCause());
                p.setNoDamageTicks(next.getNoDamageTicks());
                p.setRemainingAir(next.getRemainingAir());
                p.setSaturation(next.getSaturation());
                p.setStarvationRate(next.getStarvationRate());
                p.setSwimming(next.isSwimming());
                p.setTotalExperience(next.getTotalExperience());
                p.setSaturatedRegenRate(next.getSaturatedRegenRate());
                p.setUnsaturatedRegenRate(next.getUnsaturatedRegenRate());
                if (next.getVehicle() != null) p.teleport(next.getVehicle().getLocation().add(0, 1, 0));
                else p.teleport(next.getLocation());
                p.getInventory().setContents(next.getInventory().getContents());
                if (next.getItemOnCursor().getType() != Material.AIR) {
                    p.setItemOnCursor(next.getItemOnCursor());
                    p.getWorld().dropItemNaturally(p.getLocation(), p.getInventory().addItem(next.getItemOnCursor()).get(0));
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (bar != null) bar.addPlayer(event.getPlayer());
    }

    @EventHandler
    public void onDc(PlayerQuitEvent event) {
        if (bar != null) bar.removePlayer(event.getPlayer());
    }

    @Override
    public ArrayList<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        ArrayList<String> l = new ArrayList<>();
        ArrayList<String> ll = new ArrayList<>();
        if (args.length == 1) {
            l.add("start");
            l.add("stop");
            for (String s : l)
                if (s.contains(args[0]))
                    ll.add(s);
        } else l.clear();
        return ll;
    }

}
