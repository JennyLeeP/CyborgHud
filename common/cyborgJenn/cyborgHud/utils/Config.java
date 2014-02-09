package cyborgJenn.cyborgHud.utils;

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
	public static Boolean enableCyborgBunnyLogo;
	public static int hudFontSize;
	public static int hudFontColor;
	public static int hudOverlayColor;
	
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
		hudFontSize = config.get(configHudOptions, "Select Font Size - Integer", 8).getInt();
		hudFontColor = config.get(configHudOptions, "Hud Font Color - (hex color codes) - ex 0xffffff", 0x0033FF).getInt();
		hudOverlayColor = config.get(configHudOptions, "Hud Overlay Color - (hex color codes) - ex 0xffffff", 0x0033FF).getInt();
		enableSelectedItem = config.get(configHudOptions, "Enable CyborgBunny Logo", true).getBoolean(true);
	}
	private static void configDebug(Configuration config){
        String debug = "Debug";
        enableDebug = config.get(debug, "enableDebug", false).getBoolean(false);
      
    }
}
