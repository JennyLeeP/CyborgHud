package com.cyborgJenn.cyborgHud.Render;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.opengl.GL11;

import com.cyborgJenn.cyborgHud.utils.Config;
import com.cyborgJenn.cyborgHud.utils.MCColorCodes;
import com.cyborgJenn.cyborgHud.utils.Reference;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHudGui extends GuiScreen{
	private static final ResourceLocation INVENTORYTEXTURE = new ResourceLocation("textures/gui/container/inventory.png");
	private static final ResourceLocation COMPASSTEXTURE = new ResourceLocation(Reference.TEXTURE + "/textures/gui/compass.png");

	private static RenderItem itemRenderer = new RenderItem();
	private static final int BUFF_ICON_SIZE = 18;
	private static final int BUFF_ICON_SPACING = BUFF_ICON_SIZE + 2; // 2 pixels between buff icons
	private static final int BUFF_ICON_BASE_U_OFFSET = 0;
	private static final int BUFF_ICON_BASE_V_OFFSET = 198;
	private static final int BUFF_ICONS_PER_ROW = 8;
	private static int xPosPotions = 75;
	private static int yPosPotions = 20;
	private static int xPosArmour = 2;
	private static int yPosArmour = 26;
	private static int armourIconSpacing = 2;
	private static int xPosCurrItem = 4;
	private static int yPosCurrItem = 162;
	private static int xPosScreen = 100;
	private static int yPosScreen = 2;
	private Minecraft mc;
	private int coordX = 0;
	private int coordY = 0;
	private int coordZ = 0;
	private int screenHeight;
	private int screenWidth;
	private ScaledResolution scaledResolution;
	private int scaledHeight;
	private int scaledWidth;
	private String COLOR1 = MCColorCodes.PURPLE;
	private String COLOR2 = MCColorCodes.WHITE;
	private String RESET = MCColorCodes.RESET;

	private EnumCreatureType creatures;


	@SideOnly(Side.CLIENT)
	public RenderHudGui(Minecraft mc) {
		super();
		//Needed to invoke the render engine.
		this.mc = mc;

	}

	@SubscribeEvent
	public void RenderGameOverlayEvent(RenderGameOverlayEvent event)
	{

		scaledResolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		screenHeight = scaledResolution.getScaledHeight();
		screenWidth = scaledResolution.getScaledWidth();

		if(event.isCancelable() || event.type != ElementType.EXPERIENCE)
		{      
			return;
		}

		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glScalef(1.0f, 1.0f, 0);
		renderInfoToScreen(mc);
		renderArmour(mc);
		renderSelectedItem(mc);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();


	}
	public void renderInfoToScreen(Minecraft mc){
		this.zLevel = 200;
		coordX = GetXCoordinate(mc);
		coordY = GetYCoordinate(mc);
		coordZ = GetZCoordinate(mc);
		float fullBlockLightValue = 0.0f;
		String biome ="";
		String stringComma = COLOR1 + ", " + COLOR2;
		String gameMode ="";
		String difficulty ="";
		int time = 0;
		long worldTime = 0;
		float lightBrightness = mc.theWorld.getLightBrightness(coordX, coordY, coordZ);
		int lightTest = mc.theWorld.getSavedLightValue(EnumSkyBlock.Sky, coordX, coordY, coordZ) - mc.theWorld.skylightSubtracted;
		float lightTest2 = mc.theWorld.getLightBrightnessForSkyBlocks(1, coordX, coordY, coordZ);

		int direction = (int)mc.thePlayer.rotationYaw;
		direction += 22;	//+22 centers the compass (45degrees/2)
		direction %= 360;

		if (direction < 0)
			direction += 360;
// TODO: Look at breaking up zones into 16 segments / 22.5
		int heading = direction / 45; //  360degrees divided by 45 == 8 zones
		String compassDirection = "";

		if (heading == 0)
			compassDirection = "S ";
		else if (heading == 1)
			compassDirection = "SW";
		else if (heading == 2)
			compassDirection = "W ";
		else if (heading == 3)
			compassDirection = "NW";
		else if (heading == 4)
			compassDirection = "N ";
		else if (heading == 5)
			compassDirection = "NE";
		else if (heading == 6)
			compassDirection = "E ";
		else
			compassDirection = "SE";

		if (!mc.theWorld.isRemote){// Code Runs If Connected to MP Server
			
			fullBlockLightValue = FMLCommonHandler.instance().getMinecraftServerInstance().getServer().getEntityWorld().getFullBlockLightValue(coordX, coordY, coordZ);
			biome = FMLCommonHandler.instance().getMinecraftServerInstance().getServer().getEntityWorld().getBiomeGenForCoords(coordX, coordZ).biomeName;
			gameMode = FMLCommonHandler.instance().getMinecraftServerInstance().getServer().getGameType().name();
			//difficulty = mc.getMinecraft().gameSettings.difficulty.name();
			worldTime = FMLCommonHandler.instance().getMinecraftServerInstance().getServer().getEntityWorld().getWorldTime();
			time = (int)worldTime;
			
		}else {// Code Runs if World is Single player
			
			fullBlockLightValue = mc.theWorld.getFullBlockLightValue(coordX, coordY, coordZ);
			biome = mc.theWorld.getBiomeGenForCoords(coordX, coordZ).biomeName;
			gameMode = mc.getMinecraft().getIntegratedServer().getGameType().name();
			difficulty = mc.getMinecraft().gameSettings.difficulty.name();
			worldTime = mc.theWorld.getWorldTime();
			time = (int)worldTime;
		}

		mc.fontRenderer.drawStringWithShadow(COLOR1 + "[ "+ COLOR2 + coordX + stringComma + coordY + stringComma + coordZ + COLOR1 + " ] " 
				+ " [ " + COLOR2 + biome + COLOR1 + " ]" + " [ " + COLOR2 + fullBlockLightValue + COLOR1 + " ]"+ " [ " + COLOR2 + compassDirection + COLOR1 + " ]" + "[World Time: " + COLOR2 + time  + COLOR1 + "]"+ RESET, xPosScreen, yPosScreen, 0xffffff);

		/*+ " [ Difficulty: " + WHITE + difficulty + PURPLE + "] " + "[GameMode: " + WHITE 
			+ gameMode + PURPLE + "] " + "[World Time: " + WHITE + worldTime  + PURPLE + "]"+*/
	}

	public void renderSelectedItem(Minecraft mc){
		this.zLevel = 200;

		ItemStack currentItem = mc.thePlayer.getCurrentEquippedItem();

		RenderHelper.enableStandardItemLighting();
		RenderHelper.enableGUIStandardItemLighting();
		itemRenderer.zLevel = 200.0F;
		itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), currentItem, xPosCurrItem, yPosCurrItem);
		itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.getTextureManager(), currentItem, xPosCurrItem, yPosCurrItem);

		RenderHelper.disableStandardItemLighting();

	}
	public int getDamageOrId(){
		int damnedID = 0;
		ItemStack currentItem = mc.thePlayer.getCurrentEquippedItem();
		if (currentItem.isItemStackDamageable()){
			return damnedID;//return damage value
		}else {
			return damnedID;//return block or item id
		}
	}

	public void renderPotionStatus(Minecraft mc){
		//GL11.glDisable(GL11.GL_LIGHTING); 
		this.zLevel = 200;
		Collection collection = this.mc.thePlayer.getActivePotionEffects();
		if (!collection.isEmpty())
		{
			this.mc.renderEngine.bindTexture(INVENTORYTEXTURE);

			for (Iterator iterator = this.mc.thePlayer.getActivePotionEffects()
					.iterator(); iterator.hasNext(); xPosPotions += BUFF_ICON_SPACING)
			{
				PotionEffect potioneffect = (PotionEffect) iterator.next();
				Potion potion = Potion.potionTypes[potioneffect.getPotionID()];

				if (potion.hasStatusIcon())
				{
					int iconIndex = potion.getStatusIconIndex();
					this.drawTexturedModalRect(xPosPotions, yPosPotions, 
							BUFF_ICON_BASE_U_OFFSET + iconIndex % BUFF_ICONS_PER_ROW * BUFF_ICON_SIZE, BUFF_ICON_BASE_V_OFFSET + iconIndex / BUFF_ICONS_PER_ROW * BUFF_ICON_SIZE,
							BUFF_ICON_SIZE, BUFF_ICON_SIZE);
				}       
			}
		}
	}
	public void renderArmour(Minecraft mc){

		GL11.glScalef(0.50f, 0.50f, 0);

		ItemStack helmet = mc.thePlayer.getCurrentArmor(3);
		ItemStack chest = mc.thePlayer.getCurrentArmor(2);
		ItemStack pants = mc.thePlayer.getCurrentArmor(1);
		ItemStack boots = mc.thePlayer.getCurrentArmor(0);
		int maxDamage = 1;
		int helmetDamage = 1;
		/*
		if (helmet != null){
			maxDamage = mc.thePlayer.getCurrentArmor(0).getMaxDamage() + 1;
			helmetDamage = maxDamage - mc.thePlayer.getCurrentArmor(0).getItemDamage();

		}
		 */

		//GL11.glEnable(32826 /* GL_RESCALE_NORMAL_EXT *//* GL_RESCALE_NORMAL_EXT */);
		//RenderHelper.enableStandardItemLighting();
		//RenderHelper.enableGUIStandardItemLighting();
		itemRenderer.zLevel = 200.0F;

		itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), helmet, xPosArmour, yPosArmour);
		itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), chest, xPosArmour, yPosArmour + 13);
		itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), pants, xPosArmour, yPosArmour + 26);
		itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), boots, xPosArmour, yPosArmour + 39);

		if (Config.showDamageBars){
			itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.getTextureManager(), helmet, xPosArmour, yPosArmour);
			itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.getTextureManager(), chest, xPosArmour, yPosArmour+ 13);
			itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.getTextureManager(), pants, xPosArmour, yPosArmour+ 26);
			itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.getTextureManager(), boots, xPosArmour, yPosArmour+ 39);
		}else {

			mc.fontRenderer.drawString(helmetDamage + "", xPosArmour, yPosArmour, 0xffffff);
		}
		//RenderHelper.disableStandardItemLighting();
		//GL11.glDisable(32826 /* GL_RESCALE_NORMAL_EXT *//* GL_RESCALE_NORMAL_EXT */);
		//GL11.glDisable(GL11.GL_BLEND);
		GL11.glScalef(1.0f, 1.0f, 1.0f);
	}

	public static int GetXCoordinate(Minecraft mc)
	{
		return (int) Math.floor(mc.thePlayer.posX);
	}
	public static int GetYCoordinate(Minecraft mc)
	{
		return (int) Math.floor(mc.thePlayer.posY);
	}
	public static int GetZCoordinate(Minecraft mc)
	{
		return (int) Math.floor(mc.thePlayer.posZ);
	}
}
