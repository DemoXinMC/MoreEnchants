package com.demoxin.minecraft.moreenchants.armor;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentPoisonProtect extends Enchantment {
	public EnchantmentPoisonProtect(int fId, int fWeight)
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
    	if(fTest instanceof EnchantmentPoisonProtect || fTest instanceof EnchantmentProtection)
    		return false;
    	return true;
    }
    
    public boolean canApply(ItemStack fTest)
    {
    	return Enchantment.fireProtection.canApply(fTest);
    }
    
    @SubscribeEvent
    public void HandleEnchant(LivingHurtEvent fEvent)
    {
    	if(fEvent.source != DamageSource.magic)
			return;
    	
    	if(!(fEvent.entity instanceof EntityLivingBase))
    		return;
    	
    	EntityLivingBase victim = (EntityLivingBase)fEvent.entity;
		int[] levelProtect = new int[4];
		levelProtect[0] = EnchantmentHelper.getEnchantmentLevel(effectId, victim.getEquipmentInSlot(4));
		levelProtect[1] = EnchantmentHelper.getEnchantmentLevel(effectId, victim.getEquipmentInSlot(3));
		levelProtect[2] = EnchantmentHelper.getEnchantmentLevel(effectId, victim.getEquipmentInSlot(2));
		levelProtect[3] = EnchantmentHelper.getEnchantmentLevel(effectId, victim.getEquipmentInSlot(1));
		
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
