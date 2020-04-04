package com.SirBlobman.api.nms;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import net.minecraft.server.v1_12_R1.MojangsonParseException;
import net.minecraft.server.v1_12_R1.MojangsonParser;
import net.minecraft.server.v1_12_R1.NBTTagCompound;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

public class ItemHandler_1_12_R1 extends ItemHandler {
    private static final UUID CUSTOM_HEAD_UUID = UUID.fromString("8f0fa41c-ef7a-4cda-80e7-c7b6109c2b6f");
    public ItemHandler_1_12_R1(JavaPlugin plugin) {
        super(plugin);
    }
    
    @Override
    public Material matchMaterial(String string) {
        return Material.matchMaterial(string);
    }
    
    @Override
    public String toNBT(ItemStack item) {
        NBTTagCompound nbtData = new NBTTagCompound();
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        
        nmsItem.save(nbtData);
        return nbtData.toString();
    }
    
    @Override
    public ItemStack fromNBT(String string) {
        try {
            NBTTagCompound nbtData = MojangsonParser.parse(string);
            net.minecraft.server.v1_12_R1.ItemStack nmsItem = new net.minecraft.server.v1_12_R1.ItemStack(nbtData);
            return CraftItemStack.asBukkitCopy(nmsItem);
        } catch(MojangsonParseException ex) {
            JavaPlugin plugin = getPlugin();
            Logger logger = plugin.getLogger();
            logger.log(Level.WARNING, "Failed to parse an NBT string to an item, returning AIR...", ex);
            return new ItemStack(Material.AIR);
        }
    }
    
    @Override
    public ItemStack setCustomNBT(ItemStack item, String key, String value) {
        if(item == null || key == null || key.isEmpty() || value == null) return item;
        
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtData = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        if(nbtData == null) nbtData = new NBTTagCompound();
        
        JavaPlugin plugin = getPlugin();
        String pluginName = plugin.getName();
        
        NBTTagCompound customData = nbtData.getCompound(pluginName);
        customData.setString(key, value);
        nbtData.set(pluginName, customData);
        
        nmsItem.setTag(nbtData);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }
    
    @Override
    public String getCustomNBT(ItemStack item, String key, String defaultValue) {
        if(item == null || key == null || key.isEmpty()) return defaultValue;
    
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtData = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        if(nbtData == null) nbtData = new NBTTagCompound();
    
        JavaPlugin plugin = getPlugin();
        String pluginName = plugin.getName();
    
        NBTTagCompound customData = nbtData.getCompound(pluginName);
        String string = customData.getString(key);
        return (string == null ? defaultValue : string);
    }
    
    @Override
    public ItemStack removeCustomNBT(ItemStack item, String key) {
        if(item == null || key == null || key.isEmpty()) return item;
    
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtData = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        if(nbtData == null) nbtData = new NBTTagCompound();
    
        JavaPlugin plugin = getPlugin();
        String pluginName = plugin.getName();
    
        NBTTagCompound customData = nbtData.getCompound(pluginName);
        customData.remove(key);
        
        if (customData.isEmpty()) nbtData.remove(pluginName);
        else nbtData.set(pluginName, customData);
    
        nmsItem.setTag(nbtData);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public ItemStack getPlayerHead(String username) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(username);
        return getPlayerHead(player);
    }
    
    @Override
    public ItemStack getPlayerHead(OfflinePlayer player) {
        short playerHead = 3;
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, playerHead);
    
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(player);
    
        item.setItemMeta(meta);
        return item;
    }
    
    @Override
    public ItemStack getBase64Head(String base64) {
        GameProfile gameProfile = new GameProfile(CUSTOM_HEAD_UUID, "Base64");
        PropertyMap properties = gameProfile.getProperties();
    
        Property property = new Property("textures", base64);
        properties.put("textures", property);
    
    
        short playerHead = 3;
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, playerHead);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        
        try {
            Class<? extends SkullMeta> metaClass = meta.getClass();
            Field profileField = metaClass.getDeclaredField("profile");
            
            profileField.setAccessible(true);
            profileField.set(meta, gameProfile);
        } catch(ReflectiveOperationException ex) {
            JavaPlugin plugin = getPlugin();
            Logger logger = plugin.getLogger();
            logger.log(Level.WARNING, "An error occurred while creating a Base64 head.", ex);
            return item;
        }
        
        item.setItemMeta(meta);
        return item;
    }
}