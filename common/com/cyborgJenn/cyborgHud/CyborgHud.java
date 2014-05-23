package com.cyborgJenn.cyborgHud;
import java.util.logging.Logger;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import com.cyborgJenn.cyborgHud.Render.RenderHudGui;
import com.cyborgJenn.cyborgHud.proxy.CommonProxy;
import com.cyborgJenn.cyborgHud.utils.Config;
import com.cyborgJenn.cyborgHud.utils.Reference;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class CyborgHud {

	@SidedProxy(clientSide = Reference.CLIENTPROXY, serverSide = Reference.SERVERPROXY)
    public static CommonProxy proxy;
	
	//protected static final Minecraft mc = Minecraft.getMinecraft();
	
	public static Logger logger;
	@EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
		// Loads the Configuration File
        Config.init(event);
		System.out.println("[CyborgHud] " + "Pre Init Complete..........");
		
    }
	@EventHandler
    public void Init(FMLInitializationEvent event)
    {
		System.out.println("[CyborgHud] " + "Init Complete.............");
    }
	@SideOnly(Side.CLIENT)
	@EventHandler
	  public void postInit(FMLPostInitializationEvent event)
	  {
		
	    MinecraftForge.EVENT_BUS.register(new RenderHudGui(Minecraft.getMinecraft()));
	    System.out.println("[CyborgHud] " + "Post Init Complete.............");
	  }
	
}
