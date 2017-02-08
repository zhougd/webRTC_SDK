package com.kinstalk.voip.sdk.logic.conversation.json;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.kinstalk.voip.sdk.data.DataService;
import com.kinstalk.voip.sdk.data.model.AbstractDataItem;
import com.kinstalk.voip.sdk.logic.contact.json.ContactListJsonObject;
import com.kinstalk.voip.sdk.http.AbstractJsonObject;

public class ConversationHistoryListJsonObject extends AbstractJsonObject{
	@DatabaseField(id = true, unique = true)
	private String mClassURI;
	@DatabaseField
	private int total;
	@DatabaseField
	private long serverTime;
	
	private List<ConversationHistoryJsonObject> historys;
	@ForeignCollectionField(eager = true)
	private ForeignCollection<ConversationHistoryJsonObject> historysCollection;
	
	//private List<String> unReadmap;
	@DatabaseField
	private int status;
	
	public ConversationHistoryListJsonObject() {
		mClassURI= getUri().toString();
		super.setmTobePersisted();
	}

	private void connectToSubObjects() {
		for (int i = 0; i < historys.size(); i++) {
			historys.get(i).SetConversationHistoryListJsonObject(this);
		}
	}
	
	@Override
	public void putToDB() {
		if (super.mIsPersisted == true) {
			if (historys != null && historys.size() > 0) {
				connectToSubObjects();
				DataService.getInstance().putDataItems(historys);
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
		historys = new ArrayList<ConversationHistoryJsonObject>(historysCollection);
	}
	
	
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public long getServerTime() {
		return serverTime;
	}
	public void setServerTime(long serverTime) {
		this.serverTime = serverTime;
	}
	public List<ConversationHistoryJsonObject> getHistorys() {
		return historys;
	}
	public void setHistorys(List<ConversationHistoryJsonObject> historys) {
		this.historys = historys;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
