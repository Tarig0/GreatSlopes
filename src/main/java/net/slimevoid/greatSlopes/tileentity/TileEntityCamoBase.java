package net.slimevoid.greatSlopes.tileentity;

import com.mojang.realmsclient.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.slimevoid.greatSlopes.block.BlockCamoSlope;

import javax.annotation.Nonnull;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Allen on 4/4/2015.
 *
 */
public class TileEntityCamoBase extends TileEntity {
    //Network Related
    private Queue<Pair<Integer,Object>> pendingUpdates = new ConcurrentLinkedQueue<>();
    //NBT Names
    private static final String ITEMTAGNAME = "Slot";
    private static final String ITEMTAGGROUPNAME = "Items";
    private static final String FACINGHORZNAME = "HozFacing";
    private static final String FACINGNAME = "Facing";
    private static final String ANCHORNAME = "Anchor";
    private static final String QUADNAME = "Quad";
    private static final String FACINGUPDATEARGNAME = "facingupdatearg";
    //State Info
    private ItemStack items[] = new ItemStack[6];
    private EnumFacing horzFacing = EnumFacing.NORTH;
    private EnumFacing facing = EnumFacing.NORTH;
    private EnumFacing anchor = EnumFacing.DOWN;
    private Integer quad = 0;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagCompound itemsTag = compound.getCompoundTag(ITEMTAGGROUPNAME);
        for (int i = 0; i < 6; i++) {
            items[i] = ItemStack.loadItemStackFromNBT(itemsTag.getCompoundTag(ITEMTAGNAME + i));
        }
        this.horzFacing = EnumFacing.getFront(compound.getInteger(FACINGHORZNAME));
        this.facing = EnumFacing.getFront(compound.getInteger(FACINGNAME));
        this.anchor = EnumFacing.values()[compound.getInteger(ANCHORNAME)];
        this.quad = compound.getInteger(QUADNAME);
    }
    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        return internalWrite(compound);
    }

    private NBTTagCompound internalWrite(NBTTagCompound compound) {
        NBTTagCompound itemTags = new NBTTagCompound();
        for (int i = 0; i < 6; i++) {
            if (items[i] != null) {
                NBTTagCompound tag = new NBTTagCompound();
                items[i].writeToNBT(tag);

                itemTags.setTag(ITEMTAGNAME + i, tag);
            }
        }
        compound.setTag(ITEMTAGGROUPNAME, itemTags);
        compound.setInteger(FACINGHORZNAME,this.getHorzFacing().ordinal());
        compound.setInteger(FACINGNAME,this.getFacing().ordinal());
        compound.setInteger(ANCHORNAME,this.getAnchor().ordinal());
        compound.setInteger(QUADNAME,this.getQuad());
        return compound;
    }
    @Override
    @Nonnull
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound compound = super.getUpdateTag();
        return internalWrite(compound);
    }
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound syncData = new NBTTagCompound();
        int meta = 0;
        if (this.pendingUpdates.size()>0) {
            meta = 1;
            NBTTagList commands = new NBTTagList();
            while (this.pendingUpdates.size() > 0) {
                Pair<Integer, Object> command = this.pendingUpdates.remove();
                NBTTagCompound commandTag = new NBTTagCompound();
                commandTag.setInteger("command",command.first());
                switch (command.first()) {
                    case 1:
                        int arg = (Integer) command.second();
                        commandTag.setInteger("Index", arg);
                        if (this.items[arg] != null) {
                            NBTTagCompound item = new NBTTagCompound();
                            this.items[arg].writeToNBT(item);
                            commandTag.setTag("Item", item);
                        }
                        break;
                    case 2:
                        int argFacing = (Integer) command.second();
                        commandTag.setInteger(FACINGUPDATEARGNAME, argFacing);
                        switch (argFacing) {
                            case 1:
                                commandTag.setInteger(FACINGHORZNAME, this.horzFacing.ordinal());
                                break;
                            case 2:
                                commandTag.setInteger(FACINGNAME, this.facing.ordinal());
                                break;
                        }
                        break;
                    case 3:
                        commandTag.setInteger(ANCHORNAME, (Integer) command.second());
                        break;
                    case 4:
                        commandTag.setInteger(QUADNAME, (Integer) command.second());
                        break;
                }
                commands.appendTag(commandTag);
            }
            syncData.setTag("Commands",commands);
        }else {
            this.writeToNBT(syncData);
        }
        return new SPacketUpdateTileEntity(this.getPos(), meta, syncData);
    }
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        switch (pkt.getTileEntityType()) {
            case 1:
                NBTTagList commands = pkt.getNbtCompound().getTagList("Commands",10);
                for(int i=0;i<commands.tagCount();i++){
                    NBTTagCompound command = commands.getCompoundTagAt(i);
                    switch(command.getInteger("command")) {
                        case 1:
                            this.UpdateSlot(command); //slot updates
                            break;
                        case 2:
                            this.UpdateFacings(command); //Facing updates
                            break;
                        case 3:
                            this.anchor = EnumFacing.values()[command.getInteger(ANCHORNAME)]; //Facing updates
                            break;
                        case 4:
                            this.quad = command.getInteger(QUADNAME);
                    }
                }
               break;
            default:
                this.readFromNBT(pkt.getNbtCompound()); //full sync startup
        }
        if(pkt.getTileEntityType() != 0) {
            this.getWorld().markBlockRangeForRenderUpdate(this.getPos(),this.getPos());
            this.getWorld().notifyNeighborsOfStateChange(this.getPos(), this.getBlockType());
        }
    }

    //Commands for TileEntity want to use the for switch but have to hard code integers for some reason
    private static final Integer SLOTUPDATECOMMAND = 1;
    private static final Integer FACINGUPDATECOMMAND = 2;
    private static final Integer ANCHORUPDATECOMMAND = 3;
    private static final Integer QUADUPDATECOMMAND = 4;
    private void addPendingCommand(int commandID, Object arg) {
        this.pendingUpdates.add(Pair.of(commandID,arg));
        this.markDirty();
        IBlockState state = this.getWorld().getBlockState(this.getPos());
        this.getWorld().notifyBlockUpdate(this.getPos(),state,state,4);
    }
    //Network Reading
    private void UpdateFacings(NBTTagCompound nbtCompound) {
        switch (nbtCompound.getInteger(FACINGUPDATEARGNAME)) {
            case 1:
                this.horzFacing = EnumFacing.getFront(nbtCompound.getInteger(FACINGHORZNAME));
            break;
            case 2:
                this.facing = EnumFacing.getFront(nbtCompound.getInteger(FACINGNAME));
            break;
        }
    }
    //Used by network on packet to update just the slot specified
    private void UpdateSlot(NBTTagCompound nbtCompound) {
        int beforeLight = this.getLightValue();
        if (nbtCompound.hasKey("Item")) {
            this.setItemForSide(nbtCompound.getInteger("Index"), ItemStack.loadItemStackFromNBT(nbtCompound.getCompoundTag("Item")));
        } else {
            this.setItemForSide(nbtCompound.getInteger("Index"), null);
        }
        int afterLight = this.getLightValue();
        if (afterLight != beforeLight){
            this.getWorld().checkLight(this.pos);
        }
    }
    //Network Writing Queue basically anything that calls addPendingCommand

    public void setHorzFacing(EnumFacing value){
        this.horzFacing = value;
        this.addPendingCommand(FACINGUPDATECOMMAND,1);
    }

    public void setFacing(EnumFacing value) {
        this.horzFacing = value;
        this.addPendingCommand(FACINGUPDATECOMMAND,2);
    }

    public boolean setFaceItemWithHeldItem(EnumFacing side, ItemStack heldItem) {

        if (heldItem != null && heldItem.getItem() instanceof ItemBlock) {
            Block b = ((ItemBlock) heldItem.getItem()).getBlock();
            AxisAlignedBB c =b.getDefaultState().getCollisionBoundingBox(this.getWorld(),this.getPos());
            if (!(b instanceof BlockCamoSlope) && c !=null &&
                    c.maxX  - c.minX == 1 &&
                    c.maxY  - c.minY == 1 &&
                    c.maxZ  - c.minZ == 1
                    ) {
                ItemStack is = heldItem.splitStack(1);
                if(!ItemStack.areItemStacksEqual(is,items[side.ordinal()])){
                    items[side.ordinal()] = is;
                    this.addPendingCommand(SLOTUPDATECOMMAND, side.ordinal());
                    return true;
                }
            }
        }
        return false;
    }

    public boolean ClearItemFromFace(EnumFacing side) {

        if (this.items[side.ordinal()] != null) {
            this.items[side.ordinal()]=null;
            this.addPendingCommand(SLOTUPDATECOMMAND, side.ordinal());

            return true;

        } else {

            return false;

        }

    }


    public void setAnchor(EnumFacing anchor){
        this.anchor = anchor;
        this.addPendingCommand(ANCHORUPDATECOMMAND,this.anchor.ordinal());
    }

    public void setQuad(Integer value) {
        quad = value;
        this.addPendingCommand(QUADUPDATECOMMAND,value);
    }

    //Remaining G/Setters
    public EnumFacing getHorzFacing() {
        if (this.horzFacing == EnumFacing.DOWN || this.horzFacing == EnumFacing.UP) return EnumFacing.EAST;
        else return this.horzFacing;
    }

    public EnumFacing getFacing() {
        return this.facing;
    }

    public EnumFacing getAnchor(){
        return this.anchor;
    }

    public IBlockState getStateForSide(EnumFacing i) {
        ItemStack is = getItemForSide(i.ordinal());
        if(is == null)
        return null;
        else
            //noinspection deprecation
            return ((ItemBlock)is.getItem()).getBlock().getStateFromMeta(is.getMetadata());
    }

    private ItemStack getItemForSide(int i) {
        return this.items[i];
    }

    private void setItemForSide(int i, ItemStack value) {
        this.items[i] = value;
    }
    //Utility Functions
    //Used to change the light value to the max, we could change this to an average of the six sides
    public int getLightValue() {
        int ret = 0;
        for (ItemStack i: this.items) {
            if(i!= null){
                Block b = ((ItemBlock)i.getItem()).getBlock();
                //noinspection deprecation can'SlopeFactory use position since that causes a loop
                int lightVal = b.getDefaultState().getLightValue();
                if (lightVal > ret)
                    ret = lightVal;
            }
        }
        return ret;
    }

    public boolean hasCamoData() {
        for (ItemStack i : this.items) {
            if (i != null) return true;
        }
        return false;
    }



    public Integer getQuad() {
        return quad;
    }

    public void setQuadtoHface() {
        this.setQuad(this.getHorzFacing().ordinal()-2);
    }
}