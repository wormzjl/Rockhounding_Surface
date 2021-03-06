package com.globbypotato.rockhounding_surface.machines.gui;

import com.globbypotato.rockhounding_surface.handler.Reference;
import com.globbypotato.rockhounding_surface.machines.container.ContainerWoodIncubator;
import com.globbypotato.rockhounding_surface.machines.tileentity.TileEntityWoodIncubator;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiWoodIncubator extends GuiBase {
	private final InventoryPlayer playerInventory;
	private final TileEntityWoodIncubator woodIncubator;
	public static final int WIDTH = 176;
	public static final int HEIGHT = 225;
	public static final ResourceLocation TEXTURE_REF =  new ResourceLocation(Reference.MODID + ":textures/gui/guiwoodincubator.png");
	private FluidTank inputTank;

	public GuiWoodIncubator(InventoryPlayer playerInv, TileEntityWoodIncubator tile){
		super(tile, new ContainerWoodIncubator(playerInv, tile));
		this.playerInventory = playerInv;
		TEXTURE = TEXTURE_REF;
		this.woodIncubator = tile;
		this.xSize = WIDTH;
		this.ySize = HEIGHT;
		this.inputTank = this.woodIncubator.inputTank;
		this.containerName = "container.woodIncubator";
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float f) {
		super.drawScreen(mouseX, mouseY, f);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;

		//fuel
		if(mouseX >= 10+x && mouseX <= 21+x && mouseY >= 27+y && mouseY <= 78+y){
			drawPowerInfo("ticks", this.woodIncubator.getCookTimeMax(), this.woodIncubator.getPower(), this.woodIncubator.getPowerMax(), mouseX, mouseY);
		}

		//fuel status
		String[] fuelstatusString = handleFuelStatus(this.woodIncubator.isFuelGated(), this.woodIncubator.hasFuelBlend(), this.woodIncubator.canInduct(), this.woodIncubator.allowPermanentInduction());
		if(mouseX >= 7+x && mouseX <= 24+x && mouseY >= 7+y && mouseY <= 24+y){
			drawMultiLabel(fuelstatusString, mouseX, mouseY);
		}

		//redstone
		if(!this.woodIncubator.hasFuelBlend()){
			if(mouseX >= 31+x && mouseX <= 42+x && mouseY >= 27+y && mouseY <= 78+y){
				drawEnergyInfo("RF", this.woodIncubator.getRedstone(), this.woodIncubator.getRedstoneMax(), mouseX, mouseY);
			}
		}

		//input tank
		if(mouseX>= 84+x && mouseX <= 104+x && mouseY >= 33+y && mouseY <= 99+y){
			drawTankInfo(this.inputTank, mouseX, mouseY);
		}

		//prev
		if(mouseX >= 137+x && mouseX <= 153+x && mouseY >= 122+y && mouseY <= 138+y){
			drawButtonLabel("Previous Recipe", mouseX, mouseY);
		}

		//next
		if(mouseX >= 154+x && mouseX <= 168+x && mouseY >= 122+y && mouseY <= 138+y){
			drawButtonLabel("Next Recipe", mouseX, mouseY);
		}

		//activation
		if(mouseX >= 7+x && mouseX <= 23+x && mouseY >= 122+y && mouseY <= 138+y){
			drawButtonLabel("Activation", mouseX, mouseY);
		}
	}

	 @Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		String recipeLabel = "No Recipe";
		if(this.woodIncubator.isValidInterval()){
			recipeLabel = this.woodIncubator.getRecipe().getOutput().getDisplayName();
		}
		this.fontRendererObj.drawString(recipeLabel, 26, 127, 4210752);
	}

	 @Override
	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

		//power bar
        if (this.woodIncubator.getPower() > 0){
            int k = this.getBarScaled(50, this.woodIncubator.getPower(), this.woodIncubator.getPowerMax());
            this.drawTexturedModalRect(i + 11, j + 28 + (50 - k), 176, 27, 10, k);
        }

		//redstone
		if(!this.woodIncubator.hasFuelBlend()){
			if (this.woodIncubator.getRedstone() > 0){
				int k = this.getBarScaled(50, this.woodIncubator.getRedstone(), this.woodIncubator.getRedstoneMax());
				this.drawTexturedModalRect(i + 32, j + 28 + (50 - k), 176, 81, 10, k);
			}
		}

		//smelt bar
		int k = this.getBarScaled(14, this.woodIncubator.cookTime, this.woodIncubator.getCookTimeMax());
		this.drawTexturedModalRect(i + 109, j + 57, 176, 0, 15, k); //dust

		//process icons
		if(this.woodIncubator.canSynthesize()){
			this.drawTexturedModalRect(i + 88, j + 102, 176, 131, 12, 14); //fire
			this.drawTexturedModalRect(i + 62, j + 61, 176, 145, 15, 9); //fluid drop
		}

		//induction icons
		if(this.woodIncubator.hasPermanentInduction()){
			this.drawTexturedModalRect(i + 7, j + 7, 176, 154, 18, 18); //inductor
		}

		//activation
        if(this.woodIncubator.isActive()){
            this.drawTexturedModalRect(i + 7, j + 122, 176, 172, 16, 16);
        }

		//blend fix
		if(this.woodIncubator.hasFuelBlend()){
			this.drawTexturedModalRect(i + 25, j + 7, 208, 0, 21, 92); //blend
		}

		//input fluid
		if(this.inputTank.getFluid() != null){
			renderFluidBar(this.inputTank.getFluid(), this.inputTank.getCapacity(), i + 84, j + 34, 20, 65);
		}
	}

}