package com.demoxin.minecraft.moreenchants;

import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.DataWatcher.WatchableObject;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class Enchantment_Explosive extends Enchantment
{
	public Enchantment_Explosive(int fId, int fWeight)
	{
		super(fId, fWeight, EnumEnchantmentType.bow);
		this.setName("explosive");
		addToBookList(this);
	}
	
	@Override
	public int getMaxLevel()
	{
	    return 1;
	}
	
	@Override
	public int getMinEnchantability(int par1)
	{
	    return 20;
	}
	
	@Override
	public int getMaxEnchantability(int par1)
	{
	    return 50;
	}
	
	@Override
	public boolean canApplyTogether(Enchantment fTest)
	{
		if(fTest == Enchantment.infinity || fTest == MoreEnchants.enchantQuickdraw || fTest == MoreEnchants.enchantExplosive)
			return false;
		if(fTest == MoreEnchants.enchantPoison || fTest == Enchantment.flame || fTest == MoreEnchants.enchantFrost)
			return false;
		return true;
	}
	
	public boolean canApply(ItemStack fTest)
	{
		if(fTest.getItem() instanceof ItemBow || fTest.getItem() instanceof ItemBook)
			return true;
	    return false;
	}
	
	@SubscribeEvent
	public void HandleArrowSpawn(EntityJoinWorldEvent fEvent)
	{
		if(!(fEvent.entity instanceof EntityArrow))
			return;
		
		if(!(((EntityArrow)fEvent.entity).shootingEntity instanceof EntityLivingBase))
			return;
		
		EntityLivingBase shooter = (EntityLivingBase)((EntityArrow)fEvent.entity).shootingEntity;
		ItemStack itemBow = shooter.getHeldItem();
		
		if(itemBow == null)
			return;
		
		if(EnchantmentHelper.getEnchantmentLevel(effectId, itemBow) <= 0)
			return;
		
		boolean bitsafe = false;
		@SuppressWarnings("unchecked")
		List<WatchableObject> watched = fEvent.entity.getDataWatcher().getAllWatched();
		for(WatchableObject obj : watched)
		{
			if(obj.getDataValueId() == 24)
			{
				bitsafe = true;
				break;
			}
		}
		if(!bitsafe)
			fEvent.entity.getDataWatcher().addObject(24, Integer.valueOf(0));
		
		fEvent.entity.getDataWatcher().updateObject(24, fEvent.entity.getDataWatcher().getWatchableObjectInt(24) + Integer.valueOf(MoreEnchants.bitEXPLODE));
	}
	
	@SubscribeEvent
	public void HandleEnchant(PlaySoundAtEntityEvent fEvent)
	{
		if(!(fEvent.entity instanceof EntityArrow))
			return;
		
		if(fEvent.name != "random.bowhit")
			return;
		
		EntityArrow strikingArrow = ((EntityArrow)fEvent.entity);
		
		boolean bitsafe = false;
		@SuppressWarnings("unchecked")
		List<WatchableObject> watched = strikingArrow.getDataWatcher().getAllWatched();
		for(WatchableObject obj : watched)
		{
			if(obj.getDataValueId() == 24)
			{
				bitsafe = true;
				break;
			}
		}
		if(!bitsafe)
			return;
		
		int infoBits = strikingArrow.getDataWatcher().getWatchableObjectInt(24);
	
		if((infoBits & MoreEnchants.bitEXPLODE) != 0)
		{
			if(!strikingArrow.isInWater())
				strikingArrow.worldObj.newExplosion(strikingArrow, strikingArrow.posX, strikingArrow.posY, strikingArrow.posZ, 2.0F, false, false);
			strikingArrow.setDead();
		}
	}
}
