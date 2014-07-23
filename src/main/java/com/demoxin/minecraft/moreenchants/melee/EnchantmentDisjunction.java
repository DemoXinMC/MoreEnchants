package com.demoxin.minecraft.moreenchants.melee;

import com.demoxin.minecraft.moreenchants.MoreEnchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentDisjunction extends Enchantment {
	public EnchantmentDisjunction(int fId, int fWeight)
	{
		super(fId, fWeight, EnumEnchantmentType.weapon);
		this.setName("disjunction");
		addToBookList(this);
	}
	
	@Override
	public int getMaxLevel()
    {
        return 5;
    }
	
	@Override
    public int getMinEnchantability(int par1)
    {
        return 5 + (par1 - 1) * 8;
    }

    @Override
    public int getMaxEnchantability(int par1)
    {
        return this.getMinEnchantability(par1) + 20;
    }
    
    @Override
    public boolean canApplyTogether(Enchantment fTest)
    {
    	if(fTest == MoreEnchants.enchantDefusing || fTest == MoreEnchants.enchantDisjunction || fTest == MoreEnchants.enchantDowsing || fTest == MoreEnchants.enchantSpellbane || fTest instanceof EnchantmentDamage)
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
		
		int levelDisjunction = EnchantmentHelper.getEnchantmentLevel(effectId, dmgSource);
		if(fEvent.entity instanceof EntityEnderman)
		{				
			fEvent.ammount = fEvent.ammount + (2.5F * levelDisjunction);
			if(levelDisjunction > 4)
				fEvent.ammount = fEvent.ammount + 0.5F;
		}
    }
}
