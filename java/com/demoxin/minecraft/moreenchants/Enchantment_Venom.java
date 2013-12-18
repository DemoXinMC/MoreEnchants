package com.demoxin.minecraft.moreenchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentFireAspect;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class Enchantment_Venom extends Enchantment {
	public Enchantment_Venom(int fId, int fWeight)
	{
		super(fId, fWeight, EnumEnchantmentType.weapon);
		this.setName("venom");
		addToBookList(this);
	}
	
	@Override
	public int getMaxLevel()
    {
        return 2;
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
    	if(fTest instanceof Enchantment_Venom || fTest instanceof EnchantmentFireAspect)
    		return false;
    	return true;
    }
    
    public boolean canApply(ItemStack fTest)
    {
    	if(fTest.getItem() instanceof ItemSword || fTest.getItem() instanceof ItemBook)
    		return true;
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
    	if(attacker == null)
    		return;
    	
    	ItemStack dmgSource = ((EntityLivingBase)fEvent.source.getSourceOfDamage()).getHeldItem();
		if(dmgSource == null)
			return;
		
		if(EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantDefusing.effectId, dmgSource) <= 0)
			return;
		
		int levelVenom = EnchantmentHelper.getEnchantmentLevel(MoreEnchants.enchantVenom.effectId, dmgSource);
		((EntityLivingBase)fEvent.entity).addPotionEffect(new PotionEffect(Potion.poison.getId(), 60, levelVenom));
    }
}
