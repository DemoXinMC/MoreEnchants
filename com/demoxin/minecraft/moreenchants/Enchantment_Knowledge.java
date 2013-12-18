package com.demoxin.minecraft.moreenchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;

public class Enchantment_Knowledge extends Enchantment {
	protected Enchantment_Knowledge(int fId, int fWeight) {
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
    	if(fTest instanceof Enchantment_Knowledge)
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
    
    @ForgeSubscribe
    public void HandleEnchant(PlaySoundAtEntityEvent fEvent)
    {
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
				
		if(EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantKnowledge.effectId, armorHelm) <= 0)
			return;

		player.addExperience(((EntityXPOrb)fEvent.entity).getXpValue());
    }
}
