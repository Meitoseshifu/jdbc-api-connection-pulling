package org.example.demo;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Properties;


public class JdbcDemoApp {
    private static DataSource dataSource;

    @SneakyThrows
    public static void main(String[] args) {
        //initializeDataSource();
        initializePooledCustomDataSource();
        //initializeHikariCpDataSource();

        long start = System.nanoTime();
        for (int i = 1; i <= 1000; i++) {
            try(var connection = dataSource.getConnection()) {
                //trali vali festivali
            }
        }
        long end = System.nanoTime();
        System.out.println("It took: " + (end - start) / 1_000_000 + " millis");
    }

    private static void initializeDataSource() {
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setURL("jdbc:postgresql://localhost:5432/postgres");
        pgSimpleDataSource.setUser("postgres");
        pgSimpleDataSource.setPassword("postgres");
        dataSource = pgSimpleDataSource;
    }

    private static void initializePooledCustomDataSource() {
        PooledDataSource pooledDataSource = new PooledDataSource("jdbc:postgresql://localhost:5432/postgres","postgres", "postgres");
        dataSource = pooledDataSource;
    }

    @SneakyThrows
    private static void initializeHikariCpDataSource() {
        InputStream inputStream = JdbcDemoApp.class.getClassLoader().getResourceAsStream("application.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        HikariConfig config = new HikariConfig(properties);
        dataSource = new HikariDataSource(config);
    }

}
