package com.cyborgJenn.cyborgHud.utils;

import java.io.File;
import java.util.logging.Level;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

public class Config {
	
	public static Boolean enableHud;
	public static Boolean enableDebug;
	public static Boolean enablePotionStatus;
	public static Boolean enableArmourStatus;
	public static Boolean enableSelectedItem;
	public static Boolean enableCompass;
	public static Boolean enableHorseStats;
	public static String hudFontColor1;
	public static String hudFontColor2;
	public static boolean showDamageBars;
	
	public static void init(FMLPreInitializationEvent event) {

        File configFile = new File(event.getModConfigurationDirectory(), Reference.NAME + ".cfg");

        Configuration config = new Configuration(configFile);

        try{
            config.load();

            configDebug(config);

            configHudOptions(config);

        }catch(Exception e){
            System.out.println(e);
        }finally{
            if (config.hasChanged())
                config.save();
        }
    }
    private static void configHudOptions(Configuration config) {
		String configHudOptions = "Hud Options";
		enableHud = config.get(configHudOptions, "Enable CyborgHud", true).getBoolean(true);
		enablePotionStatus = config.get(configHudOptions, "Enable Potion Status", true).getBoolean(true);
		enableArmourStatus = config.get(configHudOptions, "Enable Armour Status", true).getBoolean(true);
		enableSelectedItem = config.get(configHudOptions, "Enable Selected Item", true).getBoolean(true);
		enableCompass = config.get(configHudOptions, "Enable Compass", true).getBoolean(true);
		enableHorseStats = config.get(configHudOptions, "Enable Horse Stats", true).getBoolean(true);
		enableSelectedItem = config.get(configHudOptions, "Enable CyborgBunny Logo", true).getBoolean(true);
		showDamageBars = config.get(configHudOptions, "Show Damage Bars or Values - false = values", true).getBoolean(true);
	}
	private static void configDebug(Configuration config){
        String debug = "Debug";
        enableDebug = config.get(debug, "enableDebug", false).getBoolean(false);
      
    }
	private static void configFontColors(Configuration config){
        String hudFontColors = "HudFontColors - Valid Colors are WHITE, ORANGE, MAGENTA, LIME";
        hudFontColor1 = config.get(hudFontColors, "hudFontColor1", "PURPLE").getString();
        hudFontColor2 = config.get(hudFontColors, "hudFontColor2", "WHITE").getString();
    }
}
