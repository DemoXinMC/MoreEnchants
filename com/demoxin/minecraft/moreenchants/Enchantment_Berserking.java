package com.demoxin.minecraft.moreenchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class Enchantment_Berserking extends Enchantment {
	public Enchantment_Berserking(int fId, int fWeight)
	{
		super(fId, fWeight, EnumEnchantmentType.armor_torso);
		this.setName("berserk");
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
    	if(fTest instanceof Enchantment_Berserking)
    		return false;
    	return true;
    }
    
    public boolean canApply(ItemStack fTest)
    {
    	if(fTest.getItem() instanceof ItemArmor)
    	{
    		ItemArmor itemArmor = (ItemArmor)fTest.getItem();
    		if(itemArmor.armorType == 1)
    		{
    			return true;
    		}
    	}
        return false;
    }
    
    @ForgeSubscribe
    public void HandleEnchant(LivingHurtEvent fEvent)
    {
		if(fEvent.source.damageType != "player" && fEvent.source.damageType != "mob")
			return;
		
		if(!(fEvent.source.getSourceOfDamage() instanceof EntityLivingBase))
			return;
		
		EntityLivingBase attacker = (EntityLivingBase)fEvent.source.getSourceOfDamage();
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
}
