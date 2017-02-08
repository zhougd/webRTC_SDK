package com.kinstalk.voip.sdk.logic.config.json;

import java.util.Map;

import com.kinstalk.voip.sdk.http.AbstractJsonObject;

public class ConfigJsonObject extends AbstractJsonObject
{
	private Map<String, ConfigItem> config;

	public static final class ConfigItem
	{
		private String value;
		private String version;
		private String platform;
		private String newVersion;

		public String getValue()
		{
			return value;
		}

		public void setValue(String value)
		{
			this.value = value;
		}

		public String getVersion()
		{
			return version;
		}

		public void setVersion(String version)
		{
			this.version = version;
		}

		public String getPlatform()
		{
			return platform;
		}

		public void setPlatform(String platform)
		{
			this.platform = platform;
		}

		public String getNewVersion()
		{
			return newVersion;
		}

		public void setNewVersion(String newVersion)
		{
			this.newVersion = newVersion;
		}

	}

	public Map<String, ConfigItem> getConfig()
	{
		return config;
	}

	public void setConfig(Map<String, ConfigItem> config)
	{
		this.config = config;
	}
}
