package com.kinstalk.voip.sdk.data.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.CloseableWrappedIterable;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.ObjectCache;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.dao.RawRowObjectMapper;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.GenericRowMapper;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.PreparedUpdate;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.support.DatabaseResults;
import com.j256.ormlite.table.ObjectFactory;

public class AbstractImageDao<T, V> implements Dao<T, String>
{

	public AbstractImageDao()
	{
		
	}

	@Override
	public CloseableIterator<T> closeableIterator()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void assignEmptyForeignCollection(T arg0, String arg1) throws SQLException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public <CT> CT callBatchTasks(Callable<CT> arg0) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearObjectCache()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeLastIterator() throws SQLException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commit(DatabaseConnection arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public long countOf() throws SQLException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long countOf(PreparedQuery<T> arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int create(T arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public T createIfNotExists(T arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public com.j256.ormlite.dao.Dao.CreateOrUpdateStatus createOrUpdate(T arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(T arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Collection<T> arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(PreparedDelete<T> arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DeleteBuilder<T, String> deleteBuilder()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteById(String arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteIds(Collection<String> arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void endThreadConnection(DatabaseConnection arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public int executeRaw(String arg0, String... arg1) throws SQLException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int executeRawNoArgs(String arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String extractId(T arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FieldType findForeignFieldType(Class<?> arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConnectionSource getConnectionSource()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<T> getDataClass()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <FT> ForeignCollection<FT> getEmptyForeignCollection(String arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObjectCache getObjectCache()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RawRowMapper<T> getRawRowMapper()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericRowMapper<T> getSelectStarRowMapper() throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CloseableWrappedIterable<T> getWrappedIterable()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CloseableWrappedIterable<T> getWrappedIterable(PreparedQuery<T> arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean idExists(String arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAutoCommit() throws SQLException
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAutoCommit(DatabaseConnection arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTableExists() throws SQLException
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUpdatable()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CloseableIterator<T> iterator()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CloseableIterator<T> iterator(int arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CloseableIterator<T> iterator(PreparedQuery<T> arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CloseableIterator<T> iterator(PreparedQuery<T> arg0, int arg1) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T mapSelectStarRow(DatabaseResults arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String objectToString(T arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean objectsEqual(T arg0, T arg1) throws SQLException
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<T> query(PreparedQuery<T> arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryBuilder<T, String> queryBuilder()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> queryForAll() throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> queryForEq(String arg0, Object arg1) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> queryForFieldValues(Map<String, Object> arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> queryForFieldValuesArgs(Map<String, Object> arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T queryForFirst(PreparedQuery<T> arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T queryForId(String arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> queryForMatching(T arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> queryForMatchingArgs(T arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T queryForSameId(T arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericRawResults<String[]> queryRaw(String arg0, String... arg1) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <UO> GenericRawResults<UO> queryRaw(String arg0, RawRowMapper<UO> arg1, String... arg2) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericRawResults<Object[]> queryRaw(String arg0, DataType[] arg1, String... arg2) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <UO> GenericRawResults<UO> queryRaw(String arg0, DataType[] arg1, RawRowObjectMapper<UO> arg2, String... arg3) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long queryRawValue(String arg0, String... arg1) throws SQLException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int refresh(T arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void rollBack(DatabaseConnection arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAutoCommit(boolean arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAutoCommit(DatabaseConnection arg0, boolean arg1) throws SQLException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObjectCache(boolean arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObjectCache(ObjectCache arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObjectFactory(ObjectFactory<T> arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public DatabaseConnection startThreadConnection() throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(T arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(PreparedUpdate<T> arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UpdateBuilder<T, String> updateBuilder()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateId(T arg0, String arg1) throws SQLException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateRaw(String arg0, String... arg1) throws SQLException
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
