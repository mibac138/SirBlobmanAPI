package com.SirBlobman.api.utility;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.SirBlobman.api.item.ItemBuilder;

public class ItemUtil {
    public static ItemStack getAir() {
        return new ItemStack(Material.AIR);
    }

    public static ItemStack newItem(Material type) {
    	return newItem(type, 1);
    }
    
    public static ItemStack newItem(Material type, int amount) {
    	return newItem(type, amount, 0);
    }
    
    public static ItemStack newItem(Material type, int amount, int data) {
    	return newItem(type, amount, data, null);
    }

    public static ItemStack newItem(Material type, int amount, int data, String displayName, String... lore) {
    	List<String> loreList = Util.newList(lore);
    	return newItem(type, amount, data, displayName, loreList);
    }
    
    public static ItemStack newItem(Material type, int amount, int data, String displayName, List<String> lore) {
    	ItemBuilder builder = new ItemBuilder().setType(type).setAmount(amount).setDamage(data);
    	if(displayName != null && !displayName.isEmpty()) builder = builder.setDisplayName(displayName);
    	if(lore != null && !lore.isEmpty()) builder = builder.setLore(lore);
    	return builder.create();
    }

    public static ItemStack newPotion(PotionEffectType main, PotionEffect[] potionEffects, String displayName, String... lore) {
    	ItemStack item = newItem(Material.POTION, 1, 0, displayName, lore);
    	ItemMeta meta = item.getItemMeta();
    	
    	PotionMeta potion = (PotionMeta) meta;
    	potion.setMainEffect(main);
    	for(PotionEffect effect : potionEffects) potion.addCustomEffect(effect, true);
    	
    	item.setItemMeta(potion);
    	return item;
    }
    
    public static ItemStack conditionalMetaItem(boolean condition, Material type, int amount, int metaTrue, int metaFalse, String displayName, String... lore) {
    	return newItem(type, amount, (condition ? metaTrue : metaFalse), displayName, lore);
    }
    
    /* Item Checks */

    /**
     * @param item The item to check
     * @return {@code true} if the item is null or has an AIR material, {@code false} otherwise.
     */
    public static boolean isAir(ItemStack item) {
        if(item == null) return true;
        if(item.equals(getAir())) return true;
        
        Material type = item.getType();
        String typeName = type.name();
        return (type == Material.AIR || typeName.endsWith("_AIR"));
    }

    public static boolean hasMeta(ItemStack item) {
        if(isAir(item)) return false;
        return item.hasItemMeta();
    }
    
    public static boolean hasLore(ItemStack item) {
        if(!hasMeta(item)) return false;
        
        ItemMeta meta = item.getItemMeta();
        return meta.hasLore();
    }

    public static boolean hasDisplayName(ItemStack item) {
        if(!hasMeta(item)) return false;

        ItemMeta meta = item.getItemMeta();
        return meta.hasDisplayName();
    }
    
    public static boolean doesAnyLoreLineContain(ItemStack item, String string) {
        if(!hasLore(item)) return false;
        
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = meta.getLore();
        
        for(String lore : loreList) {
            if(!lore.contains(string)) continue;
            return true;
        }
        return false;
    }
    
    public static boolean doesAnyLoreLineStartWith(ItemStack item, String string) {
        if(!hasLore(item)) return false;
        
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = meta.getLore();
        
        for(String lore : loreList) {
            if(!lore.startsWith(string)) continue;
            return true;
        }
        return false;
    }
    
    public static boolean doesAnyLoreLineEndWith(ItemStack item, String string) {
        if(!hasLore(item)) return false;
        
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = meta.getLore();
        
        for(String lore : loreList) {
            if(!lore.endsWith(string)) continue;
            return true;
        }
        return false;
    }

    public static boolean areLoresEqual(ItemStack item1, ItemStack item2) {
        if(!hasLore(item1) || !hasLore(item2)) return false;

        ItemMeta meta1 = item1.getItemMeta();
        ItemMeta meta2 = item2.getItemMeta();

        List<String> lore1 = meta1.getLore();
        List<String> lore2 = meta2.getLore();
        return lore1.containsAll(lore2);
    }

    public static boolean areNamesEqual(ItemStack item1, ItemStack item2) {
        if(!hasDisplayName(item1) || !hasDisplayName(item2)) return false;

        ItemMeta meta1 = item1.getItemMeta();
        ItemMeta meta2 = item2.getItemMeta();

        String display1 = meta1.getDisplayName();
        String display2 = meta2.getDisplayName();
        return display1.equals(display2);
    }
}