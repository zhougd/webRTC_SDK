package com.kinstalk.voip.sdk.logic.conversation.json;

import com.j256.ormlite.field.DatabaseField;
import com.kinstalk.voip.sdk.http.AbstractJsonObject;

public class ConversationHistoryJsonObject extends AbstractJsonObject{
	@DatabaseField
	private long userUpdateAt;
	@DatabaseField
	private long id;
	@DatabaseField
	private long userId;
	@DatabaseField(id = true, unique = true)
	private long friendId;
	@DatabaseField
	private String friendMobile;
	@DatabaseField
	private String friendName;
	@DatabaseField
	private String friendAliasName;
	@DatabaseField
	private String friendPic;
	@DatabaseField
	private int gender;
	@DatabaseField
	private int type;
	@DatabaseField
	private String content;
	@DatabaseField
	private long relationId;
	@DatabaseField
	private long createAt;
	//private int msgType;
	@DatabaseField
	private String msgUnread;
	@DatabaseField
	private String tid;
	@DatabaseField
	private String pic;
	@DatabaseField
	private String fsize;
	@DatabaseField
	private String ratio;
	@DatabaseField
	private String spec;
	@DatabaseField
	private int reply;
	@DatabaseField(foreign=true, foreignAutoCreate=false, foreignAutoRefresh=false)
	private ConversationHistoryListJsonObject mConversationHistoryListJsonObject;
	
	public ConversationHistoryJsonObject() {
		super.setmTobePersisted();
	}
	
	public void SetConversationHistoryListJsonObject(ConversationHistoryListJsonObject conversationHistoryListJsonObject) {
		mConversationHistoryListJsonObject = conversationHistoryListJsonObject;
	}
	
	public long getUserUpdateAt() {
		return userUpdateAt;
	}
	public void setUserUpdateAt(long userUpdateAt) {
		this.userUpdateAt = userUpdateAt;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getFriendId() {
		return friendId;
	}
	public void setFriendId(long friendId) {
		this.friendId = friendId;
	}
	public String getFriendMobile() {
		return friendMobile;
	}
	public void setFriendMobile(String friendMobile) {
		this.friendMobile = friendMobile;
	}
	public String getFriendName() {
		return friendName;
	}
	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	public String getFriendAliasName() {
		return friendAliasName;
	}
	public void setFriendAliasName(String friendAliasName) {
		this.friendAliasName = friendAliasName;
	}
	public String getFriendPic() {
		return friendPic;
	}
	public void setFriendPic(String friendPic) {
		this.friendPic = friendPic;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getRelationId() {
		return relationId;
	}
	public void setRelationId(long relationId) {
		this.relationId = relationId;
	}
	public long getCreateAt() {
		return createAt;
	}
	public void setCreateAt(long createAt) {
		this.createAt = createAt;
	}
	public String getMsgUnread() {
		return msgUnread;
	}
	public void setMsgUnread(String msgUnread) {
		this.msgUnread = msgUnread;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getFsize() {
		return fsize;
	}
	public void setFsize(String fsize) {
		this.fsize = fsize;
	}
	public String getRatio() {
		return ratio;
	}
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public int getReply() {
		return reply;
	}
	public void setReply(int reply) {
		this.reply = reply;
	}
}
