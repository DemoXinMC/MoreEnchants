package com.demoxin.minecraft.moreenchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class Enchantment_PoisonProtect extends Enchantment {
	public Enchantment_PoisonProtect(int fId, int fWeight)
	{
		super(fId, fWeight, EnumEnchantmentType.armor);
		this.setName("poisonprotect");
		addToBookList(this);
	}
	
	@Override
	public int getMaxLevel()
    {
        return 4;
    }
	
	@Override
    public int getMinEnchantability(int par1)
    {
		return 10 + (par1 - 1) * 8;
    }

    @Override
    public int getMaxEnchantability(int par1)
    {
    	return this.getMinEnchantability(par1) + 12;
    }
    
    @Override
    public boolean canApplyTogether(Enchantment fTest)
    {
    	if(fTest instanceof Enchantment_PoisonProtect || fTest instanceof EnchantmentProtection)
    		return false;
    	return true;
    }
    
    public boolean canApply(ItemStack fTest)
    {
    	if(fTest.getItem() instanceof ItemArmor || fTest.getItem() instanceof ItemBook)
    		return true;
        return false;
    }
    
    @ForgeSubscribe
    public void HandleEnchant(LivingHurtEvent fEvent)
    {
    	if(fEvent.source != DamageSource.magic)
			return;
    	
    	if(!(fEvent.entity instanceof EntityLivingBase))
    		return;
    	
    	EntityLivingBase victim = (EntityLivingBase)fEvent.entity;
		int[] levelProtect = new int[4];
		levelProtect[0] = EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantPoisonProtect.effectId, victim.getCurrentItemOrArmor(4));
		levelProtect[1] = EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantPoisonProtect.effectId, victim.getCurrentItemOrArmor(3));
		levelProtect[2] = EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantPoisonProtect.effectId, victim.getCurrentItemOrArmor(2));
		levelProtect[3] = EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantPoisonProtect.effectId, victim.getCurrentItemOrArmor(1));
		
		for(int i = 0; i < levelProtect.length; ++i)
		{
			if(levelProtect[i] > 0)
			{
				float dmgMod = (float)(6 + levelProtect[i] * levelProtect[i]) / 3.0F;
				dmgMod = MathHelper.floor_float(dmgMod * 1.25F);
				dmgMod = dmgMod / 100;
				dmgMod = dmgMod * 5;
				dmgMod = 1.0F - dmgMod;
				fEvent.ammount = fEvent.ammount * dmgMod;
			}
		}
    }
}
