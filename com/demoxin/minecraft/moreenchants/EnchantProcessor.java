package com.demoxin.minecraft.moreenchants;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.WatchableObject;
import net.minecraft.entity.ai.attributes.AttributeInstance;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class EnchantProcessor {
	private final int MENDING = 1;
	private final int MENDING2 = 2;
	private final int POISON = 4;

	public static UUID fleetfootUUID = UUID.fromString("edff168f-32d7-438b-8d29-189e9405e032");
	
	@ForgeSubscribe
	public void Handler_EntitySpawn(EntityJoinWorldEvent fEvent)
	{
		// Arrow manipulation for bow enchants
		// Only interested in arrows
		if(fEvent.entity instanceof EntityArrow)
		{
			// Let's check and see if our owner is something that's alive.
			if(((EntityArrow)fEvent.entity).shootingEntity instanceof EntityLivingBase)
			{
				// Let's grab their bow
				EntityLivingBase shooter = (EntityLivingBase)((EntityArrow)fEvent.entity).shootingEntity;
				ItemStack itemBow = shooter.getHeldItem();
				
				// Make sure our data int is nice and set-up.
				fEvent.entity.getDataWatcher().addObject(24, Integer.valueOf(0));
				
				// Mending Enchant
				int mendingLevel = EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantMending.effectId, itemBow);
				if(mendingLevel > 0)
				{
					// Set our fancy bitmask
					fEvent.entity.getDataWatcher().updateObject(24, fEvent.entity.getDataWatcher().getWatchableObjectInt(24) + Integer.valueOf(MENDING));
					if(mendingLevel == 2)
						fEvent.entity.getDataWatcher().updateObject(24, fEvent.entity.getDataWatcher().getWatchableObjectInt(24) + Integer.valueOf(MENDING2));
					
					// Remove knockback completely
					((EntityArrow)fEvent.entity).setKnockbackStrength(-1);
				}
				
				// Poison Enchant
				if(EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantPoison.effectId, itemBow) > 0)
					fEvent.entity.getDataWatcher().updateObject(24, fEvent.entity.getDataWatcher().getWatchableObjectInt(24) + Integer.valueOf(POISON));
				
			}
		}
		// End Arrow modification
		
		// Speed mods hack prevention
		if(fEvent.entity instanceof EntityLivingBase)
		{
			EntityLivingBase fEntity = (EntityLivingBase)fEvent.entity;
			AttributeInstance attrSpeed = fEntity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed);
			AttributeModifier modSpeed = new AttributeModifier(fleetfootUUID, "FleetfootedBoots", 0, 1);
			attrSpeed.removeModifier(modSpeed);
		}
		
	}
	
	@ForgeSubscribe
	public void Handler_PlaySoundAtEntity(PlaySoundAtEntityEvent fEvent)
	{
		// This is our SUPER hacky way of granting our experience bonus to players with the "Knowledge" enchant.
	
		if(fEvent.name != "random.orb")
			return;
		
		if(!(fEvent.entity instanceof EntityXPOrb))
			return;
		
		// This is an XP sound, let's figure out who our target player is.
		EntityPlayer player = fEvent.entity.worldObj.getClosestPlayerToEntity(fEvent.entity, 2.0D);
		
		// Let's check if they've got a Knowledge enchanted helm on.
		if(player == null)
			return;
		
		ItemStack armorHelm = player.getCurrentItemOrArmor(4);
		
		if(armorHelm == null)
			return;
		
		// Look for Knowledge
		if(EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantKnowledge.effectId, armorHelm) > 0)
		{
			// They have knowledge, let's give them double.
			player.addExperience(((EntityXPOrb)fEvent.entity).getXpValue());
		}
	}
	
	@ForgeSubscribe
	public void Handler_LivingUpdateTick(LivingUpdateEvent fEvent)
	{
		if(fEvent.entity instanceof EntityLivingBase)
		{			
			// Check for Fleetfooted
			// We know this is an EntityLivingBase at least already
			EntityLivingBase entity = (EntityLivingBase)fEvent.entity;
			ItemStack boots = entity.getCurrentItemOrArmor(1);
			
			if(boots == null)
			{
				RemoveSpeedBuff(entity);
				return;
			}
			
			// Look for the enchant on them.
			int level = EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantFleetfoot.effectId, boots);
						
			if(level > 0)
				AddSpeedBuff(entity);
			else
				RemoveSpeedBuff(entity);

		}
	}
	
	private void AddSpeedBuff(EntityLivingBase fEntity)
	{
		AttributeInstance speedAttr = fEntity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed);
		
		if(speedAttr.getModifier(fleetfootUUID) != null)
			return;
		
		AttributeModifier modSpeed = new AttributeModifier(fleetfootUUID, "FleetfootedBoots", 0.2D, 1); 
		
		// Creatures and Players use 2 different values here
		speedAttr.removeModifier(modSpeed);
		speedAttr.applyModifier(modSpeed);
	}
	
	private void RemoveSpeedBuff(EntityLivingBase fEntity)
	{
		AttributeInstance speedAttr = fEntity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed);
				
		if(speedAttr.getModifier(fleetfootUUID) == null)
			return;
		
		AttributeModifier modSpeed = new AttributeModifier(fleetfootUUID, "FleetfootedBoots", 0.2D, 1);
		speedAttr.removeModifier(modSpeed);
	}
	
	@ForgeSubscribe
	public void Handler_EntityHurt(LivingHurtEvent fEvent)
	{
		if(fEvent.source != DamageSource.magic && fEvent.source.damageType != "player" && fEvent.source.damageType != "mob" && fEvent.source.damageType != "arrow")
			return;
		
		// We're here, so this could be an event we're interested in.
		
		// Bow Enchants
		if(fEvent.source.damageType == "arrow")
		{
			EntityArrow strikingArrow = (EntityArrow)fEvent.source.getSourceOfDamage();
			// This is an arrow hit. We need to look for mending and poison.
			
			// Make sure we're getting bits safely.
			boolean bitsafe = false;
			@SuppressWarnings("unchecked")
			List<WatchableObject> watched = strikingArrow.getDataWatcher().getAllWatched();
			for(WatchableObject obj : watched)
			{
				if(obj.getDataValueId() == 24)
				{
					bitsafe = true;
					break;
				}
			}
			// Our necessary value isn't there, add it.
			if(!bitsafe)
				strikingArrow.getDataWatcher().addObject(24, Integer.valueOf(0));
			
			int infoBits = strikingArrow.getDataWatcher().getWatchableObjectInt(24);
			if((infoBits & MENDING) != 0)
			{
				// This is a mending arrow.
				// First thing, cancel the normal stuff associated with this arrow.
				fEvent.setCanceled(true);
				
				// Stop the arrow dead so that it doesn't move our "victim" around.
				fEvent.entity.setVelocity(0, 0, 0);
				
				// Next, figure out our damage modifier.
				float dmgMod = 0.3F;
				if((infoBits & MENDING2) != 0)
					dmgMod = dmgMod + 0.2F;
				
				((EntityLivingBase)fEvent.entity).heal(fEvent.ammount * dmgMod);
			}
			
			if((infoBits & POISON) != 0)
			{
				// We've got the poison mod.  We need to generate an effect based on the mending status of our bow.
				PotionEffect poisonResult;
				if((infoBits & MENDING) != 0)
					poisonResult = new PotionEffect(Potion.regeneration.getId(), 100, 1);
				else
					poisonResult = new PotionEffect(Potion.poison.getId(), 120, 1);
				
				((EntityLivingBase)fEvent.entity).addPotionEffect(poisonResult);
				
			}
		}
		
		// Melee Weapons
		if(fEvent.source.damageType == "player" || fEvent.source.damageType == "mob")
		{
						// The meat and potatoes of our damage enchants, we're looking for our damage-oriented enchants here, so let's get started.
			ItemStack dmgSource = ((EntityLivingBase)fEvent.source.getSourceOfDamage()).getHeldItem();
			if(dmgSource != null)
			{				
				// Our attacker is holding and item, we need to check it for each of our enchants.
				int levelMending = EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantMending.effectId, dmgSource);
				int levelVenom = EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantVenom.effectId, dmgSource);
				int levelDefusing = EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantDefusing.effectId, dmgSource);
				int levelCleave = EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantCleave.effectId, dmgSource);
				
				// Mending
				if(levelMending > 0)
				{
					// Mending enchant, convert to damage.
					fEvent.setCanceled(true);
					
					float healMod = 0.2F + (0.1F * levelMending);
					
					((EntityLivingBase)fEvent.entity).heal(fEvent.ammount * healMod);
					return;
				}
				
				// Venom
				if(levelVenom > 0)
				{
					// Process venom here.
					// We want to make our poison more toxic by its level, so our Venom level will be our amplifier.
					((EntityLivingBase)fEvent.entity).addPotionEffect(new PotionEffect(Potion.poison.getId(), 60, levelVenom));
				}
				
				// Defusing
				if(levelDefusing > 0)
				{
					// Creeper killer enchant, we need to increase the damage, but only if the victim is a creeper.
					if(fEvent.entity instanceof EntityCreeper)
					{				
						fEvent.ammount = fEvent.ammount + (2.5F * levelDefusing);
						if(levelDefusing > 4)
							fEvent.ammount = fEvent.ammount + 0.5F;
					}
				}
				
				// Cleave
				if(levelCleave > 0)
				{
					// We have a cleaving level, let's figure out our damage value.
					float splashDamage = fEvent.ammount * (levelCleave * 0.25F);
					
					// Next, find our entities to hit.
					EntityLivingBase attacker = (EntityLivingBase)fEvent.source.getSourceOfDamage();
					AxisAlignedBB boundBox = AxisAlignedBB.getBoundingBox(attacker.posX-5, attacker.posY-5, attacker.posZ-5, attacker.posX+5, attacker.posY+5, attacker.posZ+5);
					@SuppressWarnings("unchecked")
					ArrayList<Entity> targetEntities = new ArrayList<Entity>(attacker.worldObj.getEntitiesWithinAABBExcludingEntity(fEvent.entity, boundBox));
					
					// Let's remove all the entries that aren't within range of our attacker
					ListIterator<Entity> itr = targetEntities.listIterator();
					while(itr.hasNext())
					{
						Entity target = itr.next();
						
						if(target == attacker)
							continue;
						
						if(target.getDistanceToEntity(attacker) > 3.5F)
							continue;
						
						Vec3 attackerCheck = Vec3.createVectorHelper(target.posX-attacker.posX, target.posY-attacker.posY, target.posZ-attacker.posZ);
						double angle = Math.toDegrees(Math.acos(attackerCheck.normalize().dotProduct(attacker.getLookVec())));
						
						if(angle < 60.0D)
						{
							// This is within our arc, let's deal our damage.
							target.attackEntityFrom(DamageSource.generic, splashDamage);
						}
					}
				}
			}
		}
		// End Melee Weapons
		
		// Poison/Magic Protection
		if(fEvent.source == DamageSource.magic)
		{
			if(fEvent.entity instanceof EntityLivingBase)
			{
				// This is a living thing, so let's look through its armor slots and start reducing incoming damage.
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
		// End Poison Protection
		
		// Berserking
		if(fEvent.source.damageType == "player" || fEvent.source.damageType == "mob")
		{
			EntityLivingBase attacker = (EntityLivingBase)fEvent.source.getSourceOfDamage();
			// Grab their chest armor
			ItemStack armorChest = attacker.getCurrentItemOrArmor(3);
			
			if(armorChest == null)
				return;
			
			// See if they have berserking.  Berserking only needs to respect its existence.
			if(EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantBerserk.effectId, armorChest) != 0)
			{
				// We have berserking, let's go ahead and figure out how much to amplify the damage by.
				float attackerHealthPercent = attacker.getHealth() / attacker.getMaxHealth();
				float dmgMod = 1.0f - attackerHealthPercent;
				dmgMod = 1.0F + dmgMod;
				
				// Modify the event damage
				fEvent.ammount = fEvent.ammount * dmgMod;
			}
		}
		// End Berserking
		
		// Agility
		if(fEvent.source.damageType == "player" || fEvent.source.damageType == "mob")
		{
			EntityLivingBase victim = (EntityLivingBase)fEvent.entity;
			// Grab their chest armor
			ItemStack armorLegs = victim.getCurrentItemOrArmor(2);
			
			if(armorLegs == null)
				return;
			
			// See if we have agility.  Only should be 1 level
			if(EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantAgility.effectId, armorLegs) != 0)
			{
				if(fEvent.entity.worldObj.rand.nextInt(100) < 10)
				{
					// We hit our dodge, cancel the event.
					// Add sounds eventually maybe?
					//fEvent.entity.playSound("moreenchants:sounds/dodge", 1.0F, 1.0F);
					fEvent.setCanceled(true);
				}
			}
		}
	}
}
