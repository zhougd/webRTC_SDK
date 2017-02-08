package com.kinstalk.voip.sdk.logic.contact.json;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.kinstalk.voip.sdk.data.DataService;
import com.kinstalk.voip.sdk.data.model.AbstractDataItem;
import com.kinstalk.voip.sdk.http.AbstractJsonObject;

public class ContactListJsonObject extends AbstractJsonObject
{
	@DatabaseField
	private String mClassURI;
	@DatabaseField
	private int total;
	@DatabaseField
	private long updateAt;
	@DatabaseField
	private long userUpdateAt;
	@DatabaseField
	private long listUpdateAt;
	@DatabaseField
	private long serverTime;

	private List<ContactJsonObject> contacts;
	@ForeignCollectionField(eager = true)
	private ForeignCollection<ContactJsonObject> contactsCollection;
	
	@DatabaseField
	private int status;
	
	@DatabaseField
	private String mToken;
	@DatabaseField(id = true, unique = true)
	private String mUserId;
	
	public String getmToken() {
		return mToken;
	}

	public void setmToken(String mToken) {
		this.mToken = mToken;
	}
	
	public String getmUserId() {
		return mUserId;
	}
	
	public void setmUserId(String userId) {
		this.mUserId = userId;
	}

	public ContactListJsonObject() {
		mClassURI= getUri().toString();
		super.setmTobePersisted();
	}

	private void connectToSubObjects() {
		for (int i = 0; i < contacts.size(); i++) {
		    contacts.get(i).SetContactListJsonObject(this);
		}
	}
	
	@Override
	public <T extends AbstractDataItem> void postProcess(T objectForPostProcess) {
		ContactListJsonObject o = (ContactListJsonObject)objectForPostProcess;
		setmToken(o.getmToken());
		mUserId = o.getmUserId();
		if (o.getUserUpdateAt() < this.getUserUpdateAt() || o.getListUpdateAt() < this.getListUpdateAt() || o.getUpdateAt() < this.getUpdateAt()) {
			this.setmShouldPersist(true);
		} else {
			this.setmShouldPersist(false);
		}
	}
	
	@Override
	public void putToDB() {
		if (super.mIsPersisted == true) {
			if (super.mShouldPersist == true) {
				if (contacts != null && contacts.size() > 0) {
					connectToSubObjects();
					DataService.getInstance().putDataItems(contacts);
				}
			}
			super.putToDB();
		}
	}
	
	@Override
	public <T extends AbstractDataItem> T updateFromDB() {
		T newObject = super.updateFromDB();
		if (newObject != null) {
			newObject.attachAllForeignCollection();
		}
		return newObject;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ContactListJsonObject> queryForEq(String arg0, Object arg1) {
		List<ContactListJsonObject> list = super.queryForEq(arg0, arg1);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
			    list.get(i).attachAllForeignCollection();
			}
		}
		return list;
	}
		
	@Override
	public void attachAllForeignCollection() {
		contacts = new ArrayList<ContactJsonObject>(contactsCollection);
	}
	
	public String getClassID() {
		return mClassURI;
	}

	public int getTotal()
	{
		return total;
	}

	public void setTotal(int total)
	{
		this.total = total;
	}

	public long getUpdateAt()
	{
		return updateAt;
	}

	public void setUpdateAt(long updateAt)
	{
		this.updateAt = updateAt;
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

	public long getServerTime()
	{
		return serverTime;
	}

	public void setServerTime(long serverTime)
	{
		this.serverTime = serverTime;
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
