package net.slimevoid.greatSlopes.tileentity;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import cofh.api.energy.IEnergyStorage;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

/**
 * Created by alcoo on 10/25/2016.
 */
public class TileEntityJig extends TileEntity implements IEnergyReceiver {

    protected EnergyStorage energyStorage = new EnergyStorage(0);
    public ItemStack[] inventory = new ItemStack[0];



    protected boolean hasEnergy(int energy) {

        return energyStorage.getEnergyStored() >= energy;
    }


    public final void setEnergyStored(int quantity) {

        energyStorage.setEnergyStored(quantity);
    }

    /* GUI METHODS */
    public IEnergyStorage getEnergyStorage() {

        return energyStorage;
    }

    public int getScaledEnergyStored(int scale) {

        return (int)((long) energyStorage.getEnergyStored() * scale / energyStorage.getMaxEnergyStored());
    }

    /* NBT METHODS */
    @Override
    public void readFromNBT(NBTTagCompound nbt) {

        super.readFromNBT(nbt);

        energyStorage.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        nbt = super.writeToNBT(nbt);

        energyStorage.writeToNBT(nbt);
        return nbt;
    }


    /* IEnergyReceiver */
    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {

        return energyStorage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int getEnergyStored(EnumFacing from) {

        return energyStorage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(EnumFacing from) {

        return energyStorage.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {

        return energyStorage.getMaxEnergyStored() > 0;
    }

}