package com.demoxin.minecraft.moreenchants.armor;

import java.util.UUID;

import com.demoxin.minecraft.moreenchants.MoreEnchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentSteadfast extends Enchantment
{
	public static UUID steadfastUUID = UUID.fromString("603B0660-7D55-11E3-BAA7-0800200C9A66");
	
	public EnchantmentSteadfast(int fId, int fWeight)
	{
		super(fId, fWeight, EnumEnchantmentType.armor_torso);
		this.setName("steadfast");
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
    	if(fTest == MoreEnchants.enchantBerserk || fTest == MoreEnchants.enchantSteadfast)
    		return false;
    	return true;
    }
    
    public boolean canApply(ItemStack fTest)
    {
    	if(fTest.getItem() instanceof ItemArmor)
    	{
    		ItemArmor itemArmor = (ItemArmor)fTest.getItem();
    		if(itemArmor.armorType == 1)
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
		ItemStack boots = entity.getEquipmentInSlot(3);
		
		if(boots == null)
		{
			RemoveKnockbackBuff(entity);
			return;
		}
		
		int level = EnchantmentHelper.getEnchantmentLevel(effectId, boots);
		
		if(level > 0)
			AddKnockbackBuff(entity);
		else
			RemoveKnockbackBuff(entity);
    }
    
    private void AddKnockbackBuff(EntityLivingBase fEntity)
	{
		IAttributeInstance speedAttr = fEntity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.knockbackResistance);
		
		if(speedAttr.getModifier(steadfastUUID) != null)
			return;
		
		AttributeModifier modSpeed = new AttributeModifier(steadfastUUID, "Steadfast", 0.7D, 1); 
		
		// Creatures and Players use 2 different values here
		speedAttr.removeModifier(modSpeed);
		speedAttr.applyModifier(modSpeed);
	}
	
	private void RemoveKnockbackBuff(EntityLivingBase fEntity)
	{
		IAttributeInstance speedAttr = fEntity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.knockbackResistance);
				
		if(speedAttr.getModifier(steadfastUUID) == null)
			return;
		
		AttributeModifier modSpeed = new AttributeModifier(steadfastUUID, "Steadfast", 0.7D, 1);
		speedAttr.removeModifier(modSpeed);
	}
}
