package com.demoxin.minecraft.moreenchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class Enchantment_Quickdraw extends Enchantment
{
	public Enchantment_Quickdraw(int fId, int fWeight)
	{
		super(fId, fWeight, EnumEnchantmentType.bow);
		this.setName("quickdraw");
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
        return 20;
    }

    @Override
    public int getMaxEnchantability(int par1)
    {
        return 50;
    }
    
    @Override
	public boolean canApplyTogether(Enchantment fTest)
	{
		if(fTest == Enchantment.infinity || fTest == MoreEnchants.enchantQuickdraw || fTest == MoreEnchants.enchantExplosive || fTest == MoreEnchants.enchantScattershot)
			return false;
		if(fTest == MoreEnchants.enchantPoison || fTest == Enchantment.flame ||  fTest == MoreEnchants.enchantFrost)
			return false;
		return true;
	}
    
    public boolean canApply(ItemStack fTest)
    {
    	return Enchantment.infinity.canApply(fTest);
    }
    
    @SubscribeEvent
    public void HandleEnchant(ArrowNockEvent fEvent)
    {
    	ItemStack itemBow = fEvent.entityPlayer.getCurrentEquippedItem();
    	if(itemBow == null)
    		return;
    	
    	if(EnchantmentHelper.getEnchantmentLevel(this.effectId, itemBow) <= 0)
    		return;
    	
    	if (!fEvent.entityPlayer.capabilities.isCreativeMode && !fEvent.entityPlayer.inventory.hasItem(Items.arrow))
    		return;
    	
        fEvent.entityPlayer.setItemInUse(itemBow, itemBow.getMaxItemUseDuration()/3);
        fEvent.result = itemBow;
        fEvent.setCanceled(true);
    }
}
