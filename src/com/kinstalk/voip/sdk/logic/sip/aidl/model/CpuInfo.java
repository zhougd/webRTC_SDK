package com.kinstalk.voip.sdk.logic.sip.aidl.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.kinstalk.voip.sdk.EngineSdkCPUInstruction;
import com.kinstalk.voip.sdk.EngineSdkCPUManufacturer;
import com.kinstalk.voip.sdk.EngineSdkCpuInformation;

public class CpuInfo implements Parcelable
{
	private int coreNum;
	private int frequencyMhz;
	private int mips;
	private EngineSdkCPUInstruction instruction;
	private EngineSdkCPUManufacturer manufacturer;

	public CpuInfo()
	{

	}

	public int getCoreNum()
	{
		return coreNum;
	}

	public void setCoreNum(int coreNum)
	{
		this.coreNum = coreNum;
	}

	public int getFrequencyMhz()
	{
		return frequencyMhz;
	}

	public void setFrequencyMhz(int frequencyMhz)
	{
		this.frequencyMhz = frequencyMhz;
	}

	public int getMips()
	{
		return mips;
	}

	public void setMips(int mips)
	{
		this.mips = mips;
	}

	public EngineSdkCPUInstruction getInstruction()
	{
		return instruction;
	}

	public void setInstruction(EngineSdkCPUInstruction instruction)
	{
		this.instruction = instruction;
	}

	public EngineSdkCPUManufacturer getManufacturer()
	{
		return manufacturer;
	}

	public void setManufacturer(EngineSdkCPUManufacturer manufacturer)
	{
		this.manufacturer = manufacturer;
	}

	public static Parcelable.Creator<CpuInfo> getCreator()
	{
		return CREATOR;
	}

	public CpuInfo(Parcel source)
	{
		coreNum = source.readInt();
		frequencyMhz = source.readInt();
		mips = source.readInt();
		int instructionValue = source.readInt();
		if (instructionValue == -1)
		{
			instruction = null;
		}
		else
		{
			instruction = EngineSdkCPUInstruction.swigToEnum(instructionValue);
		}

		int manufacturerValue = source.readInt();
		if (instructionValue == -1)
		{
			manufacturer = null;
		}
		else
		{
			manufacturer = EngineSdkCPUManufacturer.swigToEnum(manufacturerValue);
		}
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(coreNum);
		dest.writeInt(frequencyMhz);
		dest.writeInt(mips);
		dest.writeInt(instruction == null ? -1 : instruction.swigValue());
		dest.writeInt(manufacturer == null ? -1 : manufacturer.swigValue());
	}

	public static final Parcelable.Creator<CpuInfo> CREATOR = new Parcelable.Creator<CpuInfo>()
	{
		@Override
		public CpuInfo createFromParcel(Parcel source)
		{// 从Parcel中读取数据，返回person对象
			return new CpuInfo(source);
		}

		@Override
		public CpuInfo[] newArray(int size)
		{
			return new CpuInfo[size];
		}
	};

	public EngineSdkCpuInformation getEsCpuInfo()
	{
		EngineSdkCpuInformation result = new EngineSdkCpuInformation();
		result.setCpuCoreCount(getCoreNum());
		result.setCpuFrequencyInMhz(getFrequencyMhz());
		if (getInstruction() != null)
		{
			result.setInstruction(getInstruction());
		}
		if (getManufacturer() != null)
		{
			setManufacturer(getManufacturer());
		}
		result.setMips(getMips());

		return result;
	}
}
