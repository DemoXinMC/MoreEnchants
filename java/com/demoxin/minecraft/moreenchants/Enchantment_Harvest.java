package com.demoxin.minecraft.moreenchants;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDigging;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentUntouching;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class Enchantment_Harvest extends Enchantment
{
	protected List<ItemStack> harvestableOres;
	
	protected Enchantment_Harvest(int par1, int par2)
    {
        super(par1, par2, EnumEnchantmentType.digger);
        this.setName("harvest");
        addToBookList(this);
    }
	
	public void Prepare()
	{
        harvestableOres = new ArrayList<ItemStack>();
        
        String[] oreDictNames = OreDictionary.getOreNames();
        
        for(String orename : oreDictNames)
        {
        	if(orename.contains("ore") || orename.contains("gem") || orename.contains("dust") || orename.contains("crystal"))
        	{
        		ArrayList<ItemStack> ores = OreDictionary.getOres(orename);
        		for(ItemStack ore : ores)
        		{
        			if(!harvestableOres.contains(ore))
        				harvestableOres.add(ore);
        		}
        	}
        }
        
        harvestableOres.add(new ItemStack(Items.coal));
        harvestableOres.add(new ItemStack(Items.quartz));
        harvestableOres.add(new ItemStack(Items.glowstone_dust));
        harvestableOres.add(new ItemStack(Items.dye, 1, 4));
	}
	
	protected class Coords
	{
		public int x,y,z;
		Coords(int fX, int fY, int fZ)
		{
			x = fX;
			y = fY;
			z = fZ;
		}
	}

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinEnchantability(int par1)
    {
        return 15;
    }

    /**
     * Returns the maximum value of enchantability nedded on the enchantment level passed.
     */
    public int getMaxEnchantability(int par1)
    {
        return super.getMinEnchantability(par1) + 50;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel()
    {
        return 1;
    }

    /**
     * Determines if the enchantment passed can be applyied together with this enchantment.
     */
    public boolean canApplyTogether(Enchantment fTest)
    {
    	if(fTest instanceof EnchantmentDigging || fTest instanceof EnchantmentUntouching)
    		return false;
    	return true;
    }
    
    public boolean canApply(ItemStack fTest)
    {
    	if(fTest.getItem() instanceof ItemPickaxe || fTest.getItem() instanceof ItemAxe || fTest.getItem() instanceof ItemHoe ||fTest.getItem() instanceof ItemBook)
    		return true;
        return false;
    }
    
    @SubscribeEvent
    public void HandlePick(HarvestDropsEvent fEvent)
    {
    	if(fEvent.harvester == null)
    		return;
    	
    	if(fEvent.block == Blocks.stone || fEvent.block == Blocks.cobblestone || fEvent.block == Blocks.netherrack || fEvent.block == Blocks.end_stone)
    		return;
    	
    	ItemStack harvestingItem = fEvent.harvester.getCurrentEquippedItem();
    	
    	if(harvestingItem == null)
    		return;
    	
    	if(EnchantmentHelper.getEnchantmentLevel(effectId, harvestingItem) <= 0)
    		return;
    	
    	int searchX = fEvent.x-3;
    	int searchY = fEvent.y-3;
    	int searchZ = fEvent.z-3;
    	
    	ArrayList<Coords> breakBlocks = new ArrayList<Coords>();
    	
    	ArrayList<ItemStack> testForOre = fEvent.block.getDrops(fEvent.world, searchX, searchY, searchZ, fEvent.blockMetadata, fEvent.fortuneLevel);
		ForgeEventFactory.fireBlockHarvesting(testForOre, fEvent.world, fEvent.block, searchX, searchY, searchZ, fEvent.blockMetadata, fEvent.fortuneLevel, fEvent.dropChance, false, null);
		boolean testOre = false;
		for(ItemStack stack : testForOre)
		{
			for(ItemStack harvestable : harvestableOres)
			{
				if(harvestable.isItemEqual(stack))
				{
					testOre = true;
					break;
				}
			}
			
		}
		if(testOre == false)
			return;
    	
    	for(int i = 0; i < 7; i++, searchX++)
    	{
    		searchY = fEvent.y-3;
    		for(int j = 0; j < 7; j++, searchY++)
    		{
    			searchZ = fEvent.z-3;
    			for(int k = 0; k < 7; k++, searchZ++)
    			{
    				Block block = fEvent.world.getBlock(searchX, searchY, searchZ);
    				int blockMeta = fEvent.world.getBlockMetadata(searchX, searchY, searchZ);
    				
    				if(block == null)
    					continue;
    				if(blockMeta < 0 || blockMeta > 15)
    					blockMeta = 0;
    				
    				ArrayList<ItemStack> itemsDropped = block.getDrops(fEvent.world, searchX, searchY, searchZ, blockMeta, fEvent.fortuneLevel);
    				ForgeEventFactory.fireBlockHarvesting(itemsDropped, fEvent.world, fEvent.block, searchX, searchY, searchZ, fEvent.blockMetadata, fEvent.fortuneLevel, fEvent.dropChance, fEvent.isSilkTouching, null);
    				
    				if(itemsDropped.isEmpty())
    					continue;
    				
    				breakLoop:
    				for(ItemStack item : itemsDropped)
    				{
    					for(ItemStack testItem : fEvent.drops)
    					{
    						if(OreDictionary.itemMatches(testItem, item, true))
        					{
        						breakBlocks.add(new Coords(searchX, searchY, searchZ));
        						break breakLoop;
        					}
    					}
    				}
    			}
    		}
    	}
    	
    	if(breakBlocks.isEmpty())
    		return;
    	
    	float avgDrop = fEvent.dropChance;
    	for(Coords targetBlock : breakBlocks)
    	{
			ArrayList<ItemStack> itemsToDrop = fEvent.block.getDrops(fEvent.world, targetBlock.x, targetBlock.y, targetBlock.z, fEvent.blockMetadata, fEvent.fortuneLevel);
    		avgDrop += ForgeEventFactory.fireBlockHarvesting(itemsToDrop, fEvent.world, fEvent.block, targetBlock.x, targetBlock.y, targetBlock.z, fEvent.blockMetadata, fEvent.fortuneLevel, fEvent.dropChance, fEvent.isSilkTouching, null);
    		for(ItemStack stack : itemsToDrop)
    			fEvent.drops.add(stack);
    		fEvent.world.setBlockToAir(targetBlock.x, targetBlock.y, targetBlock.z);
    	}
    	fEvent.dropChance = (avgDrop/(breakBlocks.size()+1));
    	
    	harvestingItem.damageItem(breakBlocks.size(), fEvent.harvester);
    	
    }
    
    @SubscribeEvent
    public void HandleAxe(HarvestDropsEvent fEvent)
    {
    	if(fEvent.harvester == null)
    		return;
    	
    	ItemStack harvestingItem = fEvent.harvester.getCurrentEquippedItem();
    	if(harvestingItem == null || !(harvestingItem.getItem() instanceof ItemAxe))
    		return;
    		
    	if(EnchantmentHelper.getEnchantmentLevel(effectId, harvestingItem) <= 0)
    		return;
    	
    	Block blockObj = fEvent.block;
    	
    	if(blockObj == null)
    		return;
    	
    	if(!(blockObj.isWood(fEvent.world, fEvent.x, fEvent.y, fEvent.z)))
    		return;
    	
    	boolean foundTop = false;
    	int searchHeight = fEvent.y;
    	
    	for(int i = 1; i <= 50; i++)
    	{
    		searchHeight = fEvent.y+i;
    		Block search = fEvent.world.getBlock(fEvent.x, searchHeight, fEvent.z);
    		if(search == blockObj)
    			continue;
    		
    		foundTop = true;
    		break;
    	}
    	
    	if(!foundTop)
    		return;
    	
    	int numLeaves = 0;
    	
    	for(int searchX = fEvent.x-1; searchX <= fEvent.x+1; searchX++)
    	{
    		for(int searchY = searchHeight-1; searchY <= searchHeight+2; searchY++)
    		{
    			for(int searchZ = fEvent.z-1; searchZ <= fEvent.z+1; searchZ++)
    			{
    				Block searchLeaves = fEvent.world.getBlock(searchX, searchY, searchZ);
    				if(searchLeaves == null)
    					continue;
    				if(searchLeaves.isLeaves(fEvent.world, searchX, searchY, searchZ))
    					numLeaves++;
    			}
    		}
    	}
    	
    	if(numLeaves < 3)
    		return;
    	
    	ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
    	fEvent.world.setBlock(fEvent.x, fEvent.y, fEvent.z, fEvent.block, fEvent.blockMetadata, 2);
    	drops = BreakTree(fEvent.world, fEvent.x, fEvent.y, fEvent.z, fEvent.fortuneLevel, fEvent.dropChance, fEvent.x, fEvent.z);
    	fEvent.drops.clear();
    	fEvent.drops.addAll(drops);
    }
    
    protected ArrayList<ItemStack> BreakTree(World fWorld, int fX, int fY, int fZ, int fFortune, float fChance, int fOriginX, int fOriginZ)
    {
    	ArrayList<Coords> listCoords = new ArrayList<Coords>();
    	ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
    	
    	listCoords.add(new Coords(fX-1, fY, fZ));
    	listCoords.add(new Coords(fX+1, fY, fZ));
    	listCoords.add(new Coords(fX, fY, fZ-1));
    	listCoords.add(new Coords(fX, fY, fZ+1));
    	
    	listCoords.add(new Coords(fX, fY+1, fZ));
    	
    	listCoords.add(new Coords(fX-1, fY, fZ-1));
    	listCoords.add(new Coords(fX+1, fY, fZ-1));
    	listCoords.add(new Coords(fX-1, fY, fZ+1));
    	listCoords.add(new Coords(fX+1, fY, fZ+1));
    	
    	
    	Block centerBlockObj = fWorld.getBlock(fX, fY, fZ);
    	int centerBlockMeta = fWorld.getBlockMetadata(fX, fY, fZ);
    	if(!centerBlockObj.isToolEffective("axe", centerBlockMeta))
    		return drops;
    	int centerHarvestLevel = centerBlockObj.getHarvestLevel(centerBlockMeta);
    	
    	ArrayList<ItemStack> itemsToDrop = centerBlockObj.getDrops(fWorld, fX, fY, fZ, centerBlockMeta, fFortune);
    	ForgeEventFactory.fireBlockHarvesting(itemsToDrop, fWorld, centerBlockObj, fX, fY, fZ, centerBlockMeta, fFortune, fChance, false, null);
		for(ItemStack stack : itemsToDrop)
			drops.add(stack);
		fWorld.setBlockToAir(fX, fY, fZ);
    	
    	for(Coords checkCoord : listCoords)
    	{
    		Block blockObj = fWorld.getBlock(checkCoord.x, checkCoord.y, checkCoord.z);
    		if(blockObj == null)
    			continue;
    		int blockMeta = fWorld.getBlockMetadata(checkCoord.x, checkCoord.y, checkCoord.z);
    		
    		if(!blockObj.isToolEffective("axe", blockMeta))
    			continue;
    		
    		if(blockObj.getHarvestLevel(blockMeta) > centerHarvestLevel)
    			continue;
    		
    		if(blockObj.isWood(fWorld, checkCoord.x, checkCoord.y, checkCoord.z) || blockObj.isLeaves(fWorld, checkCoord.x, checkCoord.y, checkCoord.z))
    		{
    			if(checkCoord.x < fOriginX+4 && checkCoord.x > fOriginX-4 && checkCoord.z < fOriginZ+4 && checkCoord.z > fOriginZ-4)
    				drops.addAll(BreakTree(fWorld, checkCoord.x, checkCoord.y, checkCoord.z, fFortune, fChance, fOriginX, fOriginZ));
    		}	
    	}
    	
    	Block blockBelow = fWorld.getBlock(fX, fY-1, fZ);
    	if(blockBelow != null && blockBelow.isLeaves(fWorld, fX, fY-1, fZ))
    		drops.addAll(BreakTree(fWorld, fX, fY-1, fZ, fFortune, fChance, fOriginX, fOriginZ));
    	
    	return drops;
    }
    
    @SubscribeEvent
    public void HandleHoe(HarvestDropsEvent fEvent)
    {
    	if(fEvent.harvester == null)
    		return;
    	
    	ItemStack harvestingItem = fEvent.harvester.getCurrentEquippedItem();
    	if(harvestingItem == null)
    		return;
    	
    	if(EnchantmentHelper.getEnchantmentLevel(effectId, harvestingItem) <= 0)
    		return;
    	
    	if(fEvent.block.getMaterial() != Material.plants)
    		return;
    	
    	int searchX = fEvent.x-2;
    	int searchY = fEvent.y;
    	int searchZ = fEvent.z-2;
    	
    	float avgDrop = fEvent.dropChance;
    	int brokenBlocks = 0;
    	
    	for(int i = 0; i < 5; i++, searchX++)
    	{
    		searchZ = fEvent.z-3;
    		for(int j = 0; j < 5; j++, searchZ++)
    		{    			
    			Block block = fEvent.world.getBlock(searchX, searchY, searchZ);
    			int blockMeta = fEvent.world.getBlockMetadata(searchX, searchY, searchZ);
    				
    			if(block == null)
    				continue;
    			if(blockMeta < 0 || blockMeta > 15)
    				blockMeta = 0;
    			
    			if(block.getMaterial() != Material.plants)
    				continue;
    				
    			ArrayList<ItemStack> itemsDropped = block.getDrops(fEvent.world, searchX, searchY, searchZ, blockMeta, fEvent.fortuneLevel);
    			ForgeEventFactory.fireBlockHarvesting(itemsDropped, fEvent.world, fEvent.block, searchX, searchY, searchZ, fEvent.blockMetadata, fEvent.fortuneLevel, fEvent.dropChance, fEvent.isSilkTouching, null);
    			
    			if(itemsDropped.isEmpty())
    				continue;
    				
    			ArrayList<ItemStack> itemsToDrop = fEvent.block.getDrops(fEvent.world, searchX, searchY, searchZ, blockMeta, fEvent.fortuneLevel);
        		avgDrop += ForgeEventFactory.fireBlockHarvesting(itemsToDrop, fEvent.world, fEvent.block, searchX, searchY, searchZ, blockMeta, fEvent.fortuneLevel, fEvent.dropChance, fEvent.isSilkTouching, null);
        		for(ItemStack stack : itemsToDrop)
        			fEvent.drops.add(stack);
        		fEvent.world.setBlockToAir(searchX, searchY, searchZ);
    		}
    	}
    	fEvent.dropChance = (avgDrop/brokenBlocks+1);    	
    }
    
    @SubscribeEvent
    public void HandleHoe(UseHoeEvent fEvent)
    {    	
    	if (!fEvent.entityPlayer.canPlayerEdit(fEvent.x, fEvent.y, fEvent.z, fEvent.world.getBlockMetadata(fEvent.x, fEvent.y, fEvent.z), fEvent.current))
            return;

    	if(fEvent.current == null)
    		return;
    	
    	if(EnchantmentHelper.getEnchantmentLevel(effectId, fEvent.current) <= 0)
    		return;
    	
    	Block blockTarget = fEvent.world.getBlock(fEvent.x, fEvent.y, fEvent.z);
    	
    	if(blockTarget == null)
    		return;
    	
    	if(blockTarget.getMaterial() != Material.gourd)
    		return;
    		
    	Block block = fEvent.world.getBlock(fEvent.x, fEvent.y, fEvent.z);
    	int blockMeta = fEvent.world.getBlockMetadata(fEvent.x, fEvent.y, fEvent.z);
    			
    	if(block == null)
    		return;
    	if(blockMeta < 0 || blockMeta > 15)
    		blockMeta = 0;
    	
    	block.dropBlockAsItem(fEvent.world, fEvent.x, fEvent.y, fEvent.z, blockMeta, 0);
    	fEvent.world.setBlockToAir(fEvent.x, fEvent.y, fEvent.z);
    }
    
    /*
    @SubscribeEvent
    public void HandleHoe(AnvilUpdateEvent fEvent)
    {
    	
    }
    */
}
