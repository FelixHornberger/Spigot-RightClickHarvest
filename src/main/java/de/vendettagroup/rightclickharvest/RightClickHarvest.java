package de.vendettagroup.rightclickharvest;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;



public class RightClickHarvest implements Listener {

    private Plugin plugin;

    public RightClickHarvest(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void rightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (!checkBlock(b.getType()) || checkForCoca(b.getType()) && !checkForAxe(p)) {
                return;
            }
            if(checkForCoca(b.getType()) && !checkForAxe(p)) {
                return;
            }
            Ageable ageable = (Ageable) b.getBlockData();
            int actualAge = ageable.getAge();
            if (actualAge == ageable.getMaximumAge()) {
                harvest(b, p);
            }
        }
    }

    private void harvest(Block b, Player p) {
        Material setToBlock = b.getType();
        p.swingMainHand();
        changeOutput(b, p);
        b.breakNaturally();
        b.setType(setToBlock);
        changeItemDurability(b.getType() ,p);
        changeCocaDirection(b);
        playSound(p, b.getType());
    }

    private boolean checkBlock(Material m) {
        return m == Material.WHEAT || m == Material.POTATOES || m == Material.CARROTS || m == Material.BEETROOTS
                || m == Material.NETHER_WART || m== Material.COCOA;
    }

    private boolean checkForCoca(Material m) {
        return m == Material.COCOA;
    }

    private boolean checkForAxe(Player p) {
        p.getInventory().getItemInMainHand();
        switch (p.getInventory().getItemInMainHand().getType()){
            case NETHERITE_AXE:
            case DIAMOND_AXE:
            case IRON_AXE:
                return true;
        }
        return false;
    }

    private void changeItemDurability(Material m,Player p){
        if(p.getGameMode() != GameMode.CREATIVE) {
            if (checkForCoca(m)) {
                ItemStack item = p.getInventory().getItemInMainHand();
                Damageable itemdmg = (Damageable) item.getItemMeta();
                int damage = itemdmg.getDamage() + 1;
                itemdmg.setDamage((short) damage);
                if(!item.getItemMeta().isUnbreakable()) {
                    item.setItemMeta((ItemMeta) itemdmg);
                }
            }
        }
    }

    private boolean checkForJungleLog(Material m) {
        return m == Material.JUNGLE_LOG || m == Material.STRIPPED_JUNGLE_LOG;
    }

    //Normally CoCpaBeansFace North so i wont ask for that direction(Z-1)
    private void changeCocaDirection(Block b) {
        if(checkForCoca(b.getType())) {
            BlockData blockData = b.getBlockData();
            Location cocoaBean = b.getLocation();
            double cocoaBeanX = cocoaBean.getX();
            double cocoaBeanZ = cocoaBean.getZ();
            Location testForJungleLogSouth = cocoaBean;
            testForJungleLogSouth.setZ(cocoaBeanZ-1);
            if(checkForJungleLog(testForJungleLogSouth.getBlock().getType())){
                System.out.println("South");
                return;
            }
            Location testForJungleLogNorth = cocoaBean;
            testForJungleLogNorth.setZ(cocoaBeanZ+1);
            if(checkForJungleLog(testForJungleLogNorth.getBlock().getType())) {
                System.out.println("North");
                ((Directional) blockData).setFacing(BlockFace.SOUTH);
                b.setBlockData(blockData);
                return;
            }
            Location testForJungleLogWest = cocoaBean;
            testForJungleLogWest.setX(cocoaBeanX-1);
            testForJungleLogWest.setZ(cocoaBeanZ);
            if(checkForJungleLog(testForJungleLogWest.getBlock().getType())) {
                System.out.println("West");
                ((Directional) blockData).setFacing(BlockFace.WEST);
                b.setBlockData(blockData);
                return;
            }
            Location testForJungleLogEast = cocoaBean;
            testForJungleLogEast.setX(cocoaBeanX+1);
            testForJungleLogEast.setZ(cocoaBeanZ);
            if(checkForJungleLog(testForJungleLogEast.getBlock().getType())) {
                System.out.println("East");
                ((Directional) blockData).setFacing(BlockFace.EAST);
                b.setBlockData(blockData);
                return;
            }
        }
    }

    private Material getSeed(Material m) {
        switch (m) {
            case WHEAT:
                return Material.WHEAT_SEEDS;
            case POTATOES:
                return Material.POTATO;
            case CARROTS:
                return Material.CARROT;
            case BEETROOTS:
                return Material.BEETROOT;
            case NETHER_WART:
                return Material.NETHER_WART;
            case COCOA:
                return Material.COCOA_BEANS;
        }
        return Material.AIR;
    }

    private void playSound(Player p, Material m) {
        if (m.equals(Material.NETHER_WART)) {
            p.playSound(p.getLocation(), Sound.BLOCK_NETHER_WART_BREAK, 10, 1);
            p.playSound(p.getLocation(), Sound.ITEM_NETHER_WART_PLANT, 8, 1);
        } else {
            p.playSound(p.getLocation(), Sound.BLOCK_CROP_BREAK, 10, 1);
            p.playSound(p.getLocation(), Sound.ITEM_CROP_PLANT, 8, 1);
        }
    }

    private void changeOutput(Block b, Player p) {
        boolean seedInDrop = false;
        Material seed = getSeed(b.getType());
        for (ItemStack is : b.getDrops()) {
            if (is.getType() == seed) {
                seedInDrop = true;
            }
        }
        if (seedInDrop) {
            b.getDrops().remove(new ItemStack(seed,1));
        } else {
            p.getInventory().remove(new ItemStack(seed, 1));
        }
    }

}
