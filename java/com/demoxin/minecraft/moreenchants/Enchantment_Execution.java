package com.demoxin.minecraft.moreenchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class Enchantment_Execution extends Enchantment
{
	public Enchantment_Execution(int fId, int fWeight)
	{
		super(fId, fWeight, EnumEnchantmentType.weapon);
		this.setName("execute");
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
		return 5 + 20 * (par1 - 1);
	}

	@Override
	public int getMaxEnchantability(int par1)
	{
		return super.getMinEnchantability(par1) + 50;
	}
	    
	@Override
	public boolean canApplyTogether(Enchantment fTest)
	{
		if(fTest == MoreEnchants.enchantMending || fTest == MoreEnchants.enchantLeech || fTest == MoreEnchants.enchantExecution)
			return false;
		if(fTest == Enchantment.knockback || fTest == Enchantment.fireAspect || fTest == MoreEnchants.enchantIceAspect || fTest == MoreEnchants.enchantVenom)
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
		ItemStack weapon = attacker.getHeldItem();
						
		if(weapon == null)
			return;
						
		if(EnchantmentHelper.getEnchantmentLevel(effectId, weapon) != 0)
		{
			float defenderHealthPercent = fEvent.entityLiving.getHealth() / fEvent.entityLiving.getMaxHealth();
			float dmgMod = 1.0f - defenderHealthPercent;
			dmgMod = 1.0F + dmgMod;
						
			fEvent.ammount = fEvent.ammount * dmgMod;
		}
	}
}
