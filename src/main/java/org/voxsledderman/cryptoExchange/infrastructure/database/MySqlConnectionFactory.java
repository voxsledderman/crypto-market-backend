package org.voxsledderman.cryptoExchange.infrastructure.database;

import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.voxsledderman.cryptoExchange.infrastructure.config.MySqlConnectionData;

import java.sql.SQLException;


@Getter
public class MySqlConnectionFactory {

    private final HikariDataSource dataSource;
    private final ConnectionSource connectionSource;

    public MySqlConnectionFactory(MySqlConnectionData data) throws SQLException {

        HikariConfig config = new HikariConfig();
        String url = "jdbc:mysql://%s:%d/%s"
                .formatted(data.host(), data.port(), data.database());
        config.setJdbcUrl(url);
        config.setUsername(data.username());
        config.setPassword(data.password());

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");

        config.setMaximumPoolSize(data.maxConnections());
        config.setPoolName("MySQL-Pool");

        this.dataSource = new HikariDataSource(config);
        this.connectionSource = new DataSourceConnectionSource(dataSource, url);
    }

    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
