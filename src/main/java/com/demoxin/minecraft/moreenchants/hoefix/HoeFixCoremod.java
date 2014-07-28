package com.demoxin.minecraft.moreenchants.hoefix;

import java.util.Map;

import com.demoxin.minecraft.moreenchants.MoreEnchants;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class HoeFixCoremod implements IFMLLoadingPlugin
{

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[]{ HoeFixTransformer.class.getName() };
    }

    @Override
    public String getModContainerClass()
    {
        return null;
    }

    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data)
    {

    }

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }

}
