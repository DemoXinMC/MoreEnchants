package com.demoxin.minecraft.moreenchants.general;

import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentResurrection extends Enchantment
{
	protected EnchantmentResurrection(int fId, int fWeight) {
		super(fId, fWeight, EnumEnchantmentType.all);
		this.setName("resurrection");
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
        return 15;
    }

    @Override
    public int getMaxEnchantability(int par1)
    {
    	return super.getMinEnchantability(par1) + 50;
    }
    
    @Override
    public boolean canApplyTogether(Enchantment fTest)
    {
    	return false;
    }
    
    public boolean canApply(ItemStack fTest)
    {
    	if(fTest.getItem() instanceof ItemBook)
    		return true;
        return false;
    }
    
    @SubscribeEvent
    public void HandleEnchant(LivingHurtEvent fEvent)
    {
    	if(!(fEvent.entity instanceof EntityPlayer))
    		return;
    	
    	EntityPlayer deadPlayer = (EntityPlayer)fEvent.entity;
    	IInventory playerInv = deadPlayer.inventory;
    	if((deadPlayer.getHealth() + deadPlayer.getAbsorptionAmount()) > (ArmorProperties.ApplyArmor(fEvent.entityLiving, deadPlayer.inventory.armorInventory, fEvent.source, fEvent.ammount)))
    		return;
    	
    	for(int i = 0; i < playerInv.getSizeInventory(); i++)
    	{
    		ItemStack checkItem = playerInv.getStackInSlot(i);
    		
    		if(checkItem == null)
    			continue;
    		
    		@SuppressWarnings("unchecked")
			Map<Short, Short> enchantList = EnchantmentHelper.getEnchantments(checkItem);
    		
    		if(enchantList.containsKey(effectId))
    		{
    			playerInv.setInventorySlotContents(i, null);
    			fEvent.setCanceled(true);
        		deadPlayer.heal(20);
        		break;
    		}
    	}
    }
}
