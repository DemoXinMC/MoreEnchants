package com.demoxin.minecraft.moreenchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class Enchantment_Mobility extends Enchantment
{	
	public Enchantment_Mobility(int fId, int fWeight)
	{
		super(fId, fWeight, EnumEnchantmentType.armor_legs);
		this.setName("mobility");
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
		return 10;
    }

    @Override
    public int getMaxEnchantability(int par1)
    {
    	return this.getMinEnchantability(par1) + 40;
    }
    
    @Override
    public boolean canApplyTogether(Enchantment fTest)
    {
    	if(fTest == MoreEnchants.enchantAgility || fTest == MoreEnchants.enchantMobility)
    		return false;
    	return true;
    }
    
    public boolean canApply(ItemStack fTest)
    {
    	if(fTest.getItem() instanceof ItemArmor)
    	{
    		ItemArmor itemArmor = (ItemArmor)fTest.getItem();
    		if(itemArmor.armorType == 2)
    		{
    			return true;
    		}
    	}
        return false;
    }
    
    @SubscribeEvent
    public void HandleEnchant(LivingUpdateEvent fEvent)
    {
    	if(!(fEvent.entity instanceof EntityLivingBase))
    		return;
    	
    	EntityLivingBase entity = (EntityLivingBase)fEvent.entity;
		ItemStack pants = entity.getEquipmentInSlot(2);
		
		if(pants == null)
		{
			RemoveMovementBuff(entity);
			return;
		}
		
		int level = EnchantmentHelper.getEnchantmentLevel(effectId, pants);
		
		if(level > 0)
			AddMovementBuff(entity);
		else
			RemoveMovementBuff(entity);
    }
    
    private void AddMovementBuff(EntityLivingBase fEntity)
	{
		fEntity.stepHeight = 1.0F;
	}
	
	private void RemoveMovementBuff(EntityLivingBase fEntity)
	{
		fEntity.stepHeight = 0.5F;
	}
}
