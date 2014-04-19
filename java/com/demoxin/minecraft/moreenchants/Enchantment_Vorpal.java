package com.demoxin.minecraft.moreenchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class Enchantment_Vorpal extends Enchantment {
	public Enchantment_Vorpal(int fId, int fWeight)
	{
		super(fId, fWeight, EnumEnchantmentType.weapon);
		this.setName("vorpal");
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
		return 10 + 20 * (par1 - 1);
    }

    @Override
    public int getMaxEnchantability(int par1)
    {
        return super.getMinEnchantability(par1) + 50;
    }
    
    @Override
    public boolean canApplyTogether(Enchantment fTest)
    {
    	if(fTest == MoreEnchants.enchantMending)
    		return false;
    	return true;
    }
    
    @Override
    public boolean canApply(ItemStack fTest)
    {
    	return Enchantment.sharpness.canApply(fTest);
    }
    
    @SubscribeEvent
    public void HandleEnchant(LivingDropsEvent fEvent)
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
		
		if(fEvent.entity.worldObj.rand.nextInt(100) < 5)
		{
			ItemStack itemHead = null;
			if(fEvent.entity instanceof EntityCreeper)
				itemHead = new ItemStack(Items.skull, 1, 4);
				
			if(fEvent.entity instanceof EntitySkeleton)
				itemHead = new ItemStack(Items.skull, 1, 0);
			
			if(fEvent.entity instanceof EntityZombie)
				itemHead = new ItemStack(Items.skull, 1, 2);
			
			if(fEvent.entity instanceof EntityPlayer)
			{
				itemHead = new ItemStack(Items.skull, 1, 3);
				itemHead.setTagCompound(new NBTTagCompound());
				itemHead.getTagCompound().setString("SkullOwner", ((EntityPlayer)fEvent.entity).getCommandSenderName());
			}
			
			if(fEvent.entity instanceof EntitySpider)
			{
				itemHead = new ItemStack(Items.skull, 1, 3);
				itemHead.setTagCompound(new NBTTagCompound());
				itemHead.getTagCompound().setString("SkullOwner", "MHF_Spider");
			}
			
			if(fEvent.entity instanceof EntityCaveSpider)
			{
				itemHead = new ItemStack(Items.skull, 1, 3);
				itemHead.setTagCompound(new NBTTagCompound());
				itemHead.getTagCompound().setString("SkullOwner", "MHF_CaveSpider");
			}
			
			if(fEvent.entity instanceof EntityEnderman)
			{
				itemHead = new ItemStack(Items.skull, 1, 3);
				itemHead.setTagCompound(new NBTTagCompound());
				itemHead.getTagCompound().setString("SkullOwner", "MHF_Enderman");
			}
			
			if(fEvent.entity instanceof EntityPigZombie)
			{
				itemHead = new ItemStack(Items.skull, 1, 3);
				itemHead.setTagCompound(new NBTTagCompound());
				itemHead.getTagCompound().setString("SkullOwner", "MHF_PigZombie");
			}
			
			if(fEvent.entity instanceof EntityBlaze)
			{
				itemHead = new ItemStack(Items.skull, 1, 3);
				itemHead.setTagCompound(new NBTTagCompound());
				itemHead.getTagCompound().setString("SkullOwner", "MHF_Blaze");
			}
			
			if(itemHead != null)
			{
				EntityItem entityItem = new EntityItem(fEvent.entity.worldObj, fEvent.entity.posX, fEvent.entity.posY, fEvent.entity.posZ, itemHead);
				fEvent.drops.add(entityItem);
			}
		}
	
    }
    
    @SubscribeEvent
    public void HandleEnchant(LivingHurtEvent fEvent)
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
		
		if(fEvent.entity.worldObj.rand.nextInt(100) < 5)
			fEvent.ammount *= 3;
    }
}
