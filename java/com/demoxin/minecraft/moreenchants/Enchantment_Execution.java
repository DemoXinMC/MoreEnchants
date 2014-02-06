package com.demoxin.minecraft.moreenchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentFireAspect;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentKnockback;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

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
		if(fTest instanceof Enchantment_Mending || fTest instanceof Enchantment_Leech || fTest instanceof Enchantment_Execution)
			return false;
		if(fTest instanceof EnchantmentKnockback || fTest instanceof EnchantmentFireAspect)
			return false;
		return true;
	}
	    
	@Override
	public boolean canApply(ItemStack fTest)
	{
		if(fTest.getItem() instanceof ItemSword || fTest.getItem() instanceof ItemAxe || fTest.getItem() instanceof ItemBook)
			return true;
		return false;
	}
	    
	@ForgeSubscribe
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
						
		if(EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantExecution.effectId, weapon) != 0)
		{
			float defenderHealthPercent = fEvent.entityLiving.getHealth() / fEvent.entityLiving.getMaxHealth();
			float dmgMod = 1.0f - defenderHealthPercent;
			dmgMod = 1.0F + dmgMod;
						
			fEvent.ammount = fEvent.ammount * dmgMod;
		}
	}
}
