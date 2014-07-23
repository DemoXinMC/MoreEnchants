package com.demoxin.minecraft.moreenchants;

import net.minecraftforge.common.config.Configuration;

public class Config
{
    private int defaultEnchantID = 70;
    
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
    
    public final int enchMobilityID;
    public final boolean enchMobilityEnable;
    public final int enchHarvestID;
    public final boolean enchHarvestEnable;
    public final int enchDowsingID;
    public final boolean enchDowsingEnable;
    public final int enchDisjunctionID;
    public final boolean enchDisjunctionEnable;
    public final int enchVorpalID;
    public final boolean enchVorpalEnable;
    public final int enchFrostID;
    public final boolean enchFrostEnable;
    public final int enchIceAspectID;
    public final boolean enchIceAspectEnable;
    public final int enchLeechID;
    public final boolean enchLeechEnable;
    public final int enchExecuteID;
    public final boolean enchExecuteEnable;
    public final int enchExplosiveID;
    public final boolean enchExplosiveEnable;
    public final int enchResurrectionID;
    public final boolean enchResurrectionEnable;
    public final int enchSteadfastID;
    public final boolean enchSteadfastEnable;
    public final int enchSilenceID;
    public final boolean enchSilenceEnable;
    
    public final int enchSpellbaneID;
    public final boolean enchSpellbaneEnable;
    public final int enchQuickdrawID;
    public final boolean enchQuickdrawEnable;
    
    public final int enchRegenID;
    public final boolean enchRegenEnable;
    public final int enchMagnetID;
    public final boolean enchMagnetEnable;
    public final int enchSoulboundID;
    public final boolean enchSoulboundEnable;
    public final int enchHeatID;
    public final boolean enchHeatEnable;
    public final int enchScattershotID;
    public final boolean enchScattershotEnable;
    public final int enchStrikingID;
    public final boolean enchStrikingEnable;
    
    public final boolean charms;
    public final int charmRarity;
 
    public Config(Configuration config)
    {
        config.load();
        
        // Weapons
        enchMendingID = config.get("Weapons", "MendingID", defaultEnchantID).getInt();
        enchMendingEnable = config.get("Weapons", "MendingEnable", true).getBoolean(true);
        enchPoisonID = config.get("Weapons", "PoisonID", defaultEnchantID+1).getInt();
        enchPoisonEnable = config.get("Weapons", "PoisonEnable", true).getBoolean(true);
        enchVenomID = config.get("Weapons", "VenomID", defaultEnchantID+2).getInt();
        enchVenomEnable = config.get("Weapons", "VenomEnable", true).getBoolean(true);
        enchDefusingID = config.get("Weapons", "DefusingID", defaultEnchantID+3).getInt();
        enchDefusingEnable = config.get("Weapons", "DefusingEnable", true).getBoolean(true);
        enchCleaveID = config.get("Weapons", "CleaveID", defaultEnchantID+4).getInt();
        enchCleaveEnable = config.get("Weapons", "CleaveEnable", true).getBoolean(true);
        
        // Armor
        enchPoisonProtectID = config.get("Armor", "PoisonProtectionID", defaultEnchantID+5).getInt();
        enchPoisonProtectEnable = config.get("Armor", "PoisonProtectEnable", true).getBoolean(true);
        enchKnowledgeID = config.get("Armor", "KnowledgeID", defaultEnchantID+6).getInt();
        enchKnowledgeEnable = config.get("Armor", "KnowledgeEnable", true).getBoolean(true);
        enchBerserkID = config.get("Armor", "BerserkingID", defaultEnchantID+7).getInt();
        enchBerserkEnable = config.get("Armor", "BerserkEnable", true).getBoolean(true);
        enchAgilityID = config.get("Armor", "AgilityID", defaultEnchantID+8).getInt();
        enchAgilityEnable = config.get("Armor", "AgilityEnable", true).getBoolean(true);
        enchFleetfootID = config.get("Armor", "FleetfootID", defaultEnchantID+9).getInt();
        enchFleetfootEnable = config.get("Armor", "FleetfootEnable", true).getBoolean(true);
        
        // 1.2
        enchMobilityID = config.get("Armor", "MobilityID", defaultEnchantID+10).getInt();
        enchMobilityEnable = config.get("Armor", "MobilityEnable", true).getBoolean(true);
        enchHarvestID = config.get("Tools", "HarvestID", defaultEnchantID+11).getInt();
        enchHarvestEnable = config.get("Tools", "HarvestEnable", true).getBoolean(true);
        enchDowsingID = config.get("Weapons", "DowsingID", defaultEnchantID+12).getInt();
        enchDowsingEnable = config.get("Weapons", "DowsingEnable", true).getBoolean(true);
        enchDisjunctionID = config.get("Weapons", "DisjunctionID", defaultEnchantID+13).getInt();
        enchDisjunctionEnable = config.get("Weapons", "DisjunctionEnable", true).getBoolean(true);
        enchVorpalID = config.get("Weapons", "VorpalID", defaultEnchantID+14).getInt();
        enchVorpalEnable = config.get("Weapons", "VorpalEnable", true).getBoolean(true);
        enchFrostID = config.get("Weapons", "FrostID", defaultEnchantID+15).getInt();
        enchFrostEnable = config.get("Weapons", "FrostEnable", true).getBoolean(true);
        enchIceAspectID = config.get("Weapons", "IceAspectID", defaultEnchantID+16).getInt();
        enchIceAspectEnable = config.get("Weapons", "IceAspectEnable", true).getBoolean(true);
        enchLeechID = config.get("Weapons", "LeechID", defaultEnchantID+17).getInt();
        enchLeechEnable = config.get("Weapons", "LeechEnable", true).getBoolean(true);
        enchExecuteID = config.get("Weapons", "ExecutionID", defaultEnchantID+18).getInt();
        enchExecuteEnable = config.get("Weapons", "ExecutionEnable", true).getBoolean(true);
        enchExplosiveID = config.get("Weapons", "ExplosiveID", defaultEnchantID+19).getInt();
        enchExplosiveEnable = config.get("Weapons", "ExplosiveEnable", true).getBoolean(true);
        enchResurrectionID = config.get("Books", "ResurrectionID", defaultEnchantID+20).getInt();
        enchResurrectionEnable = config.get("Books", "ResurrectionEnable", true).getBoolean(true);
        enchSteadfastID = config.get("Armor", "SteadfastID", defaultEnchantID+21).getInt();
        enchSteadfastEnable = config.get("Armor", "SteadfastEnable", true).getBoolean(true);
        enchSilenceID = config.get("Weapons", "SilenceID", defaultEnchantID+22).getInt();
        enchSilenceEnable = config.get("Weapons", "SilenceEnable", true).getBoolean(true);
        
        // 1.3
        enchSpellbaneID = config.get("Weapons", "SpellbaneID", defaultEnchantID+23).getInt();
        enchSpellbaneEnable = config.get("Weapons", "SpellbaneEnable", true).getBoolean(true);
        enchQuickdrawID = config.get("Weapons", "QuickdrawID", defaultEnchantID+24).getInt();
        enchQuickdrawEnable = config.get("Weapons", "QuickdrawEnable", true).getBoolean(true);
        
        charms = config.get("Charms", "Enable", true).getBoolean(true);
        charmRarity = config.get("Charms", "Rarity", 200).getInt();
        
        // 1.4
        enchRegenID = config.get("Charms", "RegenerationID", defaultEnchantID+25).getInt();
        enchRegenEnable = config.get("Charms", "RegenerationEnable", true).getBoolean(true);
        enchMagnetID = config.get("Charms", "MagnetizeID", defaultEnchantID+26).getInt();
        enchMagnetEnable = config.get("Charms", "MagnetizeEnable", true).getBoolean(true);
        enchHeatID = config.get("Tools", "HeatTouchedID", defaultEnchantID+27).getInt();
        enchHeatEnable = config.get("Tools", "HeatTouchedEnable", true).getBoolean(true);
        enchSoulboundID = config.get("Misc", "SoulboundID", defaultEnchantID+28).getInt();
        enchSoulboundEnable = config.get("Misc", "SoulboundEnable", true).getBoolean(true);
        enchScattershotID = config.get("Weapons", "ScattershotID", defaultEnchantID+29).getInt();
        enchScattershotEnable = config.get("Weapons", "ScattershotEnable", true).getBoolean(true);
        enchStrikingID = config.get("Weapons", "StrikingID", defaultEnchantID+30).getInt();
        enchStrikingEnable = config.get("Weapons", "StrikingEnable", true).getBoolean(true);
       
        if(config.hasChanged())
        {
            config.save();
        }
    }
}