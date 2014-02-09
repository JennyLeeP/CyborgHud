package cyborgJenn.cyborgHud.Render;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cyborgJenn.cyborgHud.utils.Config;
import cyborgJenn.cyborgHud.utils.MCColorCodes;
import cyborgJenn.cyborgHud.utils.Reference;

@SideOnly(Side.CLIENT)
public class RenderHudGui extends Gui{
	private static final ResourceLocation INVENTORYTEXTURE = new ResourceLocation("textures/gui/container/inventory.png");
	private static final ResourceLocation GUIBASETEXTURE = new ResourceLocation(Reference.TEXTURE + "/textures/gui/guibase2.png");
	private static final ResourceLocation GUIBASEDARKTEXTURE = new ResourceLocation(Reference.TEXTURE + "/textures/gui/guibasedark.png");
	private static final ResourceLocation GUIBASEJENNYTEXTURE = new ResourceLocation(Reference.TEXTURE + "/textures/gui/guibasejenny.png");
	private static final ResourceLocation COMPASSTEXTURE = new ResourceLocation(Reference.TEXTURE + "/textures/gui/compass.png");
	private static final ResourceLocation LOGOTEXTURE = new ResourceLocation(Reference.TEXTURE + "/textures/gui/cyborgbunny32.png");

	private static RenderItem itemRenderer = new RenderItem();
	private static final int BUFF_ICON_SIZE = 18;
	private static final int BUFF_ICON_SPACING = BUFF_ICON_SIZE + 2; // 2 pixels between buff icons
	private static final int BUFF_ICON_BASE_U_OFFSET = 0;
	private static final int BUFF_ICON_BASE_V_OFFSET = 198;
	private static final int BUFF_ICONS_PER_ROW = 8;
	private static int compassIndex = 0;
	private static int xPosCompass = 178;
	private static int yPosCompass = 3;
	private static int xPosPotions = 45;
	private static int yPosPotions = 2;
	private static int xPosBaseGui = 0;
	private static int yPosBaseGui = 0;
	private static int xPosLogo = 3;
	private static int yPosLogo = 4;
	private static int xPosInfoPanel = 4;
	private static int yPosInfoPanel = 148;
	private static int xPosArmour = 2;
	private static int yPosArmour = 18;
	private static int armourIconSpacing = 2;
	private static int xPosCurrItem = 4;
	private static int yPosCurrItem = 162;
	private static int xPosScreen = 200;
	private static int yPosScreen = 2;

	private Minecraft mc;
	private int coordX = 0;
	private int coordY = 0;
	private int coordZ = 0;
	private int posX;
	private int posY;
	private int posZ;
	private EnumCreatureType creatures;
	
	@SideOnly(Side.CLIENT)
	public RenderHudGui(Minecraft mc) {
		super();
		//Needed to invoke the render engine.
		this.mc = mc;
		
	}


	@SubscribeEvent
	public void onRenderExperienceBar(RenderGameOverlayEvent event)
	{
		//System.out.println("Has Fired Render Experience Bar");// debug code

		// We draw after the ExperienceBar has drawn.  The event raised by GuiIngameForge.pre()
		// will return true from isCancelable.  If you call event.setCanceled(true) in
		// that case, the portion of rendering which this event represents will be canceled.
		// We want to draw *after* the experience bar is drawn, so we make sure isCancelable() returns
		// false and that the eventType represents the ExperienceBar event.

		if(event.isCancelable() || event.type != ElementType.EXPERIENCE)
		{      
			return;
		}

		/*
		 * CyborgHud BaseGui
		 */

		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glScalef(0.50f, 0.50f, 0);
		renderBaseGui(mc);
		GL11.glPopMatrix();

		/*
		 * CyborgHud Logo
		 */

		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glScalef(0.45f, 0.55f, 0);
		renderLogo();
		GL11.glPopMatrix();

		/*
		 * Potion Status Render
		 */

		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glScalef(0.50f, 0.50f, 0);
		renderPotionStatus(mc);
		GL11.glPopMatrix();

		/*
		 * Render Compass
		 */

		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glScalef(0.50f, 0.50f, 0);
		renderCompass(mc);
		GL11.glPopMatrix();

		/*
		 * Info Panel Render
		 */

		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glScalef(0.50f, 0.50f, 0);
		renderInfoPanelToHud();
		GL11.glPopMatrix();

		/*
		 * Render Current Selected Item
		 */

		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glScalef(0.65f, 0.65f, 0);
		renderSelectedItem(mc);
		GL11.glPopMatrix();

		/*
		 * Render Info To *Screen
		 */

		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glScalef(0.65f, 0.65f, 0);
		renderInfoToScreen();
		GL11.glPopMatrix();

		/*
		 * Render Armour - My code
		 */
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glScalef(0.8f, 0.8f, 0);
		renderArmour();
		GL11.glPopMatrix();

	}
	public void renderBaseGui(Minecraft mc){
		this.zLevel = 100;

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		mc.getTextureManager().bindTexture(GUIBASEDARKTEXTURE);
		this.drawTexturedModalRect(xPosBaseGui, yPosBaseGui, 0, 0, 250, 240);

	}

	public void renderLogo(){

		mc.fontRenderer.drawStringWithShadow(MCColorCodes.BOLD + "CyborgHud" + MCColorCodes.RESET, xPosLogo, yPosLogo, Config.hudFontColor);
		//mc.getTextureManager().bindTexture(LOGOTEXTURE);
		//this.drawTexturedModalRect(xPosLogo, yPosLogo, 0, 0, 32, 48);
	}

	public void renderSelectedItem(Minecraft mc){

		ItemStack currentItem = mc.thePlayer.getCurrentEquippedItem();

		GL11.glEnable(32826 /* GL_RESCALE_NORMAL_EXT *//* GL_RESCALE_NORMAL_EXT */);
		RenderHelper.enableStandardItemLighting();
		RenderHelper.enableGUIStandardItemLighting();
		itemRenderer.zLevel = 200.0F;

		itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), currentItem, xPosCurrItem, yPosCurrItem);
		itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.getTextureManager(), currentItem, xPosCurrItem, yPosCurrItem);

		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(32826 /* GL_RESCALE_NORMAL_EXT *//* GL_RESCALE_NORMAL_EXT */);
		GL11.glDisable(GL11.GL_BLEND);

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

	public void renderCompass(Minecraft mc){
		coordX = GetXCoordinate(mc);
		coordY = GetYCoordinate(mc);
		coordZ = GetZCoordinate(mc);

		int direction = MathHelper.floor_double(((mc.thePlayer.rotationYaw * 256F) / 360F) + 0.5D) & 255;
		mc.getTextureManager().bindTexture(COMPASSTEXTURE);
		if (direction < 128)
			this.drawTexturedModalRect(xPosCompass, yPosCompass, direction, (yPosPotions * 24), 65, 13);
		else
			this.drawTexturedModalRect(xPosCompass, yPosCompass, direction - 128, (yPosPotions * 24) + 12, 65, 13);

		mc.fontRenderer.drawString(MCColorCodes.DARK_AQUA + "|", xPosCompass + 32, yPosCompass + 1, 0xffffff);
		mc.fontRenderer.drawString(MCColorCodes.DARK_AQUA + "|" + MCColorCodes.RESET, xPosCompass + 32, yPosCompass + 5, 0xffffff);
	}

	public void renderPotionStatus(Minecraft mc){
		//GL11.glDisable(GL11.GL_LIGHTING);  
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
	public void renderArmour(){
		ItemStack helmet = mc.thePlayer.getCurrentArmor(3);
		ItemStack chest = mc.thePlayer.getCurrentArmor(2);
		ItemStack pants = mc.thePlayer.getCurrentArmor(1);
		ItemStack boots = mc.thePlayer.getCurrentArmor(0);

		GL11.glEnable(32826 /* GL_RESCALE_NORMAL_EXT *//* GL_RESCALE_NORMAL_EXT */);
		RenderHelper.enableStandardItemLighting();
		RenderHelper.enableGUIStandardItemLighting();
		itemRenderer.zLevel = 200.0F;

		itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), helmet, xPosArmour, yPosArmour);
		itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.getTextureManager(), helmet, xPosArmour, yPosArmour);

		itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), chest, xPosArmour, yPosArmour + 17);
		itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.getTextureManager(), chest, xPosArmour, yPosArmour+ 17);

		itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), pants, xPosArmour, yPosArmour + 35);
		itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.getTextureManager(), pants, xPosArmour, yPosArmour+ 35);

		itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), boots, xPosArmour, yPosArmour + 54);
		itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.getTextureManager(), boots, xPosArmour, yPosArmour+ 54);

		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(32826 /* GL_RESCALE_NORMAL_EXT *//* GL_RESCALE_NORMAL_EXT */);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void renderInfoPanelToHud(){
		String serverPlayers = "";
		if (mc.theWorld.isRemote){
			//serverPlayers = mc.getMinecraft().func_147104_D().populationInfo;
		}

		float blockLL = mc.theWorld.getBlockLightValue(coordX, coordY, coordZ);
		mc.fontRenderer.drawStringWithShadow(MCColorCodes.BOLD + "X: " + coordX, xPosInfoPanel, yPosInfoPanel, Config.hudFontColor);
		mc.fontRenderer.drawStringWithShadow(MCColorCodes.BOLD + "Z: " + coordZ, xPosInfoPanel, yPosInfoPanel + 9, Config.hudFontColor);
		mc.fontRenderer.drawStringWithShadow(MCColorCodes.BOLD + "Y: " + coordY + MCColorCodes.RESET, xPosInfoPanel, yPosInfoPanel + 18, Config.hudFontColor);
		mc.fontRenderer.drawStringWithShadow(MCColorCodes.BOLD + "L: " + blockLL+ MCColorCodes.RESET, xPosInfoPanel, yPosInfoPanel + 27, Config.hudFontColor);

	}
	public void renderInfoToScreen(){
		String gameMode ="";
		String difficulty ="";
		String biome ="";
		long worldTime = 0;
		EntityPlayer closestPlayer = null;

		if (mc.getMinecraft().isSingleplayer()){
			gameMode = mc.getMinecraft().getIntegratedServer().getGameType().name();
			difficulty = mc.getMinecraft().gameSettings.difficulty.name();
			closestPlayer = mc.theWorld.getClosestPlayer(coordX, coordY, coordZ, 0);
			biome = mc.theWorld.getBiomeGenForCoords(coordX, coordZ).biomeName;
			worldTime = mc.theWorld.getTotalWorldTime();

		}else {
			//gameMode = mc.getMinecraft().func_147104_D().getIntegratedServer().getGameType().name();
		}
		mc.fontRenderer.drawStringWithShadow("[Biome: " + MCColorCodes.WHITE + biome + MCColorCodes.RESET + "]" + "[Nearby Player: " +  MCColorCodes.WHITE + closestPlayer + MCColorCodes.RESET +"]"+"[World Time: " + MCColorCodes.WHITE + worldTime + MCColorCodes.RESET + "]", xPosScreen, yPosScreen, Config.hudFontColor);
		mc.fontRenderer.drawStringWithShadow("[Difficulty: " + MCColorCodes.WHITE + difficulty + MCColorCodes.RESET + "]" + "[GameMode: " + MCColorCodes.WHITE + gameMode + MCColorCodes.RESET + "]", xPosScreen, yPosScreen + 9, Config.hudFontColor);
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
