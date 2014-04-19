package com.demoxin.minecraft.moreenchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;

public class Enchantment_Scattershot extends Enchantment
{
	public Enchantment_Scattershot(int fId, int fWeight)
	{
		super(fId, fWeight, EnumEnchantmentType.bow);
		this.setName("scattershot");
		addToBookList(this);
	}
	
	@Override
	public int getMinEnchantability(int par1)
    {
        return 1 + (par1 - 1) * 10;
    }

    @Override
    public int getMaxEnchantability(int par1)
    {
        return this.getMinEnchantability(par1) + 15;
    }

    @Override
    public int getMaxLevel()
    {
        return 5;
    }
    
    @Override
	public boolean canApplyTogether(Enchantment fTest)
	{
		if(fTest == Enchantment.infinity || fTest == MoreEnchants.enchantQuickdraw || fTest == MoreEnchants.enchantScattershot)
			return false;
		return true;
	}
    
    @Override
    public boolean canApply(ItemStack fTest)
    {
    	return Enchantment.infinity.canApply(fTest);
    }
    
    public void HandleEnchant(ArrowLooseEvent fEvent)
    {
    	int level = EnchantmentHelper.getEnchantmentLevel(effectId, fEvent.bow);
    	
    	if(level <= 0)
    		return;
    	
    	
    }
}
