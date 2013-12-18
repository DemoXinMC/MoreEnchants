package com.demoxin.minecraft.moreenchants;

import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentArrowFire;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.WatchableObject;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class Enchantment_Poison extends Enchantment {
	public Enchantment_Poison(int fId, int fWeight)
	{
		super(fId, fWeight, EnumEnchantmentType.bow);
		this.setName("poison");
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
    	if(fTest instanceof Enchantment_Poison || fTest instanceof EnchantmentArrowFire)
    		return false;
    	return true;
    }
    
    public boolean canApply(ItemStack fTest)
    {
    	if(fTest.getItem() instanceof ItemBow || fTest.getItem() instanceof ItemBook)
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
		
		if(EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantPoison.effectId, itemBow) <= 0)
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
		
		fEvent.entity.getDataWatcher().updateObject(24, fEvent.entity.getDataWatcher().getWatchableObjectInt(24) + Integer.valueOf(MoreEnchants.bitPOISON));
    }
    
    @ForgeSubscribe
    public void HandleEnchant(LivingHurtEvent fEvent)
    {
    	if(fEvent.source.damageType != "arrow")
			return;
    	
    	if(!(fEvent.entity instanceof EntityLivingBase))
    		return;
    	
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

		if((infoBits & MoreEnchants.bitPOISON) != 0)
		{
			PotionEffect poisonResult;
			poisonResult = new PotionEffect(Potion.poison.getId(), 120, 1);		
			((EntityLivingBase)fEvent.entity).addPotionEffect(poisonResult);	
		}
    }

}
