package com.demoxin.minecraft.moreenchants;

import java.util.List;
import java.util.Random;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentArrowFire;
import net.minecraft.enchantment.EnchantmentArrowKnockback;
import net.minecraft.enchantment.EnchantmentFireAspect;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentKnockback;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.WatchableObject;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class Enchantment_Mending extends Enchantment {
	public Enchantment_Mending(int fId, int fWeight)
	{
		super(fId, fWeight, EnumEnchantmentType.weapon);
		this.setName("mending");
		addToBookList(this);
	}
	
	@Override
	public int getMaxLevel()
	{
		return 2;
	}
	
	@Override
    public int getMinEnchantability(int par1)
    {
        return 5 + 20 * (par1 - 1);
    }

    @Override
    public int getMaxEnchantability(int par1)
    {
        return super.getMinEnchantability(par1) + 50;
    }
    
    public boolean canApplyTogether(Enchantment fTest)
    {
    	if(fTest instanceof Enchantment_Mending)
    		return false;
    	if(fTest instanceof EnchantmentKnockback || fTest instanceof EnchantmentArrowKnockback || fTest instanceof EnchantmentArrowFire || fTest instanceof EnchantmentFireAspect)
        	return false;
    	return true;
    }
    
    public boolean canApply(ItemStack fTest)
    {
    	if(fTest.getItem() instanceof ItemSword || fTest.getItem() instanceof ItemBow || fTest.getItem() instanceof ItemAxe || fTest.getItem() instanceof ItemBook)
    		return true;
        return false;
    }
    
    @ForgeSubscribe
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
		
		if(EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantMending.effectId, itemBow) <= 0)
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
		
		if(EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantMending.effectId, itemBow) == 1)
			fEvent.entity.getDataWatcher().updateObject(24, fEvent.entity.getDataWatcher().getWatchableObjectInt(24) + Integer.valueOf(MoreEnchants.bitMENDING));
		else
			fEvent.entity.getDataWatcher().updateObject(24, fEvent.entity.getDataWatcher().getWatchableObjectInt(24) + Integer.valueOf(MoreEnchants.bitMENDING2));
    }
    
    @ForgeSubscribe
    public void HandleEnchant(LivingAttackEvent fEvent)
    {
    	if(fEvent.source.damageType != "player" && fEvent.source.damageType != "mob" && fEvent.source.damageType != "arrow")
			return;
    	
    	if(!(fEvent.entity instanceof EntityLivingBase))
    		return;
    	
    	if(fEvent.source.damageType == "arrow")
    	{
    		if(!(fEvent.source.getSourceOfDamage() instanceof EntityArrow))
        		return;
    		
    		EntityArrow strikingArrow = ((EntityArrow)fEvent.source.getSourceOfDamage());
        	
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
			if((infoBits & MoreEnchants.bitMENDING) != 0 || (infoBits & MoreEnchants.bitMENDING2) != 0)
			{
				fEvent.setCanceled(true);
				
				// Next, figure out our damage modifier.
				float dmgMod = 0.3F;
				if((infoBits & MoreEnchants.bitMENDING2) != 0)
					dmgMod = dmgMod + 0.2F;
				
				((EntityLivingBase)fEvent.entity).heal(fEvent.ammount * dmgMod);
				
				if((infoBits & MoreEnchants.bitPOISON) != 0)
				{
					PotionEffect poisonResult;
					poisonResult = new PotionEffect(Potion.regeneration.getId(), 100, 1);
					((EntityLivingBase)fEvent.entity).addPotionEffect(poisonResult);
				}
			}
    	}
    	
    	if(fEvent.source.damageType == "player" || fEvent.source.damageType == "mob")
    	{
    		if(!(fEvent.source.getSourceOfDamage() instanceof EntityLivingBase))
    			return;
    		
    		ItemStack dmgSource = ((EntityLivingBase)fEvent.source.getSourceOfDamage()).getHeldItem();
			if(dmgSource == null)
				return;
			
			int levelMending = EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantMending.effectId, dmgSource);
			if(levelMending > 0)
			{
				fEvent.setCanceled(true);
				float healMod = 0.2F + (0.1F * levelMending);
				((EntityLivingBase)fEvent.entity).heal(fEvent.ammount * healMod);
			}
    	}
    	
    	if(fEvent.isCanceled())
    	{
    		double X = fEvent.entity.posX;
    		double Y = fEvent.entity.posY;
    		double Z = fEvent.entity.posZ;
    		Random random = fEvent.entity.worldObj.rand;
    		for (int i = 0; i < 5; ++i)
            {
                double d0 = random.nextGaussian() * 0.02D;
                double d1 = random.nextGaussian() * 0.02D;
                double d2 = random.nextGaussian() * 0.02D;
                fEvent.entity.worldObj.spawnParticle("happyVillager", (double)((float)X + random.nextFloat()), (double)Y + ((double)random.nextFloat() * 2), (double)((float)Z + random.nextFloat()), d0, d1, d2);
            }	
    	}
    }
}
