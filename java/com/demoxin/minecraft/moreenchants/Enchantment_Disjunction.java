package com.demoxin.minecraft.moreenchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class Enchantment_Disjunction extends Enchantment {
	public Enchantment_Disjunction(int fId, int fWeight)
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
    	if(fTest instanceof Enchantment_Defusing || fTest instanceof Enchantment_Disjunction || fTest instanceof Enchantment_Dowsing || fTest instanceof EnchantmentDamage)
    		return false;
    	return true;
    }
    
    public boolean canApply(ItemStack fTest)
    {
    	if(fTest.getItem() instanceof ItemSword || fTest.getItem() instanceof ItemBook)
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
    	if(attacker == null)
    		return;
    	
    	ItemStack dmgSource = ((EntityLivingBase)fEvent.source.getSourceOfDamage()).getHeldItem();
		if(dmgSource == null)
			return;
		
		if(EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantDisjunction.effectId, dmgSource) <= 0)
			return;
		
		int levelDisjunction = EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantDisjunction.effectId, dmgSource);
		if(fEvent.entity instanceof EntityEnderman)
		{				
			fEvent.ammount = fEvent.ammount + (2.5F * levelDisjunction);
			if(levelDisjunction > 4)
				fEvent.ammount = fEvent.ammount + 0.5F;
		}
    }
}
