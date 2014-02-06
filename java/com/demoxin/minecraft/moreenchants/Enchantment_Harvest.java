package com.demoxin.minecraft.moreenchants;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDigging;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentUntouching;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.oredict.OreDictionary;

public class Enchantment_Harvest extends Enchantment
{
	protected Enchantment_Harvest(int par1, int par2)
    {
        super(par1, par2, EnumEnchantmentType.digger);
        this.setName("harvest");
        addToBookList(this);
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
    
    @ForgeSubscribe
    public void HandlePick(HarvestDropsEvent fEvent)
    {
    	if(fEvent.harvester == null)
    		return;
    	
    	if(fEvent.block == Block.stone || fEvent.block == Block.cobblestone || fEvent.block == Block.netherrack || fEvent.block == Block.whiteStone)
    		return;
    	
    	ItemStack harvestingItem = fEvent.harvester.getCurrentEquippedItem();
    	
    	if(EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantHarvest.effectId, harvestingItem) <= 0)
    		return;
    	
    	int searchX = fEvent.x-3;
    	int searchY = fEvent.y-3;
    	int searchZ = fEvent.z-3;
    	
    	ArrayList<Coords> breakBlocks = new ArrayList<Coords>();
    	
    	ArrayList<ItemStack> testForOre = fEvent.block.getBlockDropped(fEvent.world, searchX, searchY, searchZ, fEvent.blockMetadata, fEvent.fortuneLevel);
		ForgeEventFactory.fireBlockHarvesting(testForOre, fEvent.world, fEvent.block, searchX, searchY, searchZ, fEvent.blockMetadata, fEvent.fortuneLevel, fEvent.dropChance, false, null);
		boolean testOre = false;
		for(ItemStack stack : testForOre)
		{
			if(OreDictionary.getOreName(OreDictionary.getOreID(stack)).contains("ore") || OreDictionary.getOreName(OreDictionary.getOreID(stack)).contains("gem"))
			{
				testOre = true;
				break;
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
    				int blockID = fEvent.world.getBlockId(searchX, searchY, searchZ);
    				int blockMeta = fEvent.world.getBlockMetadata(searchX, searchY, searchZ);
    				
    				if(blockID == 0)
    					continue;
    				if(blockMeta < 0 || blockMeta > 15)
    					blockMeta = 0;
    				
    				ArrayList<ItemStack> itemsDropped = Block.blocksList[blockID].getBlockDropped(fEvent.world, searchX, searchY, searchZ, blockMeta, fEvent.fortuneLevel);
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
			ArrayList<ItemStack> itemsToDrop = fEvent.block.getBlockDropped(fEvent.world, targetBlock.x, targetBlock.y, targetBlock.z, fEvent.blockMetadata, fEvent.fortuneLevel);
    		avgDrop += ForgeEventFactory.fireBlockHarvesting(itemsToDrop, fEvent.world, fEvent.block, targetBlock.x, targetBlock.y, targetBlock.z, fEvent.blockMetadata, fEvent.fortuneLevel, fEvent.dropChance, fEvent.isSilkTouching, null);
    		for(ItemStack stack : itemsToDrop)
    			fEvent.drops.add(stack);
    		fEvent.world.setBlockToAir(targetBlock.x, targetBlock.y, targetBlock.z);
    	}
    	fEvent.dropChance = (avgDrop/(breakBlocks.size()+1));
    	
    }
    
    @ForgeSubscribe
    public void HandleAxe(HarvestDropsEvent fEvent)
    {
    	if(fEvent.harvester == null)
    		return;
    	
    	ItemStack harvestingItem = fEvent.harvester.getCurrentEquippedItem();
    	
    	if(EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantHarvest.effectId, harvestingItem) <= 0)
    		return;
    	
    	Block blockObj = fEvent.block;
    	int blockID = fEvent.block.blockID;
    	
    	if(blockObj == null)
    		return;
    	
    	if(!(blockObj.isWood(fEvent.world, fEvent.x, fEvent.y, fEvent.z)))
    		return;
    	
    	boolean foundTop = false;
    	int searchHeight = fEvent.y;
    	
    	for(int i = 1; i <= 50; i++)
    	{
    		searchHeight = fEvent.y+i;
    		int searchID = fEvent.world.getBlockId(fEvent.x, searchHeight, fEvent.z);
    		if(searchID == blockID)
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
    				Block searchLeaves = Block.blocksList[fEvent.world.getBlockId(searchX, searchY, searchZ)];
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
    	fEvent.world.setBlock(fEvent.x, fEvent.y, fEvent.z, blockID, fEvent.blockMetadata, 2);
    	drops = BreakTree(fEvent.world, fEvent.x, fEvent.y, fEvent.z, fEvent.fortuneLevel, fEvent.dropChance);
    	fEvent.drops.clear();
    	fEvent.drops.addAll(drops);
    }
    
    protected ArrayList<ItemStack> BreakTree(World fWorld, int fX, int fY, int fZ, int fFortune, float fChance)
    {
    	ArrayList<Coords> listCoords = new ArrayList<Coords>();
    	ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
    	listCoords.add(new Coords(fX-1, fY, fZ));
    	listCoords.add(new Coords(fX+1, fY, fZ));
    	listCoords.add(new Coords(fX, fY+1, fZ));
    	listCoords.add(new Coords(fX, fY, fZ-1));
    	listCoords.add(new Coords(fX, fY, fZ+1));
    	
    	int centerBlockID = fWorld.getBlockId(fX, fY, fZ);
    	Block centerBlockObj = Block.blocksList[centerBlockID];
    	int centerBlockMeta = fWorld.getBlockMetadata(fX, fY, fZ);
    	int centerHarvestLevel = MinecraftForge.getBlockHarvestLevel(centerBlockObj, centerBlockMeta, "axe");
    	
    	ArrayList<ItemStack> itemsToDrop = centerBlockObj.getBlockDropped(fWorld, fX, fY, fZ, centerBlockMeta, fFortune);
    	ForgeEventFactory.fireBlockHarvesting(itemsToDrop, fWorld, centerBlockObj, fX, fY, fZ, centerBlockMeta, fFortune, fChance, false, null);
		for(ItemStack stack : itemsToDrop)
			drops.add(stack);
		fWorld.setBlockToAir(fX, fY, fZ);
    	
    	for(Coords checkCoord : listCoords)
    	{
    		int blockID = fWorld.getBlockId(checkCoord.x, checkCoord.y, checkCoord.z);
    		Block blockObj = Block.blocksList[blockID];
    		if(blockObj == null)
    			continue;
    		int blockMeta = fWorld.getBlockMetadata(checkCoord.x, checkCoord.y, checkCoord.z);
    		
    		if(MinecraftForge.getBlockHarvestLevel(blockObj, blockMeta, "axe") > centerHarvestLevel)
    			continue;
    		
    		if(blockObj.isWood(fWorld, checkCoord.x, checkCoord.y, checkCoord.z) || blockObj.isLeaves(fWorld, checkCoord.x, checkCoord.y, checkCoord.z))
    			drops.addAll(BreakTree(fWorld, checkCoord.x, checkCoord.y, checkCoord.z, fFortune, fChance));
    	}
    	
    	Block blockBelow = Block.blocksList[fWorld.getBlockId(fX, fY-1, fZ)];
    	if(blockBelow != null && blockBelow.isLeaves(fWorld, fX, fY-1, fZ))
    		drops.addAll(BreakTree(fWorld, fX, fY-1, fZ, fFortune, fChance));
    	
    	return drops;
    }
    
    @ForgeSubscribe
    public void HandleHoe(HarvestDropsEvent fEvent)
    {
    	if(fEvent.harvester == null)
    		return;
    	
    	ItemStack harvestingItem = fEvent.harvester.getCurrentEquippedItem();
    	if(harvestingItem == null)
    		return;
    	
    	if(EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantHarvest.effectId, harvestingItem) <= 0)
    		return;
    	
    	if(fEvent.block.blockMaterial != Material.plants)
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
    			int blockID = fEvent.world.getBlockId(searchX, searchY, searchZ);
    			int blockMeta = fEvent.world.getBlockMetadata(searchX, searchY, searchZ);
    				
    			if(blockID == 0)
    				continue;
    			if(blockMeta < 0 || blockMeta > 15)
    				blockMeta = 0;
    			
    			Block breakingBlock = Block.blocksList[blockID];
    			
    			if(breakingBlock.blockMaterial != Material.plants)
    				continue;
    				
    			ArrayList<ItemStack> itemsDropped = breakingBlock.getBlockDropped(fEvent.world, searchX, searchY, searchZ, blockMeta, fEvent.fortuneLevel);
    			ForgeEventFactory.fireBlockHarvesting(itemsDropped, fEvent.world, fEvent.block, searchX, searchY, searchZ, fEvent.blockMetadata, fEvent.fortuneLevel, fEvent.dropChance, fEvent.isSilkTouching, null);
    			
    			if(itemsDropped.isEmpty())
    				continue;
    				
    			ArrayList<ItemStack> itemsToDrop = fEvent.block.getBlockDropped(fEvent.world, searchX, searchY, searchZ, blockMeta, fEvent.fortuneLevel);
        		avgDrop += ForgeEventFactory.fireBlockHarvesting(itemsToDrop, fEvent.world, fEvent.block, searchX, searchY, searchZ, blockMeta, fEvent.fortuneLevel, fEvent.dropChance, fEvent.isSilkTouching, null);
        		for(ItemStack stack : itemsToDrop)
        			fEvent.drops.add(stack);
        		fEvent.world.setBlockToAir(searchX, searchY, searchZ);
    		}
    	}
    	fEvent.dropChance = (avgDrop/brokenBlocks+1);    	
    }
    
    @ForgeSubscribe
    public void HandleHoe(UseHoeEvent fEvent)
    {    	
    	if (!fEvent.entityPlayer.canPlayerEdit(fEvent.x, fEvent.y, fEvent.z, fEvent.world.getBlockMetadata(fEvent.x, fEvent.y, fEvent.z), fEvent.current))
            return;

    	if(fEvent.current == null)
    		return;
    	
    	if(EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantHarvest.effectId, fEvent.current) <= 0)
    		return;
    	
    	Block blockTarget = Block.blocksList[fEvent.world.getBlockId(fEvent.x, fEvent.y, fEvent.z)];
    	
    	if(blockTarget == null)
    		return;
    	
    	if(blockTarget.blockMaterial != Material.pumpkin)
    		return;
    		
    	int blockID = fEvent.world.getBlockId(fEvent.x, fEvent.y, fEvent.z);
    	int blockMeta = fEvent.world.getBlockMetadata(fEvent.x, fEvent.y, fEvent.z);
    			
    	if(blockID == 0)
    		return;
    	if(blockMeta < 0 || blockMeta > 15)
    		blockMeta = 0;
    	
    	Block.blocksList[blockID].dropBlockAsItem(fEvent.world, fEvent.x, fEvent.y, fEvent.z, blockMeta, 0);
    	fEvent.world.setBlockToAir(fEvent.x, fEvent.y, fEvent.z);
    }
}
