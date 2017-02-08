package com.kinstalk.voip.sdk.logic.contact.json;

import java.util.List;

import com.kinstalk.voip.sdk.http.AbstractJsonObject;

public class FriendAddJsonObject extends AbstractJsonObject
{
	private long updateAt;
	private long frequentListUpdateAt;
	private long userUpdateAt;
	private long listUpdateAt;
	private long commonUpdateAt;
	private long frequentUpdateAt;
	private long frequentUserUpdateAt;
	private long commonUserUpdateAt;
	private long commonListUpdateAt;
	private List<Long> ids;
	private List<ContactJsonObject> contacts;
	private int status;

	public long getUpdateAt()
	{
		return updateAt;
	}

	public void setUpdateAt(long updateAt)
	{
		this.updateAt = updateAt;
	}

	public long getFrequentListUpdateAt()
	{
		return frequentListUpdateAt;
	}

	public void setFrequentListUpdateAt(long frequentListUpdateAt)
	{
		this.frequentListUpdateAt = frequentListUpdateAt;
	}

	public long getUserUpdateAt()
	{
		return userUpdateAt;
	}

	public void setUserUpdateAt(long userUpdateAt)
	{
		this.userUpdateAt = userUpdateAt;
	}

	public long getListUpdateAt()
	{
		return listUpdateAt;
	}

	public void setListUpdateAt(long listUpdateAt)
	{
		this.listUpdateAt = listUpdateAt;
	}

	public long getCommonUpdateAt()
	{
		return commonUpdateAt;
	}

	public void setCommonUpdateAt(long commonUpdateAt)
	{
		this.commonUpdateAt = commonUpdateAt;
	}

	public long getFrequentUpdateAt()
	{
		return frequentUpdateAt;
	}

	public void setFrequentUpdateAt(long frequentUpdateAt)
	{
		this.frequentUpdateAt = frequentUpdateAt;
	}

	public long getFrequentUserUpdateAt()
	{
		return frequentUserUpdateAt;
	}

	public void setFrequentUserUpdateAt(long frequentUserUpdateAt)
	{
		this.frequentUserUpdateAt = frequentUserUpdateAt;
	}

	public long getCommonUserUpdateAt()
	{
		return commonUserUpdateAt;
	}

	public void setCommonUserUpdateAt(long commonUserUpdateAt)
	{
		this.commonUserUpdateAt = commonUserUpdateAt;
	}

	public long getCommonListUpdateAt()
	{
		return commonListUpdateAt;
	}

	public void setCommonListUpdateAt(long commonListUpdateAt)
	{
		this.commonListUpdateAt = commonListUpdateAt;
	}

	public List<Long> getIds()
	{
		return ids;
	}

	public void setIds(List<Long> ids)
	{
		this.ids = ids;
	}

	public List<ContactJsonObject> getContacts()
	{
		return contacts;
	}

	public void setContacts(List<ContactJsonObject> contacts)
	{
		this.contacts = contacts;
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
