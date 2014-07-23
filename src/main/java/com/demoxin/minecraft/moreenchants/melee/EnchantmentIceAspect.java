package com.demoxin.minecraft.moreenchants.melee;

import com.demoxin.minecraft.moreenchants.MoreEnchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentIceAspect extends Enchantment {
	public EnchantmentIceAspect(int fId, int fWeight)
	{
		super(fId, fWeight, EnumEnchantmentType.weapon);
		this.setName("iceaspect");
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
		return 10 + 20 * (par1 - 1);
    }

    @Override
    public int getMaxEnchantability(int par1)
    {
        return super.getMinEnchantability(par1) + 50;
    }
    
    @Override
    public boolean canApplyTogether(Enchantment fTest)
    {
    	if(fTest == Enchantment.fireAspect || fTest == MoreEnchants.enchantIceAspect || fTest == MoreEnchants.enchantVenom || fTest == MoreEnchants.enchantMending)
    		return false;
    	return true;
    }
    
    @Override
    public boolean canApply(ItemStack fTest)
    {
    	return Enchantment.sharpness.canApply(fTest);
    }
    
    @SubscribeEvent
    public void HandleEnchant(LivingHurtEvent fEvent)
    {
    	if(fEvent.source.damageType != "player" && fEvent.source.damageType != "mob")
			return;
    	
    	if(!(fEvent.source.getSourceOfDamage() instanceof EntityLivingBase))
    		return;
    	
    	EntityLivingBase attacker = (EntityLivingBase)fEvent.source.getSourceOfDamage();
    	if(attacker == null)
    		return;
    	
    	ItemStack dmgSource = ((EntityLivingBase)fEvent.source.getSourceOfDamage()).getHeldItem();
		if(dmgSource == null)
			return;
		
		if(EnchantmentHelper.getEnchantmentLevel(effectId, dmgSource) <= 0)
			return;
		
		int level = EnchantmentHelper.getEnchantmentLevel(effectId, dmgSource);
		
		((EntityLivingBase)fEvent.entity).addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), 60, level));
		if(level == 2)
			((EntityLivingBase)fEvent.entity).addPotionEffect(new PotionEffect(Potion.digSlowdown.getId(), 60, level));
			
    }
}

