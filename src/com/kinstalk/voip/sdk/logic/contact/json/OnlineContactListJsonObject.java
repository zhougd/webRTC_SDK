package com.kinstalk.voip.sdk.logic.contact.json;

import java.util.List;

import com.kinstalk.voip.sdk.http.AbstractJsonObject;

public class OnlineContactListJsonObject extends AbstractJsonObject
{
	private List<OnlineContactJsonObject> onLineUsers;
	private int status;

	public static class OnlineContactJsonObject
	{
		private long userId;
		private String userMobile;
		private long updateAt;
		private int status;
		private String domain;
		private List<Devices> devices;

		public static class Devices
		{
			private String instanceId;
			private String type;
			private String capability;
			private int status;

			public String getInstanceId()
			{
				return instanceId;
			}

			public void setInstanceId(String instanceId)
			{
				this.instanceId = instanceId;
			}

			public String getType()
			{
				return type;
			}

			public void setType(String type)
			{
				this.type = type;
			}

			public String getCapability()
			{
				return capability;
			}

			public void setCapability(String capability)
			{
				this.capability = capability;
			}

			public int getStatus()
			{
				return status;
			}

			public void setStatus(int status)
			{
				this.status = status;
			}
		}

		public long getUserId()
		{
			return userId;
		}

		public void setUserId(long userId)
		{
			this.userId = userId;
		}

		public String getUserMobile()
		{
			return userMobile;
		}

		public void setUserMobile(String userMobile)
		{
			this.userMobile = userMobile;
		}

		public long getUpdateAt()
		{
			return updateAt;
		}

		public void setUpdateAt(long updateAt)
		{
			this.updateAt = updateAt;
		}

		public int getStatus()
		{
			return status;
		}

		public void setStatus(int status)
		{
			this.status = status;
		}

		public String getDomain()
		{
			return domain;
		}

		public void setDomain(String domain)
		{
			this.domain = domain;
		}

		public List<Devices> getDevices()
		{
			return devices;
		}

		public void setDevices(List<Devices> devices)
		{
			this.devices = devices;
		}

	}

	public List<OnlineContactJsonObject> getOnLineUsers()
	{
		return onLineUsers;
	}

	public void setOnLineUsers(List<OnlineContactJsonObject> onLineUsers)
	{
		this.onLineUsers = onLineUsers;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

}
