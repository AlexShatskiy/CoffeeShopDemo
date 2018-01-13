package com.sh.coffeeshop.dao.connection;

import com.sh.coffeeshop.dao.exception.ConnectionPoolException;
import com.sh.coffeeshop.dao.manager.DaoPropertiesParameter;
import com.sh.coffeeshop.dao.manager.DaoPropertiesResourceManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

/**
 * Сonnect to the database
 */
@Component
public final class ConnectionPool implements InitializingBean, DisposableBean {

	private static final Logger log = LogManager.getRootLogger();
	private final static ConnectionPool instance = new ConnectionPool();

	private BlockingQueue<Connection> connectionQueue;
	private BlockingQueue<Connection> givenAwayConQueue;

	private String driverName;
	private String url;
	private String user;
	private String password;
	private int poolSize;

	private ConnectionPool() {
        DaoPropertiesResourceManager resourceManager = DaoPropertiesResourceManager.getInstance();
		String mainSetting = resourceManager.getValue(DaoPropertiesParameter.DAO_ACTIVE);
		if("mysql".equals(mainSetting)) {
			this.driverName = resourceManager.getValue(DaoPropertiesParameter.MYSQL_DRIVER);
			this.url = resourceManager.getValue(DaoPropertiesParameter.MYSQL_URL);
			this.user = resourceManager.getValue(DaoPropertiesParameter.MYSQL_USER);
			this.password = resourceManager.getValue(DaoPropertiesParameter.MYSQL_PASSWORD);
			try {
				this.poolSize = Integer.parseInt(resourceManager.getValue(DaoPropertiesParameter.MYSQL_POOLSIZE));
			} catch (NumberFormatException e) {
				log.error("fail in ConnectionPool", e);
				this.poolSize = 6;
			}
			//else oracle
		} else {
			this.driverName = resourceManager.getValue(DaoPropertiesParameter.ORACLE_DRIVER);
			this.url = resourceManager.getValue(DaoPropertiesParameter.ORACLE_URL);
			this.user = resourceManager.getValue(DaoPropertiesParameter.ORACLE_USER);
			this.password = resourceManager.getValue(DaoPropertiesParameter.ORACLE_PASSWORD);
			try {
				this.poolSize = Integer.parseInt(resourceManager.getValue(DaoPropertiesParameter.ORACLE_POOLSIZE));
			} catch (NumberFormatException e) {
				log.error("fail in ConnectionPool", e);
				this.poolSize = 6;
			}
		}
	}

	//for spring, destroy ConnectionPool
	@Override
	public void destroy() throws Exception {
		destroyConnectionPool();
	}

	//for spring, init ConnectionPool
	@Override
	public void afterPropertiesSet() throws Exception {
		initPoolData();
	}

	public static ConnectionPool getInstance() {
		return instance;
	}

	public void initPoolData() throws ConnectionPoolException {

		try {
			Class.forName(driverName);

			givenAwayConQueue = new ArrayBlockingQueue<Connection>(poolSize);
			connectionQueue = new ArrayBlockingQueue<Connection>(poolSize);

			for (int i = 0; i < poolSize; i++) {
				Connection connection = DriverManager.getConnection(url, user, password);
				PooledConnection pooledConnection = new PooledConnection(connection);

				connectionQueue.add(pooledConnection);
			}

		} catch (ClassNotFoundException e) {
			log.error(e);
			throw new ConnectionPoolException("ClassNotFoundException in ConnectionPool", e);
		} catch (SQLException e) {
			log.error(e);
			throw new ConnectionPoolException("SQLException in ConnectionPool", e);
		}
	}

	public Connection takeConnection() throws ConnectionPoolException {
		Connection connection = null;

		try {
			connection = connectionQueue.take();
			givenAwayConQueue.add(connection);
		} catch (InterruptedException e) {
			log.error(e);
			throw new ConnectionPoolException("InterruptedException in ConnectionPool", e);
		}
		return connection;
	}

	public void destroyConnectionPool() throws ConnectionPoolException {
		clearConnectionQueue();
	}

	private void clearConnectionQueue() throws ConnectionPoolException {

		try {
			closeConnectionsQueue(givenAwayConQueue);
			closeConnectionsQueue(connectionQueue);
		} catch (SQLException e) {
			log.error(e);
			throw new ConnectionPoolException("SQLException in ConnectionPool.clearConnectionQueue()", e);
		}
	}

	private void closeConnectionsQueue(BlockingQueue<Connection> queue) throws SQLException {
		Connection connection;
		while ((connection = queue.poll()) != null) {
			if (!connection.getAutoCommit()) {
				connection.commit();
			}
			((PooledConnection) connection).reallyClose();
		}
	}

	public static void closeConnect(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) throws ConnectionPoolException{
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				log.error(e);
				throw new ConnectionPoolException("fail in closeConnect(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet)", e);
			}
		}
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				log.error(e);
				throw new ConnectionPoolException("fail in closeConnect(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet)", e);
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				log.error(e);
				throw new ConnectionPoolException("fail in closeConnect(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet)", e);
			}
		}
	}

	private class PooledConnection implements Connection {

		private Connection connection;

		public PooledConnection(Connection c) throws SQLException {
			this.connection = c;
			this.connection.setAutoCommit(true);
		}

		public void reallyClose() throws SQLException {
			connection.close();
		}

		@Override
		public void close() throws SQLException {

			if (connection.isClosed()) {
				throw new SQLException("connection.isClosed()");
			}
			if (connection.isReadOnly()) {
				connection.setReadOnly(false);
			}
			if (!givenAwayConQueue.remove(this)) {
				throw new SQLException("!givenAwayConQueue.remove(this)");
			}

			if (!connectionQueue.offer(this)) {
				throw new SQLException("!connectionQueue.offer(this)");
			}
		}

		@Override
		public <T> T unwrap(Class<T> iface) throws SQLException {
			return connection.unwrap(iface);
		}

		@Override
		public boolean isWrapperFor(Class<?> iface) throws SQLException {
			return connection.isWrapperFor(iface);
		}

		@Override
		public Statement createStatement() throws SQLException {
			return connection.createStatement();
		}

		@Override
		public PreparedStatement prepareStatement(String sql) throws SQLException {
			return connection.prepareStatement(sql);
		}

		@Override
		public CallableStatement prepareCall(String sql) throws SQLException {
			return connection.prepareCall(sql);
		}

		@Override
		public String nativeSQL(String sql) throws SQLException {
			return connection.nativeSQL(sql);
		}

		@Override
		public void setAutoCommit(boolean autoCommit) throws SQLException {
			connection.setAutoCommit(autoCommit);
		}

		@Override
		public boolean getAutoCommit() throws SQLException {
			return connection.getAutoCommit();
		}

		@Override
		public void commit() throws SQLException {
			connection.commit();
		}

		@Override
		public void rollback() throws SQLException {
			connection.rollback();
		}

		@Override
		public boolean isClosed() throws SQLException {
			return connection.isClosed();
		}

		@Override
		public DatabaseMetaData getMetaData() throws SQLException {
			return connection.getMetaData();
		}

		@Override
		public void setReadOnly(boolean readOnly) throws SQLException {
			connection.setReadOnly(readOnly);
		}

		@Override
		public boolean isReadOnly() throws SQLException {
			return connection.isReadOnly();
		}

		@Override
		public void setCatalog(String catalog) throws SQLException {
			connection.setCatalog(catalog);
		}

		@Override
		public String getCatalog() throws SQLException {
			return connection.getCatalog();
		}

		@Override
		public void setTransactionIsolation(int level) throws SQLException {
			connection.setTransactionIsolation(level);
		}

		@Override
		public int getTransactionIsolation() throws SQLException {
			return connection.getTransactionIsolation();
		}

		@Override
		public SQLWarning getWarnings() throws SQLException {
			return connection.getWarnings();
		}

		@Override
		public void clearWarnings() throws SQLException {
			connection.clearWarnings();
		}

		@Override
		public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
			return connection.createStatement(resultSetType, resultSetConcurrency);
		}

		@Override
		public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
				throws SQLException {
			return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
		}

		@Override
		public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
				throws SQLException {
			return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
		}

		@Override
		public Map<String, Class<?>> getTypeMap() throws SQLException {
			return connection.getTypeMap();
		}

		@Override
		public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
			connection.setTypeMap(map);
		}

		@Override
		public void setHoldability(int holdability) throws SQLException {
			connection.setHoldability(holdability);
		}

		@Override
		public int getHoldability() throws SQLException {
			return connection.getHoldability();
		}

		@Override
		public Savepoint setSavepoint() throws SQLException {
			return connection.setSavepoint();
		}

		@Override
		public Savepoint setSavepoint(String name) throws SQLException {
			return connection.setSavepoint(name);
		}

		@Override
		public void rollback(Savepoint savepoint) throws SQLException {
			connection.rollback(savepoint);
		}

		@Override
		public void releaseSavepoint(Savepoint savepoint) throws SQLException {
			connection.releaseSavepoint(savepoint);
		}

		@Override
		public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
				throws SQLException {
			return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
		}

		@Override
		public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
				int resultSetHoldability) throws SQLException {
			return connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
		}

		@Override
		public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
				int resultSetHoldability) throws SQLException {
			return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
		}

		@Override
		public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
			return connection.prepareStatement(sql, autoGeneratedKeys);
		}

		@Override
		public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
			return connection.prepareStatement(sql, columnIndexes);
		}

		@Override
		public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
			return connection.prepareStatement(sql, columnNames);
		}

		@Override
		public Clob createClob() throws SQLException {
			return connection.createClob();
		}

		@Override
		public Blob createBlob() throws SQLException {
			return connection.createBlob();
		}

		@Override
		public NClob createNClob() throws SQLException {
			return connection.createNClob();
		}

		@Override
		public SQLXML createSQLXML() throws SQLException {
			return connection.createSQLXML();
		}

		@Override
		public boolean isValid(int timeout) throws SQLException {
			return connection.isValid(timeout);
		}

		@Override
		public void setClientInfo(String name, String value) throws SQLClientInfoException {
			connection.setClientInfo(name, value);
		}

		@Override
		public void setClientInfo(Properties properties) throws SQLClientInfoException {
			connection.setClientInfo(properties);
		}

		@Override
		public String getClientInfo(String name) throws SQLException {
			return connection.getClientInfo(name);
		}

		@Override
		public Properties getClientInfo() throws SQLException {
			return connection.getClientInfo();
		}

		@Override
		public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
			return connection.createArrayOf(typeName, elements);
		}

		@Override
		public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
			return connection.createStruct(typeName, attributes);
		}

		@Override
		public void setSchema(String schema) throws SQLException {
			connection.setSchema(schema);
		}

		@Override
		public String getSchema() throws SQLException {
			return connection.getSchema();
		}

		@Override
		public void abort(Executor executor) throws SQLException {
			connection.abort(executor);
		}

		@Override
		public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
			connection.setNetworkTimeout(executor, milliseconds);
		}

		@Override
		public int getNetworkTimeout() throws SQLException {
			return connection.getNetworkTimeout();
		}
	}
}
