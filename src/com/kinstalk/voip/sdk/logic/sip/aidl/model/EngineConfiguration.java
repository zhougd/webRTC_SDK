package com.kinstalk.voip.sdk.logic.sip.aidl.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.kinstalk.voip.sdk.EngineSdkEngineConfiguration;
import com.kinstalk.voip.sdk.EngineSdkEngineWorkingMode;
import com.kinstalk.voip.sdk.EngineSdkMediaChannelSetupMode;
import com.kinstalk.voip.sdk.EngineSdkNetworkType;
import com.kinstalk.voip.sdk.EngineSdkOperatingSystemType;
import com.kinstalk.voip.sdk.EngineSdkServerType;

public class EngineConfiguration implements Parcelable
{
	private EngineSdkServerType serverConfType;
	private int maxTransmitResolutionWidth;
	private int maxTransmitResolutionHeight;
	private int maxReceiveResolutionWidth;
	private int maxReceiveResolutionHeight;
	private String deviceName = "";
	private String deviceId = "";
	private String userAgent = "";
	private String dnsServer = "";
	private CpuInfo cpuInfo = new CpuInfo();
	private EngineSdkOperatingSystemType os = EngineSdkOperatingSystemType.ES_OS_TYPE_ANDROID4;
	private EngineSdkNetworkType netType = EngineSdkNetworkType.ES_NETWORK_TYPE_UNKNOWN;
	private int isFileSysMounted;// 1:mounted 0:not mounted
	private int logLevel;
	private String logPathName = "";
	private String sipTokenHeader = "";
	private String configCachePathFrame = "";
	private EngineSdkMediaChannelSetupMode audioMediaMode = EngineSdkMediaChannelSetupMode.ES_MEDIA_CHANNEL_PRE_LOADED;
	private EngineSdkMediaChannelSetupMode mainVideoMediaMode = EngineSdkMediaChannelSetupMode.ES_MEDIA_CHANNEL_PRE_LOADED;
	private EngineSdkMediaChannelSetupMode secondVideoMediaMode = EngineSdkMediaChannelSetupMode.ES_MEDIA_CHANNEL_PRE_LOADED;
	private EngineSdkMediaChannelSetupMode dataMediaMode = EngineSdkMediaChannelSetupMode.ES_MEDIA_CHANNEL_PRE_LOADED;
	private EngineSdkEngineWorkingMode engineMode = EngineSdkEngineWorkingMode.ES_ENGINE_WORKING_MODE_NORMAL;
	private int engineAudioControlMode;
	private String glsbServerIp = "";
	private String relayServerIp = "";
	private String sipProxyServerIp = "";

	public EngineConfiguration()
	{

	}

	public EngineSdkServerType getServerConfType()
	{
		return serverConfType;
	}

	public void setServerConfType(EngineSdkServerType serverConfType)
	{
		this.serverConfType = serverConfType;
	}

	public int getMaxTransmitResolutionWidth()
	{
		return maxTransmitResolutionWidth;
	}

	public void setMaxTransmitResolutionWidth(int maxTransmitResolutionWidth)
	{
		this.maxTransmitResolutionWidth = maxTransmitResolutionWidth;
	}

	public int getMaxTransmitResolutionHeight()
	{
		return maxTransmitResolutionHeight;
	}

	public void setMaxTransmitResolutionHeight(int maxTransmitResolutionHeight)
	{
		this.maxTransmitResolutionHeight = maxTransmitResolutionHeight;
	}

	public int getMaxReceiveResolutionWidth()
	{
		return maxReceiveResolutionWidth;
	}

	public void setMaxReceiveResolutionWidth(int maxReceiveResolutionWidth)
	{
		this.maxReceiveResolutionWidth = maxReceiveResolutionWidth;
	}

	public int getMaxReceiveResolutionHeight()
	{
		return maxReceiveResolutionHeight;
	}

	public void setMaxReceiveResolutionHeight(int maxReceiveResolutionHeight)
	{
		this.maxReceiveResolutionHeight = maxReceiveResolutionHeight;
	}

	public String getDeviceName()
	{
		return deviceName;
	}

	public void setDeviceName(String deviceName)
	{
		this.deviceName = deviceName;
	}

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public String getUserAgent()
	{
		return userAgent;
	}

	public void setUserAgent(String userAgent)
	{
		this.userAgent = userAgent;
	}

	public String getDnsServer()
	{
		return dnsServer;
	}

	public void setDnsServer(String dnsServer)
	{
		this.dnsServer = dnsServer;
	}

	public CpuInfo getCpuInfo()
	{
		return cpuInfo;
	}

	public void setCpuInfo(CpuInfo cpuInfo)
	{
		this.cpuInfo = cpuInfo;
	}

	public EngineSdkOperatingSystemType getOs()
	{
		return os;
	}

	public void setOs(EngineSdkOperatingSystemType os)
	{
		this.os = os;
	}

	public EngineSdkNetworkType getNetType()
	{
		return netType;
	}

	public void setNetType(EngineSdkNetworkType netType)
	{
		this.netType = netType;
	}

	public int isFileSysMounted()
	{
		return isFileSysMounted;
	}

	public void setFileSysMounted(int isFileSysMounted)
	{
		this.isFileSysMounted = isFileSysMounted;
	}

	public int getLogLevel()
	{
		return logLevel;
	}

	public void setLogLevel(int logLevel)
	{
		this.logLevel = logLevel;
	}

	public String getLogPathName()
	{
		return logPathName;
	}

	public void setLogPathName(String logPathName)
	{
		this.logPathName = logPathName;
	}

	public String getSipTokenHeader()
	{
		return sipTokenHeader;
	}

	public void setSipTokenHeader(String sipTokenHeader)
	{
		this.sipTokenHeader = sipTokenHeader;
	}

	public String getConfigCachePathFrame()
	{
		return configCachePathFrame;
	}

	public void setConfigCachePathFrame(String configCachePathFrame)
	{
		this.configCachePathFrame = configCachePathFrame;
	}

	public EngineSdkMediaChannelSetupMode getAudioMediaMode()
	{
		return audioMediaMode;
	}

	public void setAudioMediaMode(EngineSdkMediaChannelSetupMode audioMediaMode)
	{
		this.audioMediaMode = audioMediaMode;
	}

	public EngineSdkMediaChannelSetupMode getMainVideoMediaMode()
	{
		return mainVideoMediaMode;
	}

	public void setMainVideoMediaMode(EngineSdkMediaChannelSetupMode mainVideoMediaMode)
	{
		this.mainVideoMediaMode = mainVideoMediaMode;
	}

	public EngineSdkMediaChannelSetupMode getSecondVideoMediaMode()
	{
		return secondVideoMediaMode;
	}

	public void setSecondVideoMediaMode(EngineSdkMediaChannelSetupMode secondVideoMediaMode)
	{
		this.secondVideoMediaMode = secondVideoMediaMode;
	}

	public EngineSdkMediaChannelSetupMode getDataMediaMode()
	{
		return dataMediaMode;
	}

	public void setDataMediaMode(EngineSdkMediaChannelSetupMode dataMediaMode)
	{
		this.dataMediaMode = dataMediaMode;
	}

	public EngineSdkEngineWorkingMode getEngineMode()
	{
		return engineMode;
	}

	public void setEngineMode(EngineSdkEngineWorkingMode engineMode)
	{
		this.engineMode = engineMode;
	}

	public EngineConfiguration(Parcel source)
	{
		int serverConfTypeValue = source.readInt();
		serverConfType = serverConfTypeValue == -1 ? null : EngineSdkServerType.swigToEnum(serverConfTypeValue);
		maxTransmitResolutionWidth = source.readInt();
		maxTransmitResolutionHeight = source.readInt();
		maxReceiveResolutionWidth = source.readInt();
		maxReceiveResolutionHeight = source.readInt();
		deviceName = source.readString();
		deviceId = source.readString();
		userAgent = source.readString();
		dnsServer = source.readString();
		cpuInfo = source.readParcelable(CpuInfo.class.getClassLoader());
		os = EngineSdkOperatingSystemType.swigToEnum(source.readInt());
		netType = EngineSdkNetworkType.swigToEnum(source.readInt());
		isFileSysMounted = source.readInt();
		logLevel = source.readInt();
		logPathName = source.readString();
		sipTokenHeader = source.readString();
		configCachePathFrame = source.readString();
		audioMediaMode = EngineSdkMediaChannelSetupMode.swigToEnum(source.readInt());
		mainVideoMediaMode = EngineSdkMediaChannelSetupMode.swigToEnum(source.readInt());
		secondVideoMediaMode = EngineSdkMediaChannelSetupMode.swigToEnum(source.readInt());
		dataMediaMode = EngineSdkMediaChannelSetupMode.swigToEnum(source.readInt());
		engineMode = EngineSdkEngineWorkingMode.swigToEnum(source.readInt());
		engineAudioControlMode = source.readInt();
		glsbServerIp = source.readString();
		relayServerIp = source.readString();
		sipProxyServerIp = source.readString();
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(serverConfType == null ? -1 : serverConfType.swigValue());
		dest.writeInt(maxTransmitResolutionWidth);
		dest.writeInt(maxTransmitResolutionHeight);
		dest.writeInt(maxReceiveResolutionWidth);
		dest.writeInt(maxReceiveResolutionHeight);
		dest.writeString(deviceName);
		dest.writeString(deviceId);
		dest.writeString(userAgent);
		dest.writeString(dnsServer);
		dest.writeParcelable(cpuInfo, 0);
		dest.writeInt(os.swigValue());
		dest.writeInt(netType.swigValue());
		dest.writeInt(isFileSysMounted);
		dest.writeInt(logLevel);
		dest.writeString(logPathName);
		dest.writeString(sipTokenHeader);
		dest.writeString(configCachePathFrame);
		dest.writeInt(audioMediaMode.swigValue());
		dest.writeInt(mainVideoMediaMode.swigValue());
		dest.writeInt(secondVideoMediaMode.swigValue());
		dest.writeInt(dataMediaMode.swigValue());
		dest.writeInt(engineMode.swigValue());
		dest.writeInt(engineAudioControlMode);

		dest.writeString(glsbServerIp);
		dest.writeString(relayServerIp);
		dest.writeString(sipProxyServerIp);
	}

	public static final Parcelable.Creator<EngineConfiguration> CREATOR = new Parcelable.Creator<EngineConfiguration>()
	{
		@Override
		public EngineConfiguration createFromParcel(Parcel source)
		{// 从Parcel中读取数据，返回person对象
			return new EngineConfiguration(source);
		}

		@Override
		public EngineConfiguration[] newArray(int size)
		{
			return new EngineConfiguration[size];
		}
	};

	public EngineSdkEngineConfiguration getEsEngineConfiguration()
	{
		EngineSdkEngineConfiguration result = new EngineSdkEngineConfiguration();
		result.setAudioMediaChannelMode(getAudioMediaMode());
		result.setGslbCachePathWithFileName(getConfigCachePathFrame());
		result.setDataMediaChannelMode(getDataMediaMode());
		result.setDeviceCPUInformation(cpuInfo.getEsCpuInfo());
		result.setDeviceCurrentNetworkType(getNetType());
		result.setDeviceIdentification(getDeviceId());
		result.setProductModel(getDeviceName());
		result.setDeviceOperatingSystemType(getOs());
		result.setDnsServerIPAdress(getDnsServer());
		result.setEngineWorkingMode(getEngineMode());
		result.setFileSystemMounted(true);
		result.setLogLevel(getLogLevel());
		result.setLogPathWithFileName(getLogPathName());
		result.setMainVideoMediaChannelMode(getMainVideoMediaMode());
		result.setMaximalReceiveVideoResolutionHeight(getMaxReceiveResolutionHeight());
		result.setMaximalReceiveVideoResolutionWidth(getMaxReceiveResolutionWidth());
		result.setMaximalTransmitVideoResolutionHeight(getMaxTransmitResolutionHeight());
		result.setMaximalTransmitVideoResolutionWidth(getMaxTransmitResolutionWidth());
		result.setSecondVideoMediaChannelMode(getSecondVideoMediaMode());
		if (getServerConfType() != null)
		{
			result.setServerType(getServerConfType());
		}
		result.setUserAgent(getUserAgent());
		result.setGslbIPFromServer(getGlsbServerIp());
		result.setRelayIPFromServer(getRelayServerIp());
		result.setEdgeProxyIPFromServer(getSipProxyServerIp());
		return result;

	}

	public int getEngineAudioControlMode()
	{
		return engineAudioControlMode;
	}

	public void setEngineAudioControlMode(int engineAudioControlMode)
	{
		this.engineAudioControlMode = engineAudioControlMode;
	}

	public int getIsFileSysMounted()
	{
		return isFileSysMounted;
	}

	public void setIsFileSysMounted(int isFileSysMounted)
	{
		this.isFileSysMounted = isFileSysMounted;
	}

	public String getGlsbServerIp()
	{
		return glsbServerIp;
	}

	public void setGlsbServerIp(String glsbServerIp)
	{
		this.glsbServerIp = glsbServerIp;
	}

	public String getRelayServerIp()
	{
		return relayServerIp;
	}

	public void setRelayServerIp(String relayServerIp)
	{
		this.relayServerIp = relayServerIp;
	}

	public String getSipProxyServerIp()
	{
		return sipProxyServerIp;
	}

	public void setSipProxyServerIp(String sipProxyServerIp)
	{
		this.sipProxyServerIp = sipProxyServerIp;
	}
}
