package by.epam.osipov.internet.provider.pool;

import by.epam.osipov.internet.provider.exception.ConnectionPoolException;
import by.epam.osipov.internet.provider.exception.DatabaseConnectorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Threadsafe connection pool
 */
public class ConnectionPool {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final int POOL_SIZE = 5;
    private static final int TIMEOUT_VALID = 3;

    private BlockingQueue<ConnectionProxy> availableConnections;

    private static AtomicBoolean isInitialized = new AtomicBoolean(false);
    private static Lock initializationLock = new ReentrantLock();

    private static ConnectionPool instance;

    /**
     * ConnectionPool constructor.
     * Initialize connection pool
     */
    private ConnectionPool() {
        availableConnections = new ArrayBlockingQueue<ConnectionProxy>(POOL_SIZE);

        while (availableConnections.size() != POOL_SIZE) {
            int rest = POOL_SIZE - availableConnections.size();
            for (int i = 0; i < rest; i++)
                try {
                    ConnectionProxy connection = new ConnectionProxy(DatabaseConnector.getConnection());
                    connection.setAutoCommit(true);
                    availableConnections.put(connection);
                    LOGGER.info("Connection was initialized and added to pool");
                } catch (InterruptedException | DatabaseConnectorException | SQLException e) {
                    LOGGER.error("Connection wasn't initialized" + e);
                }
            if (availableConnections.isEmpty()) {
                LOGGER.fatal("Pool was not initialized");
                throw new RuntimeException();
            }
        }
    }

    /**
     * Returns ConnectionPool instance if
     * it is initialized.
     * Otherwise initialize ConnectionPool instance
     * and returns it.
     *
     * @return Connection pool instance
     */
    public static ConnectionPool getInstance() {
        if (isInitialized.compareAndSet(false, true)) {
            initializationLock.lock();
            try {
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            } finally {
                initializationLock.unlock();
            }
        }

        return instance;
    }

    /**
     * Returns available connection from pool
     *
     * @return connection database connection
     */
    public ConnectionProxy getConnection() throws ConnectionPoolException {
        ConnectionProxy connection;
        try {
            connection = availableConnections.take();
            LOGGER.info("Connection was taken from pool");
        } catch (InterruptedException e) {
            throw new ConnectionPoolException("Exception in ConnectionPool while trying to take connection", e);
        }

        return connection;
    }

    /**
     * Returns connection back to pool
     *
     * @param connection connection to return
     */
    void putConnection(ConnectionProxy connection) throws ConnectionPoolException {

        // if (availableConnections.size() == POOL_SIZE) {
        //    return;
        //}
        try {
            if (connection.isValid(TIMEOUT_VALID)) {
                availableConnections.put(connection);
            } else {
                ConnectionProxy newConnection = new ConnectionProxy(DatabaseConnector.getConnection());
                newConnection.setAutoCommit(true);
                availableConnections.put(newConnection);
            }
            LOGGER.info("Connection was put to pool");
        } catch (DatabaseConnectorException | InterruptedException | SQLException e) {
            throw new ConnectionPoolException("Exception in ConnectionPool while trying to put connection", e);
        }
    }

    /**
     * Closes all connections from connection pool
     */
    public void closeAll() {
        if (isInitialized.compareAndSet(true, false)) {

            for (int i = 0; i < POOL_SIZE; i++) {
                try {
                    ConnectionProxy connection = availableConnections.take();

                    if (!connection.getAutoCommit()) {
                        connection.commit();
                    }

                    connection.realClose();
                    LOGGER.info(String.format("closed successfully (#%d)", i));
                } catch (SQLException | InterruptedException e) {
                    LOGGER.warn(String.format("problem with connection closing (#%d)", i));
                }
            }
        }
    }

    /**
     * Returns flag of initialization
     */
    public static boolean isInitialized() {
        return isInitialized.get();
    }
}