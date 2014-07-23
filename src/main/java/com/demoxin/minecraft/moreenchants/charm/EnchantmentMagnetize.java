package com.demoxin.minecraft.moreenchants.charm;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentMagnetize extends Enchantment
{
	public EnchantmentMagnetize(int fId, int fWeight)
	{
		super(fId, fWeight, EnumEnchantmentType.all);
		this.setName("magnetize");
		addToBookList(this);
	}
	
	@Override
	public int getMaxLevel()
    {
        return 3;
    }
	
	@Override
    public int getMinEnchantability(int par1)
    {
		return 1;
    }

    @Override
    public int getMaxEnchantability(int par1)
    {
    	return 1;
    }
    
    @Override
    public boolean canApplyTogether(Enchantment fTest)
    {
    	return false;
    }
    
    public boolean canApply(ItemStack fTest)
    {
    	return false;
    }
    
    @SubscribeEvent
    public void HandleEnchant(LivingUpdateEvent fEvent)
    {
    	if(!(fEvent.entity instanceof EntityPlayer))
    		return;
    	
    	fEvent.entity.worldObj.theProfiler.startSection("MagnetizeHandler");
    	
    	EntityPlayer player = (EntityPlayer)fEvent.entity;
		
		int level = 0;
		
		IInventory playerInv = player.inventory;
		
		for(int i = 0; i < playerInv.getSizeInventory(); i++)
    	{
    		ItemStack checkItem = playerInv.getStackInSlot(i);
    		
    		if(EnchantmentHelper.getEnchantmentLevel(this.effectId, checkItem) > 0)
    			level += EnchantmentHelper.getEnchantmentLevel(this.effectId, checkItem);
    	}
		
		if(level > 0)
		{
			float range = (level * 0.5F) + 0.5F;
			AxisAlignedBB searchBox = AxisAlignedBB.getBoundingBox(player.posX + 0.5 - range, player.posY + 0.5 - range, player.posZ + 0.5 - range, player.posX + 0.5 + range, player.posY + 0.5 + range, player.posZ + 0.5 + range);
			for(Object item : player.worldObj.getEntitiesWithinAABB(EntityItem.class, searchBox))
			{
				applyGravity(level, player, (EntityItem) item);
			}
		}
		
		fEvent.entity.worldObj.theProfiler.endSection();
			
    }
    
    // Fancy gravity function by tterrag1098
    private void applyGravity(int level, EntityPlayer target, EntityItem entity)
    {
    	double xCoord = target.posX;
    	double yCoord = target.posY;
    	double zCoord = target.posZ;
    	float range = (level * 0.5F) + 0.5F;
    	float gravStrength = level * 0.1F;
    	
    	float maxGravXZ = 1;
    	float maxGravY = 1;
    	float minGrav = 1;
    	
    	double dist = Math.sqrt(Math.pow(xCoord + 0.5 - entity.posX, 2) + Math.pow(zCoord + 0.5 - entity.posZ, 2) + Math.pow(yCoord + 0.5 - entity.posY, 2));

        if (dist > range)
        	return;

        double xDisplacment = entity.posX - (xCoord + 0.5);
        double yDisplacment = entity.posY - (yCoord + 0.5);
        double zDisplacment = entity.posZ - (zCoord + 0.5);

        double theta = Math.acos(zDisplacment / dist);
        double phi = Math.atan2(yDisplacment, xDisplacment);

        // Gravity decreases linearly
        double gravForce = gravStrength * (1 - dist / range);
        gravForce *= 2;

        double vecX = -gravForce * Math.sin(theta) * Math.cos(phi);
        double vecY = -gravForce * Math.sin(theta) * Math.sin(phi);
        double vecZ = -gravForce * Math.cos(theta);

        // trims gravity above max
        if (Math.abs(vecX) > maxGravXZ)
            vecX *= maxGravXZ / Math.abs(vecX);
        if (Math.abs(vecY) > maxGravY)
            vecY *= maxGravY / Math.abs(vecY);
        if (Math.abs(vecZ) > maxGravXZ)
            vecZ *= maxGravXZ / Math.abs(vecZ);

        // trims gravity below min
        if (Math.abs(vecX) < minGrav)
            vecX = 0;
        if (Math.abs(vecY) < minGrav)
            vecY = 0;
        if (Math.abs(vecZ) < minGrav)
            vecZ = 0;

        entity.setVelocity(entity.motionX + vecX, entity.motionY + vecY, entity.motionZ + vecZ);
    }
}
