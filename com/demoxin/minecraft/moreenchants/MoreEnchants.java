package com.demoxin.minecraft.moreenchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = MoreEnchants.MODID, name = MoreEnchants.NAME, version = MoreEnchants.VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class MoreEnchants {
	// Mod Info
	public static final String MODID = "MoreEnchants";
	public static final String NAME = "MoreEnchants";
	public static final String VERSION = "1.0";
	// Mod Info End
	
	// Singleton
	@Instance("MoreEnchants")
	public static MoreEnchants instance;
	
	public static Enchantment enchantMending;
	public static Enchantment enchantPoison;
	public static Enchantment enchantVenom;
	public static Enchantment enchantDefusing;
	public static Enchantment enchantCleave;
	
	public static Enchantment enchantPoisonProtect;
	public static Enchantment enchantKnowledge;
	public static Enchantment enchantBerserk;
	public static Enchantment enchantAgility;
	public static Enchantment enchantFleetfoot;
	
	// Config
	public static Config config;
	
    @EventHandler
    public void preInit(FMLPreInitializationEvent fEvent)
    {
    	config = new Config(new Configuration(fEvent.getSuggestedConfigurationFile()));
    }
    
    @EventHandler
    public void load(FMLInitializationEvent fEvent)
    {
    	if(config.enchMendingEnable)
    		enchantMending = new Enchantment_Mending(config.enchMendingID, 3);
    	
    	if(config.enchPoisonEnable)
    		enchantPoison = new Enchantment_Poison(config.enchPoisonID, 2);
    	
    	if(config.enchVenomEnable)
	    	enchantVenom = new Enchantment_Venom(config.enchVenomID, 2);
	    	
    	if(config.enchDefusingEnable)
	    	enchantDefusing = new Enchantment_Defusing(config.enchDefusingID, 5);
	    	
    	if(config.enchCleaveEnable)
	    	enchantCleave = new Enchantment_Cleave(config.enchCleaveID, 2);
	    	
	    	
	    if(config.enchPoisonProtectEnable)
	    	enchantPoisonProtect = new Enchantment_PoisonProtect(config.enchPoisonProtectID, 5);
	    	
	    if(config.enchKnowledgeEnable)
	    	enchantKnowledge = new Enchantment_Knowledge(config.enchKnowledgeID, 1);
	    	
	    if(config.enchBerserkEnable)
	    	enchantBerserk = new Enchantment_Berserking(config.enchBerserkID, 1);
	    	
	    if(config.enchAgilityEnable)
	    	enchantAgility = new Enchantment_Agility(config.enchAgilityID, 1);
	    	
	    if(config.enchFleetfootEnable)
	    	enchantFleetfoot = new Enchantment_Fleetfooted(config.enchFleetfootID, 1);
	}
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent fEvent)
    {
    	MinecraftForge.EVENT_BUS.register(new EnchantProcessor());
    }
}
