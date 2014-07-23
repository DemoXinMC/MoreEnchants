package com.demoxin.minecraft.moreenchants.armor;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentKnowledge extends Enchantment {
	protected EnchantmentKnowledge(int fId, int fWeight) {
		super(fId, fWeight, EnumEnchantmentType.armor_head);
		this.setName("knowledge");
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
    		if(itemArmor.armorType == 0)
    		{
    			return true;
    		}
    	}
        return false;
    }
    
    @SubscribeEvent
    public void HandleEnchant(PlayerPickupXpEvent fEvent)
    {
		if(fEvent.entityPlayer == null)
			return;
				
		ItemStack armorHelm = fEvent.entityPlayer.getEquipmentInSlot(4);
		if(armorHelm == null)
			return;
				
		if(EnchantmentHelper.getEnchantmentLevel(effectId, armorHelm) <= 0)
			return;
		
		int newXP = (int)Math.ceil(fEvent.orb.xpValue * 1.5D);
		fEvent.orb.xpValue = newXP;
    }
}
