package com.demoxin.minecraft.moreenchants.armor;

import java.util.UUID;

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

public class EnchantmentFleetfooted extends Enchantment {
	
	public static UUID fleetfootUUID = UUID.fromString("edff168f-32d7-438b-8d29-189e9405e032");
	
	public EnchantmentFleetfooted(int fId, int fWeight)
	{
		super(fId, fWeight, EnumEnchantmentType.armor_feet);
		this.setName("fleetfoot");
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
    	if(fTest == this)
    		return false;
    	return true;
    }
    
    public boolean canApply(ItemStack fTest)
    {
    	if(fTest.getItem() instanceof ItemArmor)
    	{
    		ItemArmor itemArmor = (ItemArmor)fTest.getItem();
    		if(itemArmor.armorType == 3)
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
    	
    	fEvent.entity.worldObj.theProfiler.startSection("FleetfootHandler");
    	
    	EntityLivingBase entity = (EntityLivingBase)fEvent.entity;
		ItemStack boots = entity.getEquipmentInSlot(1);
		
		if(boots == null)
		{
			RemoveSpeedBuff(entity);
			fEvent.entity.worldObj.theProfiler.endSection();
			return;
		}
		
		int level = EnchantmentHelper.getEnchantmentLevel(effectId, boots);
		
		if(level > 0)
			AddSpeedBuff(entity);
		else
			RemoveSpeedBuff(entity);
		
		fEvent.entity.worldObj.theProfiler.endSection();
    }
    
    private void AddSpeedBuff(EntityLivingBase fEntity)
	{
		IAttributeInstance speedAttr = fEntity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed);
		
		if(speedAttr.getModifier(fleetfootUUID) != null)
			return;
		
		AttributeModifier modSpeed = new AttributeModifier(fleetfootUUID, "FleetfootedBoots", 0.2D, 1); 
		
		// Creatures and Players use 2 different values here
		speedAttr.removeModifier(modSpeed);
		speedAttr.applyModifier(modSpeed);
	}
	
	private void RemoveSpeedBuff(EntityLivingBase fEntity)
	{
		IAttributeInstance speedAttr = fEntity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed);
				
		if(speedAttr.getModifier(fleetfootUUID) == null)
			return;
		
		AttributeModifier modSpeed = new AttributeModifier(fleetfootUUID, "FleetfootedBoots", 0.2D, 1);
		speedAttr.removeModifier(modSpeed);
	}
}
