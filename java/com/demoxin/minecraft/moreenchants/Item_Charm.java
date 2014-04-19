package com.demoxin.minecraft.moreenchants;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Item_Charm extends Item
{
	private ArrayList<Enchantment> validEnchants;
	private int rarity;
	private IIcon[] icons;
	
	public Item_Charm(int fRarity)
	{
		super();
		
		setMaxStackSize(1);
		setHasSubtypes(true);
		setUnlocalizedName("charm");
		setCreativeTab(CreativeTabs.tabMisc);
		
		rarity = fRarity;
		validEnchants = new ArrayList<Enchantment>();
	}
	
	public void addEnchants()
	{
		validEnchants.add(Enchantment.sharpness);
		validEnchants.add(Enchantment.baneOfArthropods);
		validEnchants.add(Enchantment.smite);
		if(MoreEnchants.enchantDefusing != null)
			validEnchants.add(MoreEnchants.enchantDefusing);
		if(MoreEnchants.enchantDisjunction != null)
			validEnchants.add(MoreEnchants.enchantDisjunction);
		if(MoreEnchants.enchantDowsing != null)
			validEnchants.add(MoreEnchants.enchantDowsing);
		if(MoreEnchants.enchantSpellbane != null)
			validEnchants.add(MoreEnchants.enchantSpellbane);
		validEnchants.add(Enchantment.protection);
		validEnchants.add(Enchantment.fireProtection);
		validEnchants.add(Enchantment.projectileProtection);
		validEnchants.add(Enchantment.blastProtection);
		if(MoreEnchants.enchantPoisonProtect != null)
			validEnchants.add(MoreEnchants.enchantPoisonProtect);
		if(MoreEnchants.enchantResurrection != null)
			validEnchants.add(MoreEnchants.enchantResurrection);
	}
	
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister fIconRegister)
    {
        icons = new IIcon[8];
 
        for (int i = 0; i < icons.length; i++)
        {
            icons[i] = fIconRegister.registerIcon(MoreEnchants.MODID + ":charm_" + i);
        }
    }
	
	@Override
    public IIcon getIconFromDamage(int fMeta)
    {
		if(fMeta > (icons.length-1))
			fMeta = 0;
        return icons[fMeta];
    }
	
	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return EnumRarity.epic;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item fItem, CreativeTabs tabs, List list)
	{
		for(int i = 0; i < validEnchants.size(); ++i)
		{
			ItemStack charm = new ItemStack(this, 1);
			charm.addEnchantment(validEnchants.get(i), 1);
			list.add(charm);
		}
	}
	
	@SubscribeEvent
	public void HandleDrop(LivingDropsEvent fEvent)
	{
		if(!fEvent.recentlyHit)
			return;
		
		if(!(fEvent.entityLiving.isCreatureType(EnumCreatureType.monster, false)))
			return;
		
		if(fEvent.entity.worldObj.rand.nextInt(rarity) > 1)
			return;
		
		int dropTexture = (fEvent.entity.worldObj.rand.nextInt(icons.length)-1);
		ItemStack dropCharm = new ItemStack(this, 1, dropTexture);
		
		Enchantment dropEnchant = validEnchants.get(fEvent.entity.worldObj.rand.nextInt(validEnchants.size())-1);
		dropCharm.addEnchantment(dropEnchant, 1);
		fEvent.drops.add(new EntityItem(fEvent.entity.worldObj, fEvent.entity.posX, fEvent.entity.posY, fEvent.entity.posZ, dropCharm));
	}
	
	@SubscribeEvent
	public void HandleSharpness(LivingHurtEvent fEvent)
	{	
		EntityPlayer attacker;
		
		if(fEvent.source.damageType == "player")
			attacker = (EntityPlayer)fEvent.source.getSourceOfDamage();
		else if (fEvent.source.damageType == "arrow")
		{
			if(!(((EntityArrow)fEvent.source.getSourceOfDamage()).shootingEntity instanceof EntityPlayer))
				return;
			attacker = ((EntityPlayer)((EntityArrow)fEvent.source.getSourceOfDamage()).shootingEntity);
		}
		else
			return;
		
    	if(attacker == null)
    		return;
    	
    	IInventory playerInv = attacker.inventory;
    	
    	int level = 0;
    	
    	for(int i = 0; i < playerInv.getSizeInventory(); i++)
    	{
    		ItemStack checkItem = playerInv.getStackInSlot(i);
    		
    		if(EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, checkItem) > 0)
    			level += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, checkItem);
    	}
    	
    	if(level > 0)
    		fEvent.ammount += (float)level;
	}
	
	@SubscribeEvent
	public void HandleBoA(LivingHurtEvent fEvent)
	{
		EntityPlayer attacker;
		
		if(fEvent.source.damageType == "player")
			attacker = (EntityPlayer)fEvent.source.getSourceOfDamage();
		else if (fEvent.source.damageType == "arrow")
		{
			if(!(((EntityArrow)fEvent.source.getSourceOfDamage()).shootingEntity instanceof EntityPlayer))
				return;
			attacker = ((EntityPlayer)((EntityArrow)fEvent.source.getSourceOfDamage()).shootingEntity);
		}
		else
			return;
		
    	if(attacker == null)
    		return;
		
		if(fEvent.entityLiving.getCreatureAttribute() != EnumCreatureAttribute.ARTHROPOD)
			return;
    	
    	IInventory playerInv = attacker.inventory;
    	
    	int level = 0;
    	
    	for(int i = 0; i < playerInv.getSizeInventory(); i++)
    	{
    		ItemStack checkItem = playerInv.getStackInSlot(i);
    		
    		if(EnchantmentHelper.getEnchantmentLevel(Enchantment.baneOfArthropods.effectId, checkItem) > 0)
    			level += EnchantmentHelper.getEnchantmentLevel(Enchantment.baneOfArthropods.effectId, checkItem);
    	}
    	
    	if(level > 0)
    		fEvent.ammount += (float)level * 2.0F;
	}
	
	@SubscribeEvent
	public void HandleSmite(LivingHurtEvent fEvent)
	{
		EntityPlayer attacker;
		
		if(fEvent.source.damageType == "player")
			attacker = (EntityPlayer)fEvent.source.getSourceOfDamage();
		else if (fEvent.source.damageType == "arrow")
		{
			if(!(((EntityArrow)fEvent.source.getSourceOfDamage()).shootingEntity instanceof EntityPlayer))
				return;
			attacker = ((EntityPlayer)((EntityArrow)fEvent.source.getSourceOfDamage()).shootingEntity);
		}
		else
			return;
		
    	if(attacker == null)
    		return;
		
		if(fEvent.entityLiving.getCreatureAttribute() != EnumCreatureAttribute.UNDEAD)
			return;
    	
    	IInventory playerInv = attacker.inventory;
    	
    	int level = 0;
    	
    	for(int i = 0; i < playerInv.getSizeInventory(); i++)
    	{
    		ItemStack checkItem = playerInv.getStackInSlot(i);
    		
    		if(EnchantmentHelper.getEnchantmentLevel(Enchantment.smite.effectId, checkItem) > 0)
    			level += EnchantmentHelper.getEnchantmentLevel(Enchantment.smite.effectId, checkItem);
    	}
    	
    	if(level > 0)
    		fEvent.ammount += (float)level * 2.0F;
	}
	
	@SubscribeEvent
	public void HandleDefusing(LivingHurtEvent fEvent)
	{
		if(MoreEnchants.enchantDefusing == null)
			return;
		
		EntityPlayer attacker;
		
		if(fEvent.source.damageType == "player")
			attacker = (EntityPlayer)fEvent.source.getSourceOfDamage();
		else if (fEvent.source.damageType == "arrow")
		{
			if(!(((EntityArrow)fEvent.source.getSourceOfDamage()).shootingEntity instanceof EntityPlayer))
				return;
			attacker = ((EntityPlayer)((EntityArrow)fEvent.source.getSourceOfDamage()).shootingEntity);
		}
		else
			return;
		
    	if(attacker == null)
    		return;
		
		if(!(fEvent.entityLiving instanceof EntityCreeper))
			return;
    	
    	IInventory playerInv = attacker.inventory;
    	
    	int level = 0;
    	
    	for(int i = 0; i < playerInv.getSizeInventory(); i++)
    	{
    		ItemStack checkItem = playerInv.getStackInSlot(i);
    		
    		if(EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantDefusing.effectId, checkItem) > 0)
    			level += EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantDefusing.effectId, checkItem);
    	}
    	
    	if(level > 0)
    		fEvent.ammount += (float)level * 2.0F;
	}
	
	@SubscribeEvent
	public void HandleDisjunction(LivingHurtEvent fEvent)
	{
		if(MoreEnchants.enchantDisjunction == null)
			return;
		
		EntityPlayer attacker;
		
		if(fEvent.source.damageType == "player")
			attacker = (EntityPlayer)fEvent.source.getSourceOfDamage();
		else if (fEvent.source.damageType == "arrow")
		{
			if(!(((EntityArrow)fEvent.source.getSourceOfDamage()).shootingEntity instanceof EntityPlayer))
				return;
			attacker = ((EntityPlayer)((EntityArrow)fEvent.source.getSourceOfDamage()).shootingEntity);
		}
		else
			return;
		
    	if(attacker == null)
    		return;
		
		if(!(fEvent.entityLiving instanceof EntityEnderman))
			return;
    	
    	IInventory playerInv = attacker.inventory;
    	
    	int level = 0;
    	
    	for(int i = 0; i < playerInv.getSizeInventory(); i++)
    	{
    		ItemStack checkItem = playerInv.getStackInSlot(i);
    		
    		if(EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantDisjunction.effectId, checkItem) > 0)
    			level += EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantDisjunction.effectId, checkItem);
    	}
    	
    	if(level > 0)
    		fEvent.ammount += (float)level * 2.0F;
	}
	
	@SubscribeEvent
	public void HandleDowsing(LivingHurtEvent fEvent)
	{
		if(MoreEnchants.enchantDowsing == null)
			return;
		
		EntityPlayer attacker;
		
		if(fEvent.source.damageType == "player")
			attacker = (EntityPlayer)fEvent.source.getSourceOfDamage();
		else if (fEvent.source.damageType == "arrow")
		{
			if(!(((EntityArrow)fEvent.source.getSourceOfDamage()).shootingEntity instanceof EntityPlayer))
				return;
			attacker = ((EntityPlayer)((EntityArrow)fEvent.source.getSourceOfDamage()).shootingEntity);
		}
		else
			return;
		
    	if(attacker == null)
    		return;
		
		if(!(fEvent.entityLiving instanceof EntityBlaze) && !(fEvent.entityLiving instanceof EntityMagmaCube))
			return;
    	
    	IInventory playerInv = attacker.inventory;
    	
    	int level = 0;
    	
    	for(int i = 0; i < playerInv.getSizeInventory(); i++)
    	{
    		ItemStack checkItem = playerInv.getStackInSlot(i);
    		
    		if(EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantDowsing.effectId, checkItem) > 0)
    			level += EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantDowsing.effectId, checkItem);
    	}
    	
    	if(level > 0)
    		fEvent.ammount += (float)level * 2.0F;
	}
	
	@SubscribeEvent
	public void HandleSpellbane(LivingHurtEvent fEvent)
	{
		if(MoreEnchants.enchantSpellbane == null)
			return;
		
		EntityPlayer attacker;
		
		if(fEvent.source.damageType == "player")
			attacker = (EntityPlayer)fEvent.source.getSourceOfDamage();
		else if (fEvent.source.damageType == "arrow")
		{
			if(!(((EntityArrow)fEvent.source.getSourceOfDamage()).shootingEntity instanceof EntityPlayer))
				return;
			attacker = ((EntityPlayer)((EntityArrow)fEvent.source.getSourceOfDamage()).shootingEntity);
		}
		else
			return;
		
    	if(attacker == null)
    		return;
		
		if(!(fEvent.entityLiving instanceof EntityWitch))
			return;
    	
    	IInventory playerInv = attacker.inventory;
    	
    	int level = 0;
    	
    	for(int i = 0; i < playerInv.getSizeInventory(); i++)
    	{
    		ItemStack checkItem = playerInv.getStackInSlot(i);
    		
    		if(EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantSpellbane.effectId, checkItem) > 0)
    			level += EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantSpellbane.effectId, checkItem);
    	}
    	
    	if(level > 0)
    		fEvent.ammount += (float)level * 2.0F;
	}
	
	@SubscribeEvent
	public void HandleProtection(LivingHurtEvent fEvent)
	{
		if(!(fEvent.entityLiving instanceof EntityPlayer))
			return;
		
		EntityPlayer victim = ((EntityPlayer)fEvent.entityLiving);
		
    	if(victim == null)
    		return;
    	
    	IInventory playerInv = victim.inventory;
    	
    	int level = 0;
    	
    	for(int i = 0; i < playerInv.getSizeInventory(); i++)
    	{
    		ItemStack checkItem = playerInv.getStackInSlot(i);
    		
    		if(EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, checkItem) > 0)
    			level += EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, checkItem);
    	}
    	
    	if(level > 0)
    	{
    		float modifier = (float)level * 0.06F;
    		if(modifier > 0.5F)
    			modifier = 0.5F;
    		modifier = 1.0F - modifier;
    		fEvent.ammount *= modifier;
    	}
	}
	
	@SubscribeEvent
	public void HandleFireProtection(LivingHurtEvent fEvent)
	{
		if(!(fEvent.entityLiving instanceof EntityPlayer))
			return;
		
		if(!fEvent.source.isFireDamage())
			return;
		
		EntityPlayer victim = ((EntityPlayer)fEvent.entityLiving);
		
    	if(victim == null)
    		return;
    	
    	IInventory playerInv = victim.inventory;
    	
    	int level = 0;
    	
    	for(int i = 0; i < playerInv.getSizeInventory(); i++)
    	{
    		ItemStack checkItem = playerInv.getStackInSlot(i);
    		
    		if(EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, checkItem) > 0)
    			level += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, checkItem);
    	}
    	
    	if(level > 0)
    	{
    		float modifier = (float)level * 0.1F;
    		if(modifier > 0.5F)
    			modifier = 0.5F;
    		modifier = 1.0F - modifier;
    		fEvent.ammount *= modifier;
    	}
	}
	
	@SubscribeEvent
	public void HandleBlastProtection(LivingHurtEvent fEvent)
	{
		if(!(fEvent.entityLiving instanceof EntityPlayer))
			return;
		
		if(!fEvent.source.isExplosion())
			return;
		
		EntityPlayer victim = ((EntityPlayer)fEvent.entityLiving);
		
    	if(victim == null)
    		return;
    	
    	IInventory playerInv = victim.inventory;
    	
    	int level = 0;
    	
    	for(int i = 0; i < playerInv.getSizeInventory(); i++)
    	{
    		ItemStack checkItem = playerInv.getStackInSlot(i);
    		
    		if(EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, checkItem) > 0)
    			level += EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, checkItem);
    	}
    	
    	if(level > 0)
    	{
    		float modifier = (float)level * 0.1F;
    		if(modifier > 0.5F)
    			modifier = 0.5F;
    		modifier = 1.0F - modifier;
    		fEvent.ammount *= modifier;
    	}
	}
	
	@SubscribeEvent
	public void HandleProjectileProtection(LivingHurtEvent fEvent)
	{
		if(!(fEvent.entityLiving instanceof EntityPlayer))
			return;
		
		if(!fEvent.source.isProjectile())
			return;
		
		EntityPlayer victim = ((EntityPlayer)fEvent.entityLiving);
		
    	if(victim == null)
    		return;
    	
    	IInventory playerInv = victim.inventory;
    	
    	int level = 0;
    	
    	for(int i = 0; i < playerInv.getSizeInventory(); i++)
    	{
    		ItemStack checkItem = playerInv.getStackInSlot(i);
    		
    		if(EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, checkItem) > 0)
    			level += EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, checkItem);
    	}
    	
    	if(level > 0)
    	{
    		float modifier = (float)level * 0.1F;
    		if(modifier > 0.5F)
    			modifier = 0.5F;
    		modifier = 1.0F - modifier;
    		fEvent.ammount *= modifier;
    	}
	}
	
	@SubscribeEvent
	public void HandlePoisonProtection(LivingHurtEvent fEvent)
	{
		if(MoreEnchants.enchantPoisonProtect == null)
			return;
		
		if(!(fEvent.entityLiving instanceof EntityPlayer))
			return;
		
		if(!fEvent.source.isMagicDamage())
			return;
		
		EntityPlayer victim = ((EntityPlayer)fEvent.entityLiving);
		
    	if(victim == null)
    		return;
    	
    	IInventory playerInv = victim.inventory;
    	
    	int level = 0;
    	
    	for(int i = 0; i < playerInv.getSizeInventory(); i++)
    	{
    		ItemStack checkItem = playerInv.getStackInSlot(i);
    		
    		if(EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantPoisonProtect.effectId, checkItem) > 0)
    			level += EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantPoisonProtect.effectId, checkItem);
    	}
    	
    	if(level > 0)
    	{
    		float modifier = (float)level * 0.1F;
    		if(modifier > 0.5F)
    			modifier = 0.5F;
    		modifier = 1.0F - modifier;
    		fEvent.ammount *= modifier;
    	}
	}
}
