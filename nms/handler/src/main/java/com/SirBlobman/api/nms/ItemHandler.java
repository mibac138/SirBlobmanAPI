package com.SirBlobman.api.nms;

import java.util.Base64;
import java.util.Base64.Encoder;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ItemHandler {
    private final JavaPlugin plugin;
    public ItemHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public final JavaPlugin getPlugin() {
        return this.plugin;
    }
    
    /**
     * @param url The mojang texture URL
     * @return A custom head {@link ItemStack} with the proper texture
     */
    public final ItemStack getTextureHead(String url) {
        String base64 = encodeTextureURL(url);
        return getBase64Head(base64);
    }
    
    protected final String encodeTextureURL(String url) {
        byte[] urlByteArray = url.getBytes();
        Encoder encoder = Base64.getEncoder();
        
        byte[] base64ByteArray = encoder.encode(urlByteArray);
        return new String(base64ByteArray);
    }
    
    /**
     * @param string The name of the material to check
     * @return A valid material if one matches the string, otherwise {@code null}
     */
    public abstract Material matchMaterial(String string);
    
    /**
     * @param item The {@link ItemStack} to get the name for
     * @return The display name or internal name of the item
     */
    public abstract String getLocalizedName(ItemStack item);
    
    public abstract String toNBT(ItemStack item);
    public abstract ItemStack fromNBT(String string);
    
    public abstract ItemStack setCustomNBT(ItemStack item, String key, String value);
    public abstract String getCustomNBT(ItemStack item, String key, String defaultValue);
    public abstract ItemStack removeCustomNBT(ItemStack item, String key);
    
    public abstract ItemStack getPlayerHead(String username);
    public abstract ItemStack getPlayerHead(OfflinePlayer player);
    public abstract ItemStack getBase64Head(String base64);
    
    /**
     * Set the data/damage/durability of an {@link ItemStack}.
     * Some items can't have durability applied
     *
     * @param item The item to set the value of
     * @param damage The value to set
     */
    public abstract void setDamage(ItemStack item, int damage);
}