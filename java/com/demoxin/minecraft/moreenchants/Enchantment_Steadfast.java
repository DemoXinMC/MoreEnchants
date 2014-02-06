package com.demoxin.minecraft.moreenchants;

import java.util.UUID;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeInstance;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class Enchantment_Steadfast extends Enchantment
{
	public static UUID steadfastUUID = UUID.fromString("603B0660-7D55-11E3-BAA7-0800200C9A66");
	
	public Enchantment_Steadfast(int fId, int fWeight)
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
    	if(fTest instanceof Enchantment_Steadfast)
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
    
    @ForgeSubscribe
    public void HandleEnchant(LivingUpdateEvent fEvent)
    {
    	if(!(fEvent.entity instanceof EntityLivingBase))
    		return;
    	
    	EntityLivingBase entity = (EntityLivingBase)fEvent.entity;
		ItemStack boots = entity.getCurrentItemOrArmor(3);
		
		if(boots == null)
		{
			RemoveKnockbackBuff(entity);
			return;
		}
		
		int level = EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantSteadfast.effectId, boots);
		
		if(level > 0)
			AddKnockbackBuff(entity);
		else
			RemoveKnockbackBuff(entity);
    }
    
    private void AddKnockbackBuff(EntityLivingBase fEntity)
	{
		AttributeInstance speedAttr = fEntity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.knockbackResistance);
		
		if(speedAttr.getModifier(steadfastUUID) != null)
			return;
		
		AttributeModifier modSpeed = new AttributeModifier(steadfastUUID, "Steadfast", 0.7D, 1); 
		
		// Creatures and Players use 2 different values here
		speedAttr.removeModifier(modSpeed);
		speedAttr.applyModifier(modSpeed);
	}
	
	private void RemoveKnockbackBuff(EntityLivingBase fEntity)
	{
		AttributeInstance speedAttr = fEntity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.knockbackResistance);
				
		if(speedAttr.getModifier(steadfastUUID) == null)
			return;
		
		AttributeModifier modSpeed = new AttributeModifier(steadfastUUID, "Steadfast", 0.7D, 1);
		speedAttr.removeModifier(modSpeed);
	}
}
