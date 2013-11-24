package com.demoxin.minecraft.moreenchants;

import net.minecraftforge.common.Configuration;

public class Config
{
    private int defaultEnchantID = 60;
    
    // Weapons
    public final int enchMendingID;
    public final boolean enchMendingEnable;
    public final int enchPoisonID;
    public final boolean enchPoisonEnable;
    public final int enchVenomID;
    public final boolean enchVenomEnable;
    public final int enchDefusingID;
    public final boolean enchDefusingEnable;
    public final int enchCleaveID;
    public final boolean enchCleaveEnable;
    
    // Armor
    public final int enchPoisonProtectID;
    public final boolean enchPoisonProtectEnable;
    public final int enchKnowledgeID;
    public final boolean enchKnowledgeEnable;
    public final int enchBerserkID;
    public final boolean enchBerserkEnable;
    public final int enchAgilityID;
    public final boolean enchAgilityEnable;
    public final int enchFleetfootID;
    public final boolean enchFleetfootEnable;
 
    public Config(Configuration config)
    {
        config.load();
        
        // Weapons
        enchMendingID = config.get("Weapons", "MendingID", defaultEnchantID).getInt(defaultEnchantID);
        enchMendingEnable = config.get("Weapons", "MendingEnable", true).getBoolean(true);
        enchPoisonID = config.get("Weapons", "PoisonID", defaultEnchantID+1).getInt(defaultEnchantID+1);
        enchPoisonEnable = config.get("Weapons", "PoisonEnable", true).getBoolean(true);
        enchVenomID = config.get("Weapons", "VenomID", defaultEnchantID+2).getInt(defaultEnchantID+2);
        enchVenomEnable = config.get("Weapons", "VenomEnable", true).getBoolean(true);
        enchDefusingID = config.get("Weapons", "DefusingID", defaultEnchantID+3).getInt(defaultEnchantID+3);
        enchDefusingEnable = config.get("Weapons", "DefusingEnable", true).getBoolean(true);
        enchCleaveID = config.get("Weapons", "CleaveID", defaultEnchantID+4).getInt(defaultEnchantID+4);
        enchCleaveEnable = config.get("Weapons", "CleaveEnable", true).getBoolean(true);
        
        // Armor
        enchPoisonProtectID = config.get("Armor", "PoisonProtectionID", defaultEnchantID+5).getInt(defaultEnchantID+5);
        enchPoisonProtectEnable = config.get("Armor", "PoisonProtectEnable", true).getBoolean(true);
        enchKnowledgeID = config.get("Armor", "KnowledgeID", defaultEnchantID+6).getInt(defaultEnchantID+6);
        enchKnowledgeEnable = config.get("Armor", "KnowledgeEnable", true).getBoolean(true);
        enchBerserkID = config.get("Armor", "BerserkingID", defaultEnchantID+7).getInt(defaultEnchantID+7);
        enchBerserkEnable = config.get("Armor", "BerserkEnable", true).getBoolean(true);
        enchAgilityID = config.get("Armor", "AgilityID", defaultEnchantID+8).getInt(defaultEnchantID+8);
        enchAgilityEnable = config.get("Armor", "AgilityEnable", true).getBoolean(true);
        enchFleetfootID = config.get("Armor", "FleetfootID", defaultEnchantID+9).getInt(defaultEnchantID+9);
        enchFleetfootEnable = config.get("Armor", "FleetfootEnable", true).getBoolean(true);
               
        if(config.hasChanged())
        {
            config.save();
        }
    }
}