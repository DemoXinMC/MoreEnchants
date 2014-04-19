package com.demoxin.minecraft.moreenchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = MoreEnchants.MODID, name = MoreEnchants.NAME, version = MoreEnchants.VERSION)
public class MoreEnchants {
	// Mod Info
	public static final String MODID = "MoreEnchants";
	public static final String NAME = "MoreEnchants";
	public static final String VERSION = "1.4.0";
	// Mod Info End
	
	// Singleton
	@Instance("MoreEnchants")
	public static MoreEnchants instance;
	
	public static Enchantment enchantPoison;
	public static Enchantment enchantVenom;
	public static Enchantment enchantFrost;
	public static Enchantment enchantIceAspect;
	public static Enchantment enchantDefusing;
	public static Enchantment enchantDisjunction;
	public static Enchantment enchantDowsing;
	public static Enchantment enchantMending;
	public static Enchantment enchantCleave;
	public static Enchantment enchantExplosive;
	public static Enchantment enchantLeech;
	public static Enchantment enchantVorpal;
	public static Enchantment enchantSilence;
	public static Enchantment enchantExecution;
	
	public static Enchantment enchantPoisonProtect;
	public static Enchantment enchantKnowledge;
	public static Enchantment enchantBerserk;
	public static Enchantment enchantSteadfast;
	public static Enchantment enchantAgility;
	public static Enchantment enchantMobility;
	public static Enchantment enchantFleetfoot;
	
	public static Enchantment enchantHarvest;
	public static Enchantment enchantResurrection;
	
	public static Enchantment enchantSpellbane;
	public static Enchantment enchantQuickdraw;
	
	public static Enchantment enchantRegen;
	public static Enchantment enchantMagnet;
	public static Enchantment enchantSoulbound;
	public static Enchantment enchantHeat;
	public static Enchantment enchantScattershot;
	public static Enchantment enchantStriking;
	
	public static Item itemCharm;
	
	// bits
	public static int bitMENDING = 1;
	public static int bitMENDING2 = 2;
	public static int bitPOISON = 4;
	public static int bitEXPLODE = 8;
	public static int bitICE = 16;
	
	// Config
	public static Config config;
	
    @EventHandler
    public void preInit(FMLPreInitializationEvent fEvent)
    {
    	config = new Config(new Configuration(fEvent.getSuggestedConfigurationFile()));
    	
    	if(config.charms)
	    {
	    	itemCharm = new Item_Charm(config.charmRarity);
	    	GameRegistry.registerItem(itemCharm, "charm", MODID);
	    }
    }
    
    @EventHandler
    public void load(FMLInitializationEvent fEvent)
    {   	
    	if(config.enchPoisonEnable)
    		enchantPoison = new Enchantment_Poison(config.enchPoisonID, 2);
    	
    	if(config.enchFrostEnable)
    		enchantFrost = new Enchantment_Frost(config.enchFrostID, 2);
    	
    	if(config.enchExplosiveEnable)
    		enchantExplosive = new Enchantment_Explosive(config.enchExplosiveID, 1);
    	
    	if(config.enchQuickdrawEnable)
    		enchantQuickdraw = new Enchantment_Quickdraw(config.enchQuickdrawID, 1);
    	
    	if(config.enchVenomEnable)
	    	enchantVenom = new Enchantment_Venom(config.enchVenomID, 2);
    	
    	if(config.enchIceAspectEnable)
    		enchantIceAspect = new Enchantment_IceAspect(config.enchIceAspectID, 2);
	    	
    	if(config.enchDefusingEnable)
	    	enchantDefusing = new Enchantment_Defusing(config.enchDefusingID, 5);
    	
    	if(config.enchDisjunctionEnable)
	    	enchantDisjunction = new Enchantment_Disjunction(config.enchDisjunctionID, 5);
    	
    	if(config.enchDowsingEnable)
	    	enchantDowsing = new Enchantment_Dowsing(config.enchDowsingID, 5);
    	
    	if(config.enchSpellbaneEnable)
    		enchantSpellbane = new Enchantment_Spellbane(config.enchSpellbaneID, 5);
    	
    	if(config.enchMendingEnable)
    		enchantMending = new Enchantment_Mending(config.enchMendingID, 2);
	    	
    	if(config.enchCleaveEnable)
	    	enchantCleave = new Enchantment_Cleave(config.enchCleaveID, 2);
    	
    	if(config.enchVorpalEnable)
    		enchantVorpal = new Enchantment_Vorpal(config.enchVorpalID, 1);
    	
    	if(config.enchLeechEnable)
    		enchantLeech = new Enchantment_Leech(config.enchLeechID, 2);
    	
    	if(config.enchExecuteEnable)
    		enchantExecution = new Enchantment_Execution(config.enchExecuteID, 2);
	    	
	    	
	    if(config.enchPoisonProtectEnable)
	    	enchantPoisonProtect = new Enchantment_PoisonProtect(config.enchPoisonProtectID, 5);
	    	
	    if(config.enchKnowledgeEnable)
	    	enchantKnowledge = new Enchantment_Knowledge(config.enchKnowledgeID, 1);
	    	
	    if(config.enchBerserkEnable)
	    	enchantBerserk = new Enchantment_Berserking(config.enchBerserkID, 1);
	    
	    if(config.enchSteadfastEnable)
	    	enchantSteadfast = new Enchantment_Steadfast(config.enchSteadfastID, 1);
	    	
	    if(config.enchAgilityEnable)
	    	enchantAgility = new Enchantment_Agility(config.enchAgilityID, 1);
	    
	    if(config.enchMobilityEnable)
	    	enchantMobility = new Enchantment_Mobility(config.enchMobilityID, 1);
	    	
	    if(config.enchFleetfootEnable)
	    	enchantFleetfoot = new Enchantment_Fleetfooted(config.enchFleetfootID, 1);
	    
	    if(config.enchResurrectionEnable)
	    	enchantResurrection = new Enchantment_Resurrection(config.enchResurrectionID, 1);
	    
	    if(config.enchHarvestEnable)
	    	enchantHarvest = new Enchantment_Harvest(config.enchHarvestID, 1);
	}
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent fEvent)
    {   	
    	if(config.enchPoisonEnable)
    		MinecraftForge.EVENT_BUS.register(enchantPoison);
    	
    	if(config.enchFrostEnable)
    		MinecraftForge.EVENT_BUS.register(enchantFrost);
    	
    	if(config.enchExplosiveEnable)
    		MinecraftForge.EVENT_BUS.register(enchantExplosive);
    	
    	if(config.enchQuickdrawEnable)
    		MinecraftForge.EVENT_BUS.register(enchantQuickdraw);
    	
    	if(config.enchVenomEnable)
    		MinecraftForge.EVENT_BUS.register(enchantVenom);
    	
    	if(config.enchIceAspectEnable)
    		MinecraftForge.EVENT_BUS.register(enchantIceAspect);
	    	
    	if(config.enchDefusingEnable)
    		MinecraftForge.EVENT_BUS.register(enchantDefusing);
    	
    	if(config.enchDisjunctionEnable)
    		MinecraftForge.EVENT_BUS.register(enchantDisjunction);
    	
    	if(config.enchDowsingEnable)
    		MinecraftForge.EVENT_BUS.register(enchantDowsing);
    	
    	if(config.enchSpellbaneEnable)
    		MinecraftForge.EVENT_BUS.register(enchantSpellbane);
    	
    	if(config.enchMendingEnable)
    		MinecraftForge.EVENT_BUS.register(enchantMending);
	    	
    	if(config.enchCleaveEnable)
    		MinecraftForge.EVENT_BUS.register(enchantCleave);
    	
    	if(config.enchVorpalEnable)
    		MinecraftForge.EVENT_BUS.register(enchantVorpal);
    	
    	if(config.enchLeechEnable)
    		MinecraftForge.EVENT_BUS.register(enchantLeech);
    	
    	if(config.enchExecuteEnable)
    		MinecraftForge.EVENT_BUS.register(enchantExecution);
	    	
	    	
	    if(config.enchPoisonProtectEnable)
	    	MinecraftForge.EVENT_BUS.register(enchantPoisonProtect);
	    	
	    if(config.enchKnowledgeEnable)
	    	MinecraftForge.EVENT_BUS.register(enchantKnowledge);
	    	
	    if(config.enchBerserkEnable)
	    	MinecraftForge.EVENT_BUS.register(enchantBerserk);
	    
	    if(config.enchSteadfastEnable)
	    	MinecraftForge.EVENT_BUS.register(enchantSteadfast);
	    	
	    if(config.enchAgilityEnable)
	    	MinecraftForge.EVENT_BUS.register(enchantAgility);
	    
	    if(config.enchMobilityEnable)
	    	MinecraftForge.EVENT_BUS.register(enchantMobility);
	    	
	    if(config.enchFleetfootEnable)
	    	MinecraftForge.EVENT_BUS.register(enchantFleetfoot);
	    
	    if(config.enchResurrectionEnable)
	    	MinecraftForge.EVENT_BUS.register(enchantResurrection);
	    
	    if(config.enchHarvestEnable)
	    {
	    	MinecraftForge.EVENT_BUS.register(enchantHarvest);
	    	((Enchantment_Harvest)enchantHarvest).Prepare();
	    }
	    
	    if(config.charms)
	    {
	    	MinecraftForge.EVENT_BUS.register(itemCharm);
	    	((Item_Charm)itemCharm).addEnchants();
	    }
    }
}
