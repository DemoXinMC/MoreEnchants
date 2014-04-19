package com.demoxin.minecraft.moreenchants;

import java.util.ArrayList;
import java.util.ListIterator;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class Enchantment_Cleave extends Enchantment {
	public Enchantment_Cleave(int fId, int fWeight)
	{
		super(fId, fWeight, EnumEnchantmentType.digger);
		this.setName("cleave");
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
        return 5 + (par1 - 1) * 8;
    }

    @Override
    public int getMaxEnchantability(int par1)
    {
    	return super.getMinEnchantability(par1) + 50;
    }
    
    @Override
    public boolean canApplyTogether(Enchantment fTest)
    {
    	if(fTest == this || fTest == Enchantment.knockback)
    		return false;
    	return true;
    }
    
    public boolean canApply(ItemStack fTest)
    {
    	if(fTest.getItem() instanceof ItemAxe || fTest.getItem() instanceof ItemBook)
    		return true;
        return false;
    }
    
    @SubscribeEvent
    public void HandleEnchant(LivingAttackEvent fEvent)
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
		
		if(EnchantmentHelper.getEnchantmentLevel(effectId, dmgSource) <= 0)
			return;
		
		// We have a cleaving level, let's figure out our damage value.
		float splashDamage = fEvent.ammount * (EnchantmentHelper.getEnchantmentLevel(effectId, dmgSource) * 0.25F);
		
		// Next, find our entities to hit.
		AxisAlignedBB boundBox = AxisAlignedBB.getBoundingBox(attacker.posX-5, attacker.posY-5, attacker.posZ-5, attacker.posX+5, attacker.posY+5, attacker.posZ+5);
		@SuppressWarnings("unchecked")
		ArrayList<Entity> targetEntities = new ArrayList<Entity>(attacker.worldObj.getEntitiesWithinAABBExcludingEntity(fEvent.entity, boundBox));
		
		// Let's remove all the entries that aren't within range of our attacker
		ListIterator<Entity> itr = targetEntities.listIterator();
		while(itr.hasNext())
		{
			Entity target = itr.next();
			
			if(!(target instanceof EntityLivingBase))
				continue;
			
			if(target == attacker)
				continue;
			
			if(target.getDistanceToEntity(attacker) > 3.5F)
				continue;
			
			Vec3 attackerCheck = Vec3.createVectorHelper(target.posX-attacker.posX, target.posY-attacker.posY, target.posZ-attacker.posZ);
			double angle = Math.toDegrees(Math.acos(attackerCheck.normalize().dotProduct(attacker.getLookVec())));
			
			if(angle < 60.0D)
			{
				// This is within our arc, let's deal our damage.
				DamageSource source = null;
				if(attacker instanceof EntityPlayer)
					source = new EntityDamageSource("player", attacker);
				if(attacker instanceof EntityMob)
					source = new EntityDamageSource("mob", attacker);
				
				if(source != null)
				{
					target.attackEntityFrom(DamageSource.generic, splashDamage);
				}
				
				if(attacker instanceof EntityPlayer)
				{
					// Apply knockback
					int modKnockback = 1;
					modKnockback += EnchantmentHelper.getKnockbackModifier(attacker, (EntityLivingBase)target);
					if(attacker.isSprinting())
						modKnockback++;
					
					if(modKnockback > 0)
						target.addVelocity((double)(-MathHelper.sin(attacker.rotationYaw * (float)Math.PI / 180.0F) * (float)modKnockback * 0.5F), 0.1D, (double)(MathHelper.cos(attacker.rotationYaw * (float)Math.PI / 180.0F) * (float)modKnockback * 0.5F));
				}
			}
		}
		
		// Stop the player sprinting
		attacker.setSprinting(false);
    }

}
