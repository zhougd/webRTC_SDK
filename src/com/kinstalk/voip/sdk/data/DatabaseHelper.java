package com.kinstalk.voip.sdk.data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.kinstalk.voip.sdk.data.dao.AbstractImageDao;
import com.kinstalk.voip.sdk.data.model.AbstractDataItem;
import com.kinstalk.voip.sdk.data.model.PicWallDataItem;
import com.kinstalk.voip.sdk.logic.contact.json.ContactJsonObject;
import com.kinstalk.voip.sdk.logic.contact.json.ContactListJsonObject;
import com.kinstalk.voip.sdk.logic.contact.json.MyFriendJsonObject;
import com.kinstalk.voip.sdk.logic.conversation.json.ConversationHistoryJsonObject;
import com.kinstalk.voip.sdk.logic.conversation.json.ConversationHistoryListJsonObject;
import com.kinstalk.voip.sdk.logic.user.json.UserDetailJsonObject;
import com.kinstalk.voip.sdk.logic.user.json.UserInformationJsonObject;
import com.kinstalk.voip.sdk.logic.user.json.UserLoginJsonObject;
import com.kinstalk.voip.sdk.common.Log;
import com.kinstalk.voip.sdk.common.UserPreferences;

class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
	private static DataBaseVersion mDbVersion = new DataBaseVersion();

	private static DatabaseHelper gInstance = null;
	private static Context gContext;

	private static HashMap<Class<?>, Dao<?, ?>> mDaoMap = new HashMap<Class<?>, Dao<?, ?>>(10);
	private static HashMap<Class<?>, Uri> mUriMap = new HashMap<Class<?>, Uri>(10);

	public static final void setContext(Context context)
	{
		gContext = context;
	}
	
	static class DataBaseVersion {
		public DataBaseVersion() {
			mDatabaseVersion = UserPreferences.getInt(mDatabaseVersionKey, 0);
			mDatabaseSupportedDataTypeHashCode = UserPreferences.getInt(mDatabaseSupportedDataTypeHashCodeKey, 0);
		}
		int getmDatabaseSupportedDataTypeHashCode() {
			return mDatabaseSupportedDataTypeHashCode;
		}
		void setmDatabaseSupportedDataTypeHashCode(int mDatabaseSupportedDataTypeHashCode) {
			this.mDatabaseSupportedDataTypeHashCode = mDatabaseSupportedDataTypeHashCode;
		}
		String getmDatabaseSupportedDataTypeHashCodeKey() {
			return mDatabaseSupportedDataTypeHashCodeKey;
		}
		void setmDatabaseSupportedDataTypeHashCodeKey(String mDatabaseSupportedDataTypeHashCodeKey) {
			this.mDatabaseSupportedDataTypeHashCodeKey = mDatabaseSupportedDataTypeHashCodeKey;
		}
		int getmDatabaseVersion() {
			return mDatabaseVersion;
		}
		void setmDatabaseVersion(int mDatabaseVersion) {
			this.mDatabaseVersion = mDatabaseVersion;
		}
		String getmDatabaseVersionKey() {
			return mDatabaseVersionKey;
		}
		void setmDatabaseVersionKey(String mDatabaseVersionKey) {
			this.mDatabaseVersionKey = mDatabaseVersionKey;
		}
		String getmDataBaseName() {
			return mDataBaseName;
		}
		private int mDatabaseSupportedDataTypeHashCode;
		private String mDatabaseSupportedDataTypeHashCodeKey = UserPreferences.Key.DATABASEVERSION_SUPPORTEDDATATYPEHASHCODE;
		private int mDatabaseVersion;
		private String mDatabaseVersionKey = UserPreferences.Key.DATABASEVERSION_NUMBER;
		private final String mDataBaseName = "weaver_data";
		
		void persistVersionAndHashCode() {
			UserPreferences.setInt(mDatabaseVersionKey, mDatabaseVersion);
			UserPreferences.setInt(mDatabaseSupportedDataTypeHashCodeKey, mDatabaseSupportedDataTypeHashCode);
		}
	}
	
	private static final Class<?>[] mDatabaseSupportedDataType= {
			ContactJsonObject.class,
			ContactListJsonObject.class,
			UserDetailJsonObject.class,
			MyFriendJsonObject.class,
			ConversationHistoryJsonObject.class,
			ConversationHistoryListJsonObject.class,
			UserLoginJsonObject.class,
			UserInformationJsonObject.class};
	
	private static final void addAllDataTypes() {
		String uriPre = "content://weaver/data/";

		for (int i = 0; i < Array.getLength(mDatabaseSupportedDataType); i++) {
			mUriMap.put(mDatabaseSupportedDataType[i], Uri.parse(uriPre + mDatabaseSupportedDataType[i].getName()));
		}

		int hashCode = 0;
		String databaseStr = "";
		for (int i = 0; i < Array.getLength(mDatabaseSupportedDataType); i++) {
			mUriMap.put(mDatabaseSupportedDataType[i], Uri.parse(uriPre + mDatabaseSupportedDataType[i].getName()));
			databaseStr += mDatabaseSupportedDataType[i].toString();
			Field[] field = mDatabaseSupportedDataType[i].getDeclaredFields();
			for (int j = 0; j < field.length; j++) {
				Annotation[] anno = field[j].getDeclaredAnnotations();
				if (anno != null && anno.length > 0) {
					for (int k = 0; k < anno.length; k++) {
						String annoStr = anno[k].toString();
						if (annoStr != null && annoStr.contains("@com.j256.ormlite.field.DatabaseField")) {
							Log.i(DatabaseHelper.class, "TableUtils: " + field[j].toString() + " has annotations" + "[" + k + "]=" + annoStr);
							databaseStr += field[j].toString();
							break;
						}
					}
				} else {
					Log.i(DatabaseHelper.class, "TableUtils: " + field[j].toString() + "has annotations.");
				}
			}
		}
		hashCode = databaseStr.hashCode();
		if (mDbVersion.getmDatabaseSupportedDataTypeHashCode() != hashCode) {
			Log.i(DatabaseHelper.class,
					"TableUtils: onCreate old version[" + mDbVersion.getmDatabaseVersion() + "] with hashcode"
							+ mDbVersion.getmDatabaseSupportedDataTypeHashCode());
			mDbVersion.setmDatabaseVersion(mDbVersion.getmDatabaseVersion() + 1);
			mDbVersion.setmDatabaseSupportedDataTypeHashCode(hashCode);
			mDbVersion.persistVersionAndHashCode();
		}
		Log.i(DatabaseHelper.class,
				"TableUtils: onCreate databaseStr len = " + databaseStr.length() + "create version[" + mDbVersion.getmDatabaseVersion() + "] with hashCode["
						+ mDbVersion.getmDatabaseSupportedDataTypeHashCode() + "]");
	}

	public static final DatabaseHelper getInstance()
	{
		if (gInstance == null && gContext != null)
		{
			addAllDataTypes();
			gInstance = new DatabaseHelper(gContext);
		}
		return gInstance;
	}

	private DatabaseHelper(Context context)
	{
		super(context, mDbVersion.getmDataBaseName(), null, mDbVersion.getmDatabaseVersion());
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource connection)
	{
		try
		{
			Set<Class<?>> set = mUriMap.keySet();
			Class<?> key;
			for (Iterator<Class<?>> itr = set.iterator(); itr.hasNext();) {
				key = (Class<?>)itr.next();
				TableUtils.createTableIfNotExists(connection, key);
			}
			
			Log.i(getClass().getName(), "TableUtils: onCreate table OK!");
		}
		catch (SQLException e)
		{
			 Log.e(getClass().getName(), "TableUtils: onCreate table failed!", e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource connection, int arg2, int arg3)
	{
//		try
//		{
//			Set<Class<?>> set = mUriMap.keySet();
//			Class<?> key;
//			for (Iterator<Class<?>> itr = set.iterator(); itr.hasNext();) {
//				key = (Class<?>)itr.next();
//				TableUtils.dropTable(connection, key, true);
//			}
//			
//			onCreate(arg0, connection);
//			Log.i(getClass().getName(), "TableUtils: onUpgrade table OK!" + "oldVersion:" + arg2 + "newVersion" + arg3);
//		}
//		catch (SQLException e)
//		{
//			 Log.d(getClass().getName(), "TableUtils: onUpgrade table failed!", e);
//		}
		onCreate(arg0, connection);
		Log.i(getClass().getName(), "TableUtils: onUpgrade table OK!" + "oldVersion:" + arg2 + "newVersion" + arg3);
	}

	@Override
	public void close()
	{
		super.close();
	}

	@SuppressWarnings("unchecked")
	public <D extends Dao<T, ?>, T> D getDao(Class<T> clazz) throws SQLException
	{
		D result = (D) mDaoMap.get(clazz);
		if (result == null)
		{
			if(clazz == PicWallDataItem.class)
			{
				result = (D)new AbstractImageDao<T, String>();
			}
			
			result = super.getDao(clazz);
			if (result != null)
			{
				mDaoMap.put(clazz, result);
			}
		}

		return result;
	}

	public Uri getUri(Class<? extends AbstractDataItem> c)
	{
		return mUriMap.get(c);
	}
}
