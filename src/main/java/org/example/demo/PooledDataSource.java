package org.example.demo;

import lombok.SneakyThrows;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class PooledDataSource extends PGSimpleDataSource {
    private final int POOL_SIZE = 10;
    private Queue<Connection> connectionPool;

    @SneakyThrows
    public PooledDataSource(String url, String username, String password) {
        super();
        setURL(url);
        setUser(username);
        setPassword(password);
        connectionPool = new ConcurrentLinkedDeque<>();
        for(int i = 0; i < POOL_SIZE; i++) {
            Connection physicalConnection = super.getConnection();
            ConnectionProxy connection = new ConnectionProxy(physicalConnection, connectionPool);
            connectionPool.add(connection);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connectionPool.poll();
    }
}
